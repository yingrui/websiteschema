/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.crawler;

import com.webrenderer.swing.BrowserFactory;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import websiteschema.crawler.browser.BrowserWebCrawler;
import websiteschema.element.DocumentUtil;

import java.util.List;

/**
 *
 * @author ray
 */
public class BrowserWebCrawlerTest {

    @Test
    public void test() throws InterruptedException {
        String url = "http://fund.eastmoney.com/data/fundranking.html#tall;c0;r;szzf;pn50;ddesc;qsd20140111;qed20150111;qdii";
        Crawler crawler = new BrowserWebCrawler();
        Document[] docs = crawler.crawl(url);
        System.out.println("----"+System.currentTimeMillis());
        crawler = null;

        Document doc = docs[0];

        List<Node> nodes = DocumentUtil.getByXPath(doc, "//a");
        for (Node node : nodes) {
            NamedNodeMap attributes = node.getAttributes();
            if(attributes != null) {
                Node href = attributes.getNamedItem("href");
                if(null != href) {
                    System.out.println(href.getNodeValue() + " " + node.getNodeValue());
                }
            }
        }
    }
}
