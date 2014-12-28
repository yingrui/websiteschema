/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler;

import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import websiteschema.model.domain.cralwer.CrawlerSettings;
import websiteschema.utils.JsoupUtil;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import websiteschema.utils.StringUtil;

/**
 *
 * @author ray
 */
public class SimpleHttpCrawler implements Crawler {

    boolean loadImage = false;
    boolean loadEmbeddedFrame = false;
    boolean allowPopupWindow = false;
    String url = null;
    String encoding = null;
    CrawlerSettings crawlerSettings;
    int sec = 1000;
    int delay = 60 * sec;
    int httpStatus = 0;
    Map<String, String> header = new HashMap<String, String>(2);
    Logger l = Logger.getLogger(SimpleHttpCrawler.class);

    @Override
    public Document[] crawl(String url_str) {
        WebPage page = crawlWebPage(url_str);
        if (null != page) {
            return page.getDocs();
        }
        return null;
    }

    @Override
    public WebPage crawlWebPage(String url_str) {
        try {
            url = url_str;
            Connection conn = org.jsoup.Jsoup.connect(url_str);
            conn.followRedirects(true);
            conn.request().timeout(delay);
            if (!header.isEmpty()) {
                for (String iter : header.keySet()) {
                    conn.request().header(iter, header.get(iter));
                }
            }

            org.jsoup.nodes.Document doc = null;
            String html = null;
            if (StringUtil.isNotEmpty(encoding)) {
                byte datas[] = conn.execute().bodyAsBytes();
                html = new String(datas, encoding);
                doc = JsoupUtil.getInstance().parse(html);
            } else {
                Response res = conn.execute();
                String charset = null != res.charset() ? res.charset() : "";
                if (charset.equalsIgnoreCase("ISO-8859-1")) {
                    byte datas[] = res.bodyAsBytes();
                    html = new String(datas, defaultEncoding);
                    doc = JsoupUtil.getInstance().parse(html);
                } else {
                    html = res.body();
                    doc = res.parse();
                }
            }
            String title = doc.title();
            System.out.println(title);
            org.w3c.dom.Document document = JsoupUtil.getInstance().convert(doc);
            httpStatus = conn.response().statusCode();
            url = conn.response().url().toString();
            //System.err.println(html.getTextContent());
            WebPage ret = new WebPage(this);
            ret.setDocs(new Document[]{document});
            ret.setHtmlSource(new String[]{html});
            ret.setUrl(url);
            return ret;
        } catch (Exception ex) {
            l.error(ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public void stopLoad() {
        l.debug("getLinks: Not supported yet.");
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String[] getLinks() {
        l.debug("getLinks: Not supported yet.");
        return null;
    }

    @Override
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public void setProxy(String server, int port) {
        l.debug("setProxy: Not supported yet.");
    }

    @Override
    public void executeScript(String javascriptBody) {
        l.debug("executeScript: Not supported yet.");
    }

    @Override
    public void setLoadImage(boolean yes) {
        this.loadImage = yes;
    }

    @Override
    public void setLoadEmbeddedFrame(boolean yes) {
        loadEmbeddedFrame = yes;
    }

    @Override
    public void setAllowPopupWindow(boolean yes) {
        l.debug("setAllowPopupWindow: Not supported yet.");
    }

    @Override
    public void setCrawlerSettings(CrawlerSettings setting) {
        this.crawlerSettings = setting;
        if (null == encoding || "".equals(encoding)) {
            encoding = crawlerSettings.getEncoding();
        }
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
}
