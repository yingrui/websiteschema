/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.common.amqp;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author ray
 */
public class ToolAddtoQueue {

    public void addtoQueue(String server, String queueName, String... datas) {
        RabbitQueue queue = new RabbitQueue(server, queueName);
        List<String> list = Arrays.asList(datas);
        for (String data : list) {
            queue.offer(data);
        }
        queue.close();
    }

    public static void main(String args[]) {
        String server = "localhost";
        String queueName = "index_queue";
        String data1 = "http://moc.yenomtsae.abug/look,000001,7013404681.html";
        String data2 = "http://moc.yenomtsae.abug/look,000001,7013404730.html";
        new ToolAddtoQueue().addtoQueue(server, queueName, data1, data2);
    }
}
