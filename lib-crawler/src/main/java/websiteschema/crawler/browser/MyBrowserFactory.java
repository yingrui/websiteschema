/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.browser;

import com.webrenderer.swing.BrowserFactory;
import com.webrenderer.swing.IMozillaBrowserCanvas;
import com.webrenderer.swing.RenderingOptimization;
import org.apache.log4j.Logger;
import websiteschema.conf.Configure;

/**
 *
 * @author ray
 */
public class MyBrowserFactory {

    private static final String user = Configure.getDefaultConfigure().getProperty("Browser", "LicenseUser");
    private static final String serial = Configure.getDefaultConfigure().getProperty("Browser", "LicenseSerial");
    private static final Logger l = Logger.getLogger(MyBrowserFactory.class);

    static {
        l.info("Add Shutdown Hook");
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                l.info("Shutdown Mozilla : " + BrowserFactory.shutdownMozilla());
            }
        });
    }
    private static MyBrowserFactory ins = new MyBrowserFactory();
    // Improves scrolling performance on pages with windowless flash.
    RenderingOptimization renOps = null;

    public static MyBrowserFactory getInstance() {
        return ins;
    }

    private MyBrowserFactory() {
        BrowserFactory.setLicenseData(user, serial);
        renOps = new RenderingOptimization();
        renOps.setWindowlessFlashSmoothScrolling(true);
    }

    public IMozillaBrowserCanvas getBrowser() {
        IMozillaBrowserCanvas browser = BrowserFactory.spawnMozilla();
        browser.enableCache();
        browser.setRenderingOptimizations(renOps);
        browser.setHTTPHeadersEnabled(true);
        return browser;
    }
}
