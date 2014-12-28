/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler;

import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Document;
import websiteschema.crawler.webpage.NextPageAnalyzer;

/**
 *
 * @author ray
 */
public class WebPage {

    String url;
    String[] htmlSource;
    Document[] docs;
    Crawler crawler;
    List<String> pages;
    Iterator<String> it;

    public WebPage(Crawler crawler) {
        this.crawler = crawler;
    }

    public Document[] getDocs() {
        return docs;
    }

    public void setDocs(Document[] docs) {
        this.docs = docs;
    }

    public String[] getHtmlSource() {
        return htmlSource;
    }

    public void setHtmlSource(String[] htmlSource) {
        this.htmlSource = htmlSource;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean hasNext() {
        if (null != it) {
            return it.hasNext();
        }
        analysisNextPages();
        if (null != it) {
            return it.hasNext();
        }
        return false;
    }

    public void analysisNextPages() {
        if (null != docs) {
            for (Document doc : docs) {
                NextPageAnalyzer analyzer = new NextPageAnalyzer(doc, url);
                analyzer.analysis();
                List<String> result = analyzer.getResults();
                if (null != result && !result.isEmpty()) {
                    this.pages = result;
                    this.it = pages.iterator();
                }
            }
        }
    }

    public WebPage getNext() {
        String link = it.next();
        WebPage next = crawler.crawlWebPage(link);
        return next;
    }
}
