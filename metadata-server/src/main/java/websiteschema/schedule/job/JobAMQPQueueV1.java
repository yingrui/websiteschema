/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.schedule.job;

import java.util.Map;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import websiteschema.common.amqp.Message;
import websiteschema.common.amqp.RabbitQueue;
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
public class JobAMQPQueueV1 implements Job {

    private long jobId;
    private long schedulerId;
    private long startURLId;
    private String url;
    private JobMapper jobMapper;
    private WrapperMapper wrapperMapper;
    private StartURLMapper startURLMapper;
    private TaskMapper taskMapper;
    Logger l = Logger.getLogger(JobAMQPQueueV1.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey key = context.getJobDetail().getKey();
        l.debug("Instance " + key + " of schedulerId: " + schedulerId + ", and jobId is: " + jobId + ", and startURLId is: " + startURLId);
        websiteschema.model.domain.Job job = jobMapper.getById(jobId);
        String jobConfig = job.getConfigure();
        System.out.println("--------------------------"+jobConfig);
        l.debug(jobConfig);
        try {
            //生成Task,配置调度器
            Task task = new Task(schedulerId);
            task.setTaskType(Task.TYPE_LINK);
            taskMapper.insert(task);
            l.debug("created task " + task.getId());
            boolean suc = false;
            Map<String, String> conf = CollectionUtil.toMap(jobConfig);
            String queueName = conf.get("QUEUE_NAME");
            //声称Rabbitqueue
            RabbitQueue<Message> queue = StringUtil.isNotEmpty(queueName)
                    ? TaskHandler.getInstance().getQueue(queueName) : TaskHandler.getInstance().getQueue();
            Message msg = create(job);
            msg.setTaskId(task.getId());
            //发送信息，返回发送状态
            suc = queue.offer(msg);
            if (suc) {
                l.debug("Message about Job " + jobId + " has been emitted to queue: " + queue.getQueueName());
                task.setStatus(Task.SENT);
                taskMapper.update(task);
            } else {
                task.setStatus(Task.UNSENT);
                l.debug("Message about Job " + jobId + " can not send to queue: " + queue.getQueueName());
                task.setMessage("Message can not send to queue.");
                taskMapper.update(task);
            }
        } catch (Exception ex) {
            l.error(ex.getMessage(), ex);
        }
    }

    private Message create(websiteschema.model.domain.Job job) {
        StartURL startURL = startURLMapper.getById(startURLId);
        if (null != startURL) {
            if(null != getUrl()) {
                return new Message(jobId, startURLId, schedulerId, job.getWrapperId(), startURL.getSiteId(), startURL.getJobname(), getUrl(), job.getConfigure());
            }
            return new Message(jobId, startURLId, schedulerId, job.getWrapperId(), startURL.getSiteId(), startURL.getJobname(), startURL.getStartURL(), job.getConfigure());
        }
        return new Message(jobId, startURLId, schedulerId, job.getWrapperId(), null, null, null, job.getConfigure());
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
