/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.util.ArrayList;
import java.util.List;
import websiteschema.cluster.analyzer.Link;
import websiteschema.common.amqp.Message;
import websiteschema.common.amqp.RabbitQueue;
import websiteschema.common.amqp.QueueFactory;
import websiteschema.fb.annotation.Description;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.model.domain.Task;
import websiteschema.persistence.rdbms.TaskMapper;

/**
 *
 * @author ray
 */
@EI(name = {"NEW:ADDONE", "ADD:ADD"})
@EO(name = {"EO", "FATAL"})
@Description(desc = "将URL添加至URL Queue")
public class FBURLQueue extends FunctionBlock {

    @DI(name = "HOST", desc = "RabbitMQ server host")
    public String host;
    @DI(name = "PORT", desc = "RabbitMQ server port")
    public int port;
    @DI(name = "QUEUE", desc = "RabbitMQ server queue name")
    public String queueName;
    @DI(name = "URL", desc = "需要采集的链接")
    public String url;
    @DI(name = "LINKS", desc = "需要采集的链接")
    public List<Link> links;
    @DI(name = "JOBNAME", desc = "起始URL的jobname")
    public String jobname;
    @DI(name = "SITEID", desc = "起始URL的站点ID")
    public String siteId;
    @DI(name = "SID", desc = "startURLId")
    public long startURLId;
    @DI(name = "SCHEID", desc = "scheId")
    public long scheId;
    @DI(name = "WID", desc = "wrapperId")
    public long wrapperId;
    @DI(name = "JID", desc = "jobId")
    public long jobId;
    @DI(name = "CID", desc = "channelId")
    public long chnlId;
    @DI(name = "CFG", desc = "configure")
    public String configure;
    @DI(name = "DEPTH", desc = "URL深度")
    public int depth;

    @Algorithm(name = "ADDONE", desc = "将添加链接保存至HBase存储")
    public void addOne() {
        RabbitQueue<Message> queue = QueueFactory.getInstance().getQueue(host, port, queueName);
        try {
            Message msg = new Message(jobId, startURLId, scheId, wrapperId,
                    siteId, jobname,
                    url, //URL
                    configure, depth);
            queue.offer(msg);
            triggerEvent("EO");
        } catch (Exception ex) {
            triggerEvent("FATAL");
        }
    }

    @Algorithm(name = "ADD", desc = "将添加链接保存至HBase存储")
    public void add() {
        RabbitQueue<Message> queue = QueueFactory.getInstance().getQueue(host, port, queueName);
        try {
            if (null != links && links.size() > 0) {
                List<Message> addList = new ArrayList<Message>();
                for (Link u : links) {
                    Message msg = new Message(jobId, startURLId, scheId, wrapperId,
                            siteId, jobname,
                            u.getHref(), //URL
                            configure, depth);

                    msg.setChnlId(chnlId);
                    TaskMapper taskMapper = this.getContext().getSpringBean("taskMapper", TaskMapper.class);
                    if (null != taskMapper) {
                        Task task = new Task();
                        task.setScheduleId(scheId);
                        task.setStatus(Task.SENT);
                        taskMapper.insert(task);
                        msg.setTaskId(task.getId());
                    }
                    addList.add(msg);
                }
                queue.offer(addList.toArray(new Message[0]));
            }
            triggerEvent("EO");
        } catch (Exception ex) {
            l.error(ex.getMessage(), ex);
            triggerEvent("FATAL");
        }
    }
}
