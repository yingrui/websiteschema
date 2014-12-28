/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import websiteschema.fb.core.app.Application;
import websiteschema.fb.core.RuntimeContext;

/**
 *
 * @author ray
 */
public class WebCrawlerTest {

    String siteId = "www_qq_com_4845";

//    @Test
    public void test() throws InterruptedException {
        Application app = new Application();
        RuntimeContext context = app.getContext();
        context.loadConfigure("fb/links.app");

        Thread t = new Thread(app);
        t.start();
        t.join();
    }

    @Test
    public void testCrawler() throws InterruptedException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("URL", "http://finance.qq.com/a/20120110/002908.htm");
        map.put("SITEID", siteId);
        map.put("JOBNAME", siteId);
        map.put("CLS", "30");
        map.put("DIH", "[\"localhost:9001\"]");
        map.put("DBNAME", "NEWS");
        map.put("CRAWLER", "websiteschema.crawler.SimpleHttpCrawler");

        Application app = new Application();
        RuntimeContext context = app.getContext();
        context.loadConfigure("fb/crawler.app", map);

        Thread t = new Thread(app);
        t.start();
        t.join();
    }
}
