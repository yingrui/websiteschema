/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler;

import org.junit.Test;
import org.w3c.dom.Document;
import websiteschema.element.DocumentUtil;

/**
 *
 * @author mgd
 */
public class SimpleCrawlerTest {

    private static String URL_STR = "http://mil.news.sohu.com/s2005/junshiguonei.shtml";

    @Test
    public void test() throws Exception {
        Crawler crawler = new SimpleHttpCrawler();
        Document doc = crawler.crawl(URL_STR)[0];
        assert(crawler.getHttpStatus() == 200);
        System.out.println(DocumentUtil.getXMLString(doc));
    }
}
