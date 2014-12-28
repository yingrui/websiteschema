/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import org.junit.Test;
import websiteschema.fb.core.RuntimeContext;
import websiteschema.fb.core.app.Application;

/**
 *
 * @author Administrator
 */
public class BrowserWebCrawlerTest {
    @Test
    public void testBrowserWebCrawler() throws InterruptedException {
        Application app = new Application();
        RuntimeContext context = app.getContext();
        context.loadConfigure("fb/BrowserWebCrawler.app");

        Thread t = new Thread(app);
        t.start();
        t.join();
    }
}
