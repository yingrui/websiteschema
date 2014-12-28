/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.guba;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import websiteschema.fb.core.app.Application;
import websiteschema.fb.core.RuntimeContext;

/**
 *
 * @author ray
 */
public class EastmoneyCrawlerTest {

    String siteId = "guba_eastmoney_com_100";

    @Test
    public void fetchContent() throws InterruptedException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("URL", "http://guba.eastmoney.com/look,600111,1014347971.html");
        map.put("SITEID", siteId);
        map.put("JOBNAME", siteId);
        map.put("CLS", "custom");
        map.put("QUEUE_SERVER", "localhost");
        map.put("CRAWLER", "websiteschema.crawler.SimpleHttpCrawler");

        Application app = new Application();
        RuntimeContext context = app.getContext();
        context.loadConfigure("fb/guba/content.app", map);

        Thread t = new Thread(app);
        t.start();
        t.join();

        System.out.println("-------------\nStatus is: " + app.getStatus().getStatus() + " " + app.getStatus().getMessage());
    }

//    @Test
    public void fetchLinks() throws InterruptedException, Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("URL", "http://guba.eastmoney.com/topic,000009.html");
        map.put("SITEID", siteId);
        map.put("JOBNAME", siteId);
        map.put("UPATH", "html/body/div[@id='main']/div[@id='mainleft']/div[@class='liebiao']/div/ul");
        map.put("PTS", "[{\"regex\":\"(\\\\d+)\",\"name\":\"clicked\",\"path\":\"li[1]/text()\"},{\"name\":\"reply\",\"path\":\"li[2]/text()\"},{\"name\":\"text\",\"path\":\"li[3]/a[1]/text()\"},{\"name\":\"href\",\"path\":\"li[3]/a[1]/@href\"},{\"name\":\"author\",\"path\":\"li[4]/text()\"}]");
        map.put("STARTURLID", "1");
        map.put("JOBID", "2");
        map.put("SCHEID", "62");
        map.put("QUEUE_SERVER", "localhost");
        map.put("QUEUE_NAME", "url_queue_1");
        map.put("WRAPPERID", "8"); //股吧内容采集
        map.put("CRAWLER", "websiteschema.crawler.SimpleHttpCrawler");

        Application app = new Application();
        RuntimeContext context = app.getContext();
        context.loadConfigure("fb/guba/link.app", map);

        app.call();

        System.out.println("-------------\nStatus is: " + app.getStatus().getStatus() + " " + app.getStatus().getMessage());
    }

//    @Test
    public void fetchGubaList() throws InterruptedException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("URL", "http://guba.eastmoney.com/gblb.html");
        map.put("XPATH", "html/body/div[@class='gblb']/div[@class='hang2']/ul/li/a");
        map.put("CRAWLER", "websiteschema.crawler.SimpleHttpCrawler");
        map.put("SITEID", siteId);

        Application app = new Application();
        RuntimeContext context = app.getContext();
        context.loadConfigure("fb/guba/gblb.app", map);

        Thread t = new Thread(app);
        t.start();
        t.join();

        System.out.println("-------------\nStatus is: " + app.getStatus().getStatus() + " " + app.getStatus().getMessage());
    }

//    @Test
    public void sendIdx() throws InterruptedException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("QUEUE_SERVER", "localhost");

        Application app = new Application();
        RuntimeContext context = app.getContext();
        context.loadConfigure("fb/guba/send.app", map);

        Thread t = new Thread(app);
        t.start();
        t.join();

        System.out.println("-------------\nStatus is: " + app.getStatus().getStatus() + " " + app.getStatus().getMessage());
    }

//    @Test
    public void refreshReply() throws InterruptedException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("QUEUE_SERVER", "localhost");

        Application app = new Application();
        RuntimeContext context = app.getContext();
        context.loadConfigure("fb/guba/refreshReply.app", map);

        Thread t = new Thread(app);
        t.start();
        t.join();

        System.out.println("-------------\nStatus is: " + app.getStatus().getStatus() + " " + app.getStatus().getMessage());
    }
}
