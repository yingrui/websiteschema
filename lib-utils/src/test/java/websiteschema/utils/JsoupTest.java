/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.utils;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import websiteschema.element.DocumentUtil;

/**
 *
 * @author ray
 */
public class JsoupTest {

    @Test
    public void test() throws IOException {
//        Document doc = Jsoup.connect("http://news.163.com/12/0117/11/7NVE580G00014JB5.html").get();
        Document doc = Jsoup.connect("http://www.baidu.com/").get();
        String title = doc.title();
        System.out.println(title);
        org.w3c.dom.Document document = JsoupUtil.getInstance().convert(doc);
        System.out.println(DocumentUtil.getXMLString(document));
    }
}
