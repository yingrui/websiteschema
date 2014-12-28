/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.common.amqp;

/**
 *
 * @author ray
 */
public class ToolCleanQueue {

    public void cleanQueue(String server, String queueName) {
        RabbitQueue<Message> queue = new RabbitQueue<Message>(server, queueName);
        Message msg = queue.poll(Message.class, 1000);
        while (msg != null) {
            System.out.println(msg.getUrl() + "  " + msg.getWrapperId());
            if(msg.getWrapperId() != 3 && msg.getWrapperId() != 4) {
                queue.offer(msg);
            }
            msg = queue.poll(Message.class, 1000);
        }
        queue.close();
    }

    public static void main(String args[]) {
        String server = "172.16.100.161";
        String queueName = "url_queue";
        new ToolCleanQueue().cleanQueue(server, queueName);
    }
}
