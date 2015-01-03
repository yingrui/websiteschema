/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.common.amqp;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import websiteschema.conf.Configure;

/**
 *
 * @author ray
 */
public class QueueFactory {

    private String username = "websiteschema";
    private String password = "websiteschema";

    private static QueueFactory ins = new QueueFactory();

    public static QueueFactory getInstance() {
        return ins;
    }
    private Map<String, RabbitQueue> queueRepos = new HashMap<String, RabbitQueue>();
    private Logger l = Logger.getLogger(QueueFactory.class);

    public QueueFactory() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfig(Configure config) {
        String username = config.getProperty("URLQueue", "Username", "websiteschema");
        String password = config.getProperty("URLQueue", "Password", "websiteschema");
        QueueFactory.getInstance().setUsername(username);
        QueueFactory.getInstance().setPassword(password);
    }

    public <T> RabbitQueue<T> getQueue(String host, int port, String queueName) {
        String key = host + ":" + port + queueName;
        l.debug("get queue : " + key);
        if (queueRepos.containsKey(key)) {
            l.debug("return queue from repo.");
            return queueRepos.get(key);
        } else {
            return create(key, host, port, queueName);
        }
    }

    private synchronized <T> RabbitQueue<T> create(String key, String host, int port, String queueName) {
        if (!queueRepos.containsKey(key)) {
            RabbitQueue<T> q = new RabbitQueue<T>(host, port, queueName, username, password);
            queueRepos.put(key, q);
            l.debug("created a queue and put into repo.");
            return q;
        } else {
            l.debug("return queue from repo.");
            return queueRepos.get(key);
        }
    }
}
