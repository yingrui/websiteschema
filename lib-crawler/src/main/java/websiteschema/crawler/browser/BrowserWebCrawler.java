/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.browser;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import websiteschema.crawler.Crawler;
import websiteschema.crawler.WebPage;
import websiteschema.model.domain.cralwer.CrawlerSettings;
import websiteschema.utils.UrlLinkUtil;

/**
 * @author ray
 */
public class BrowserWebCrawler implements Crawler {

    Logger l = Logger.getLogger(BrowserWebCrawler.class);

    InnerBrowser browser;

    boolean loadImage = false;
    boolean loadEmbeddedFrame = false;
    boolean allowPopupWindow = false;
    String url = null;
    String encoding = null;
    Document document = null;
    CrawlerSettings crawlerSettings;
    String proxyServer;
    int proxyPort = 80;
    String javascriptBody;
    final Boolean lock = false;
    final Boolean browserLock = false;
    int sec = 1000;
    long delay = 50 * sec;
    int httpStatus = 0;
    Map<String, String> header = new HashMap<String, String>(2);

    public BrowserWebCrawler() {
        init();
    }

    private void init() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                browser = new InnerBrowser();
                browser.createScene();
                synchronized (browserLock) {
                    browserLock.notify();
                }
            }
        });
    }

    private void load() {
        synchronized (browserLock) {
            if(browser == null) {
                try {
                    browserLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        browser.load(getUrl());
    }

    private void destroy() {
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
        browser.engine.executeScript(javascriptBody);
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
    }

    @Override
    public String[] getLinks() {
        NodeList links = document.getElementsByTagName("a");
        List<String> ret = new ArrayList<String>();

        for (int i = 0; i < links.getLength(); i++) {
            Node ele = links.item(i);
            Node attrHref = ele.getAttributes().getNamedItem("href");
            String href = attrHref != null ? attrHref.getNodeValue() : null;
            if (href != null) {
                URL uri = UrlLinkUtil.getInstance().getURL(getUrl(), href);
                ret.add(uri.toString());
            }
        }

        return ret.toArray(new String[0]);
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
        setUrl(url);
        load();
        long startTime = System.currentTimeMillis();
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
            document = browser.engine.getDocument();
            this.url = browser.engine.getLocation();
//            org.w3c.dom.Element frames = document.getDocumentElement();
//            Document[] docs = null;
//            String[] sources = null;
//            if (null != document) {
//                int len = null != frames ? frames.length + 1 : 1;
//                docs = new Document[len];
//                sources = new String[len];
//                docs[0] = (Document) browser.getW3CDocument();
//                sources[0] = document.getDocumentSource();
//                for (int i = 1; i < len; i++) {
//                    IElement body = frames[i - 1].getBody();
//                    sources[i] = frames[i - 1].getDocumentSource();
//                    try {
//                        if (null != body) {
//                            docs[i] = body.convertToW3CNode().getOwnerDocument();
//                        }
//                    } catch (Exception ex) {
//                        l.error(ex.getMessage(), ex);
//                    }
//                }
//            }
            WebPage ret = new WebPage(this);
            ret.setDocs(new Document[]{document});
            ret.setHtmlSource(new String[]{document.getTextContent()});
            ret.setUrl(getUrl());
            l.debug("return, elaspe: " + (System.currentTimeMillis() - startTime) + " ms");
            return ret;
        } finally {
            destroy();
        }
    }

    final class InnerBrowser {

        private JFXPanel jfxPanel = new JFXPanel();
        public WebEngine engine;
        JTextField textfield;

        public InnerBrowser() {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(content());
            frame.setSize(750, 450);
            frame.setVisible(true);
        }

        public void load(final String url) {
            Platform.runLater(new Runnable() {
                public void run() {
                    engine.load(url);
                }
            });
        }

        public void createScene() {
            Platform.runLater(new Runnable() {
                public void run() {
                    WebView view = new WebView();
                    engine = view.getEngine();

                    engine.getLoadWorker().workDoneProperty().addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, final Number newValue) {
                            l.debug("Web page load progress " + newValue.intValue());
                            System.out.println("Web page load " + engine.getLocation() + " progress: " + newValue.intValue());
                            if(newValue.intValue() >= 95) {
                                l.debug("Web page loaded");
                                synchronized (lock) {
                                    lock.notify();
                                }
                            }
                        }
                    });

                    jfxPanel.setScene(new Scene(view));
                }
            });
        }

        public JPanel content() {
            JPanel panel = new JPanel(new BorderLayout());

            textfield = new JTextField();

            textfield.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    Platform.runLater(new Runnable() {
                        public void run(){
                            engine.load(textfield.getText());}
                    });
                }
            });

            panel.add(BorderLayout.NORTH, textfield);
            panel.add(BorderLayout.CENTER, jfxPanel);
            return panel;
        }

    }

//    public static void main(String args[]) throws InterruptedException {
//        String url = "http://www.baidu.com/";
//        BrowserWebCrawler crawler = new BrowserWebCrawler();
//        Document[] docs = crawler.crawl(url);
//        System.out.println("----" + System.currentTimeMillis());
//        crawler = null;
//    }
}


