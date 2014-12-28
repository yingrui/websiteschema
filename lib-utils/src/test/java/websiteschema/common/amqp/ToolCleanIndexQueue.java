/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.common.amqp;

/**
 *
 * @author ray
 */
public class ToolCleanIndexQueue {

    public void cleanQueue(String server, String queueName) {
        RabbitQueue<String> queue = new RabbitQueue<String>(server, queueName);
        String msg = queue.poll(String.class, 1000);
        while (msg != null) {
            System.out.println(msg);
            msg = queue.poll(String.class, 1000);
        }
        queue.close();
    }

    public static void main(String args[]) {
        String server = "172.16.100.161";
        String queueName = "index_queue_unit_guba";
        new ToolCleanIndexQueue().cleanQueue(server, queueName);
    }
}
