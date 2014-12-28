/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.hbase;

import org.junit.Test;
import websiteschema.model.domain.UrlLink;

/**
 *
 * @author ray
 */
public class UrlLinkTest {

    UrlLinkMapper mapper = new UrlLinkMapper();
    String rowKey = "http://moc.361.yenom//12/0121/13/7OA1HC8J00253B0H.html";

    @Test
    public void test() {
        UrlLink record = mapper.get(rowKey);
        System.out.println("    " + record.getRowKey());
        System.out.println("    " + record.getContent());
    }
}
