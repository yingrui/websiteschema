/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.crawler;

import com.webrenderer.swing.BrowserFactory;
import org.junit.Test;
import org.w3c.dom.Document;
import websiteschema.crawler.browser.BrowserWebCrawler;

/**
 *
 * @author ray
 */
public class BrowserWebCrawlerTest {

    @Test
    public void test() throws InterruptedException {
        String url = "http://localhost:8080/";
        Crawler crawler = new BrowserWebCrawler();
        Document[] docs = crawler.crawl(url);
        System.out.println("----"+System.currentTimeMillis());
        crawler = null;

        //测试是否DOM对象已经被释放
        Thread.sleep(5000);

        Document doc = docs[0];
//        BrowserWebCrawler.print(doc);

        while(!BrowserFactory.shutdownMozilla()){
            System.out.println("-----"+System.currentTimeMillis());
            Thread.sleep(1000);
        }
    }
}
