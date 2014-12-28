/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.schedule.job;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import websiteschema.common.amqp.Message;
import websiteschema.common.amqp.RabbitQueue;
import websiteschema.model.domain.Channel;
import websiteschema.model.domain.StartURL;
import websiteschema.model.domain.Task;
import websiteschema.persistence.rdbms.*;
import websiteschema.schedule.TaskHandler;
import websiteschema.utils.CollectionUtil;
import websiteschema.utils.StringUtil;

/**
 *
 * @author ray
 */
public class JobMultiStartURL implements Job {

    private long jobId;
    private long schedulerId;
    private long startURLId;
    private ChannelMapper channelMapper;
    private JobMapper jobMapper;
    private WrapperMapper wrapperMapper;
    private StartURLMapper startURLMapper;
    private TaskMapper taskMapper;
    Logger l = Logger.getLogger(JobMultiStartURL.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey key = context.getJobDetail().getKey();
        l.debug("Instance " + key + " of schedulerId: " + schedulerId + ", and jobId is: " + jobId + ", and startURLId is: " + startURLId);
        websiteschema.model.domain.Job job = jobMapper.getById(jobId);
        StartURL startURL = startURLMapper.getById(startURLId);
        String siteId = startURL.getSiteId();
        l.debug("getChannelsBySiteId " + siteId);
        List<Channel> channels = channelMapper.getChannelsBySiteId(siteId);

        String jobConfig = job.getConfigure();
        l.debug(jobConfig);
        if (null != channels) {
            l.debug("there are " + channels.size() + " channels need to start.");
            Map<String, String> conf = CollectionUtil.toMap(jobConfig);
            String queueName = conf.get("QUEUE_NAME");
            RabbitQueue<Message> queue = StringUtil.isNotEmpty(queueName)
                    ? TaskHandler.getInstance().getQueue(queueName) : TaskHandler.getInstance().getQueue();
            com.rabbitmq.client.Channel channel = null;
            try {
                try {
                    channel = queue.getChannel();
                } catch (Exception ex) {
                    l.error(ex.getMessage(), ex);
                    queue.processException(ex);
                }
                if (null != channel) {
                    for (Channel chl : channels) {
//                        l.debug(chl.getChannel());
                        if (chl.getStatus() == Channel.STATUS_VALID) {
                            //仅发送有效的栏目。
                            Task task = new Task(schedulerId);
                            try {
                                task.setTaskType(Task.TYPE_LINK);
                                taskMapper.insert(task);
                                // 把栏目的URL添加到消息中，并发送出去。
                                Message msg = create(job, chl.getUrl());
                                msg.setTaskId(task.getId());
                                msg.setChnlId(chl.getId());
                                queue.offer(channel, msg);
                                l.debug("Message about Job " + jobId + " has been emitted to queue: " + queue.getQueueName());
                                task.setStatus(Task.SENT);
                                taskMapper.update(task);
                            } catch (Exception ex) {
                                task.setStatus(Task.UNSENT);
                                task.setMessage(ex.getMessage());
                                taskMapper.update(task);
                                l.error(ex.getMessage(), ex);
                                break;
                            }
                        }
                    }
                } else {
                    l.debug("can not get channel from queue: " + queue.getQueueName());
                }
            } catch (Exception ex) {
                l.error(ex.getMessage(), ex);
            } finally {
                // Close channel
                if (null != channel) {
                    try {
                        channel.close();
                    } catch (Exception ex) {
                        l.error(ex.getMessage(), ex);
                    }
                }
            }
        }
    }

    /**
     * 检查一个栏目是否可以被当作任务发送到消息队列中。
     * @param chnl
     * @param conf
     * @return
     */
    private boolean validChannel(Channel chnl, Map<String, String> conf) {
        String include = conf.get("INCLUDE_CHNL");
        String exclude = conf.get("EXCLUDE_CHNL");
        return true;
    }

    private Message create(websiteschema.model.domain.Job job, String url) {
        StartURL startURL = startURLMapper.getById(startURLId);
        return new Message(jobId, startURLId, schedulerId, job.getWrapperId(), startURL.getSiteId(), startURL.getJobname(), url, job.getConfigure());
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public long getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(long schedulerId) {
        this.schedulerId = schedulerId;
    }

    public long getStartURLId() {
        return startURLId;
    }

    public void setStartURLId(long startURLId) {
        this.startURLId = startURLId;
    }

    public JobMapper getJobMapper() {
        return jobMapper;
    }

    public void setJobMapper(JobMapper jobMapper) {
        this.jobMapper = jobMapper;
    }

    public StartURLMapper getStartURLMapper() {
        return startURLMapper;
    }

    public void setStartURLMapper(StartURLMapper startURLMapper) {
        this.startURLMapper = startURLMapper;
    }

    public WrapperMapper getWrapperMapper() {
        return wrapperMapper;
    }

    public void setWrapperMapper(WrapperMapper wrapperMapper) {
        this.wrapperMapper = wrapperMapper;
    }

    public TaskMapper getTaskMapper() {
        return taskMapper;
    }

    public void setTaskMapper(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    public ChannelMapper getChannelMapper() {
        return channelMapper;
    }

    public void setChannelMapper(ChannelMapper channelMapper) {
        this.channelMapper = channelMapper;
    }
}
