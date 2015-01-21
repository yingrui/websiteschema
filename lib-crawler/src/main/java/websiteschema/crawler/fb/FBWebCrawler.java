/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import org.w3c.dom.Document;
import websiteschema.crawler.Crawler;
import websiteschema.crawler.WebPage;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.model.domain.Websiteschema;
import websiteschema.utils.StringUtil;

/**
 *
 * @author ray
 */
@EO(name = {"SUC", "FAL"})
@EI(name = {"FETCH:FETCH"})
public class FBWebCrawler extends FunctionBlock {

    Crawler crawler;
    @DI(name = "URL")
    public String url;
    @DI(name = "CRAWLER")
    public String crawlerType;
    @DI(name = "USERAGENT")
    public String userAgent;
    @DI(name = "SCHEMA")
    public Websiteschema schema;
    @DO(name = "DOC", relativeEvents = {"SUC"})
    public Document out;
    @DO(name = "STATUS", relativeEvents = {"SUC"})
    public int status;
    @DO(name = "DOCS", relativeEvents = {"SUC"})
    public Document[] docAndFrames;
    @DO(name = "PAGE", relativeEvents = {"SUC"})
    public WebPage page;
    @DO(name = "URL", relativeEvents = {"SUC"})
    public String do_url;

    @Algorithm(name = "FETCH")
    public void fetch() {
        try {
            docAndFrames = null;
            Crawler c = createCrawler();
            setConfig(c);
            page = c.crawlWebPage(url);
            if (null != page) {
                docAndFrames = page.getDocs();
                do_url = c.getUrl();
                status = c.getHttpStatus();
                if (null != docAndFrames && docAndFrames.length > 0) {
                    out = docAndFrames[0];
                    this.triggerEvent("SUC");
                    return;
                }
            }
        } catch (Exception ex) {
            l.error(ex.getMessage(), ex);
        }
        this.triggerEvent("FAL");
    }

    private void setConfig(Crawler crawler) {
        if (StringUtil.isNotEmpty(userAgent) && null != crawler) {
            crawler.addHeader("User-Agent", userAgent);
        }
    }

    private Crawler createCrawler() {
        if (null == crawler) {
            if (null == crawlerType) {
                crawlerType = "websiteschema.crawler.htmlunit.HtmlUnitWebCrawler";
            }
            try {
                Class clazz = Class.forName(crawlerType);
                crawler = (Crawler) clazz.newInstance();
                setCrawlerSettings();
            } catch (Exception ex) {
                l.error(ex.getMessage(), ex);
            }
        }
        return crawler;
    }

    private void setCrawlerSettings() {
        if(schema != null && schema.getCrawlerSettings() != null) {
            crawler.setCrawlerSettings(schema.getCrawlerSettings());
        }
    }
}
