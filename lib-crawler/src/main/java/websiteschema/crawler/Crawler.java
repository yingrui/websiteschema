/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler;

import org.w3c.dom.Document;
import websiteschema.model.domain.cralwer.CrawlerSettings;

/**
 *
 * @author ray
 */
public interface Crawler {

    public final static String defaultEncoding = "gbk";

    public Document[] crawl(String url);

    public WebPage crawlWebPage(String url);

    public void stopLoad();

    public String getUrl();

    public int getHttpStatus();

    public String[] getLinks();

    public void setEncoding(String encoding);

    public void setProxy(String server, int port);

    public void executeScript(String javascriptBody);

    public void setLoadImage(boolean yes);

    public void setLoadEmbeddedFrame(boolean yes);

    public void setAllowPopupWindow(boolean yes);

    public void setCrawlerSettings(CrawlerSettings setting);

    public void setTimeout(int timeout);

    public void addHeader(String key, String value);

    public void setCookie(String cookies);
}
