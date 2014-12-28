/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.schedule;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import websiteschema.common.amqp.Message;
import websiteschema.common.amqp.RabbitQueue;
import websiteschema.metadata.utils.MetadataServerContext;

/**
 *
 * @author ray
 */
public class TaskHandler {

    private static TaskHandler ins = new TaskHandler();

    public static TaskHandler getInstance() {
        return ins;
    }
    private RabbitQueue<Message> queue;
    private Map<String, RabbitQueue<Message>> queueRepos = new HashMap<String, RabbitQueue<Message>>();
    private Logger l = Logger.getLogger(TaskHandler.class);

    TaskHandler() {
        String host = MetadataServerContext.getInstance().getConf().
                getProperty("URLQueue", "ServerHost", "localhost");
        String queueName = MetadataServerContext.getInstance().getConf().
                getProperty("URLQueue", "PriorQueueName", "url_queue");
        l.debug("create a new RabbitQueue instance with host: " + host + " and queue name: " + queueName);
        queue = new RabbitQueue<Message>(host, queueName);
        queueRepos.put(queueName, queue);
    }

    public RabbitQueue<Message> getQueue() {
        return queue;
    }

    public RabbitQueue<Message> getQueue(String queueName) {
        if (queueRepos.containsKey(queueName)) {
            return queueRepos.get(queueName);
        } else {
            String host = MetadataServerContext.getInstance().getConf().
                    getProperty("URLQueue", "ServerHost", "localhost");
            l.debug("create a new RabbitQueue instance with host: " + host + " and queue name: " + queueName);
            RabbitQueue<Message> q = new RabbitQueue<Message>(host, queueName);
            queueRepos.put(queueName, q);
            return q;
        }
    }
}
