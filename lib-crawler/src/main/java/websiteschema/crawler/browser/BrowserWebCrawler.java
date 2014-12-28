/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.browser;

import com.webrenderer.swing.BrowserFactory;
import com.webrenderer.swing.IMozillaBrowserCanvas;
import com.webrenderer.swing.ProxySetting;
import com.webrenderer.swing.dom.IDocument;
import com.webrenderer.swing.dom.IElement;
import com.webrenderer.swing.dom.IElementCollection;
import java.awt.BorderLayout;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import websiteschema.crawler.Crawler;
import websiteschema.crawler.WebPage;
import websiteschema.model.domain.cralwer.CrawlerSettings;
import websiteschema.utils.UrlLinkUtil;

/**
 *
 * @author ray
 */
public class BrowserWebCrawler implements Crawler {

    Logger l = Logger.getLogger(BrowserWebCrawler.class);
    IMozillaBrowserCanvas browser = null;
    JFrame frame;
    boolean loadImage = false;
    boolean loadEmbeddedFrame = false;
    boolean allowPopupWindow = false;
    String url = null;
    String encoding = null;
    MyNetworkListener listener = new MyNetworkListener(this);
    IDocument document = null;
    CrawlerSettings crawlerSettings;
    String proxyServer;
    int proxyPort = 80;
    String javascriptBody;
    final Boolean lock = false;
    int sec = 1000;
    long delay = 5 * sec;
    int httpStatus = 0;
    Map<String, String> header = new HashMap<String, String>(2);

    public BrowserWebCrawler() {
    }

    private void init() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(content());
        frame.setSize(1024, 600);
        frame.setVisible(true);
        frame.setTitle(url);
    }

    private void destroy() {
        if (null != browser) {
            BrowserFactory.destroyBrowser(browser);
        }
        if (null != frame) {
            frame.dispose();
        }
    }

    private JPanel content() {
        JPanel panel = new JPanel(new BorderLayout());
        //Core function to create browser
        browser = MyBrowserFactory.getInstance().getBrowser();
        browser.addNetworkListener(listener);
        browser.addPromptListener(new MyPromptListener());
//        browser.addBrowserListener(new MyBrowserListener(isLoadImage()));
        if (null != proxyServer) {
            browser.setProxyProtocol(new ProxySetting(ProxySetting.PROTOCOL_ALL, proxyServer, proxyPort));
            browser.enableProxy();
        }
        panel.add(BorderLayout.CENTER, browser.getComponent());
        return panel;
    }

    @Override
    public Document[] crawl(String url) {
        WebPage page = crawlWebPage(url);
        if (null != page) {
            return page.getDocs();
        } else {
            return null;
        }
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public void setProxy(String server, int port) {
        this.proxyPort = port;
        this.proxyServer = server;
    }

    @Override
    public void executeScript(String javascriptBody) {
        browser.executeScript(javascriptBody);
    }

    @Override
    public void setLoadImage(boolean yes) {
        this.loadImage = yes;
    }

    public boolean isLoadImage() {
        return loadImage;
    }

    @Override
    public void setLoadEmbeddedFrame(boolean yes) {
        loadEmbeddedFrame = yes;
    }

    public boolean isLoadEmbeddedFrame() {
        return loadEmbeddedFrame;
    }

    @Override
    public void stopLoad() {
        this.browser.stopLoad();
    }

    @Override
    public String[] getLinks() {
        IElementCollection links = document.getLinks();
        String[] ret = new String[links.length()];

        for (int i = 0; i < links.length(); i++) {
            IElement ele = links.item(i);
            String href = ele.getAttribute("href", 0);
            URL uri = UrlLinkUtil.getInstance().getURL(getUrl(), href);
            ret[i] = uri.toString();
        }

        return ret;
    }

    @Override
    public void setAllowPopupWindow(boolean yes) {
    }

    @Override
    public void setCrawlerSettings(CrawlerSettings setting) {
        this.crawlerSettings = setting;
        if (null == encoding || "".equals(encoding)) {
            encoding = crawlerSettings.getEncoding();
        }
    }

    @Override
    public int getHttpStatus() {
        return this.httpStatus;
    }

    public void setHttpStatus(int status) {
        this.httpStatus = status;
    }

    @Override
    public void setTimeout(int timeout) {
        this.delay = timeout;
    }

    @Override
    public void addHeader(String key, String value) {
        header.put(key, value);
    }

    @Override
    public void setCookie(String cookies) {
        header.put("Cookie", cookies);
    }

    @Override
    public WebPage crawlWebPage(String url) {
        l.debug("start crawler.");
        init();
        setUrl(url);
        long startTime = System.currentTimeMillis();
        browser.loadURL(getUrl());
        try {
            synchronized (lock) {
                l.debug("wait");
                lock.wait(delay);
            }
        } catch (InterruptedException ex) {
            l.error(ex.getMessage(), ex);
        }
        try {
            long endTime = System.currentTimeMillis();
            l.debug("after wait, elaspe: " + (endTime - startTime) + " ms");
            document = browser.getDocument();
            this.url = browser.getURL();
            IDocument frames[] = document.getChildFrames();
            Document[] docs = null;
            String[] sources = null;
            if (null != document) {
                int len = null != frames ? frames.length + 1 : 1;
                docs = new Document[len];
                sources = new String[len];
                docs[0] = (Document) browser.getW3CDocument();
                sources[0] = document.getDocumentSource();
                for (int i = 1; i < len; i++) {
                    IElement body = frames[i - 1].getBody();
                    sources[i] = frames[i - 1].getDocumentSource();
                    try {
                        if (null != body) {
                            docs[i] = body.convertToW3CNode().getOwnerDocument();
                        }
                    } catch (Exception ex) {
                        l.error(ex.getMessage(), ex);
                    }
                }
            }
            WebPage ret = new WebPage(this);
            ret.setDocs(docs);
            ret.setHtmlSource(sources);
            ret.setUrl(getUrl());
            l.debug("return, elaspe: " + (System.currentTimeMillis() - startTime) + " ms");
            return ret;
        } finally {
            destroy();
        }
    }
}
