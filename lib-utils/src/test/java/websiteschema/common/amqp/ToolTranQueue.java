/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.common.amqp;

/**
 *
 * @author ray
 */
public class ToolTranQueue {

    public void tranQueue(String server, String queueName, String server2, String queueName2) {
        RabbitQueue<Message> queue = new RabbitQueue<Message>(server, queueName);
        RabbitQueue<Message> queue2 = new RabbitQueue<Message>(server2, queueName2);
        Message msg = queue.poll(Message.class);
        while (msg != null) {
            msg = queue.poll(Message.class);
            if(null != msg) {
                queue2.offer(msg);
            }
        }
        queue.close();
        queue2.close();
    }

    public static void main(String args[]) {
        String server = "192.168.4.122";
        String queueName = "url_queue_1";
        String server2 = "192.168.4.38";
        String queueName2 = "url_queue_1";
        new ToolTranQueue().tranQueue(server, queueName, server2, queueName2);
    }
}
