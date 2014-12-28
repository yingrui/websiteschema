/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.htmlunit;

import com.gargoylesoftware.htmlunit.BinaryPage;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.JavaScriptPage;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.XHtmlPage;
import com.gargoylesoftware.htmlunit.util.WebConnectionWrapper;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import websiteschema.crawler.Crawler;
import websiteschema.crawler.WebPage;
import websiteschema.model.domain.cralwer.CrawlerSettings;
import websiteschema.utils.StringUtil;

/**
 *
 * @author ray
 */
public class HtmlUnitWebCrawler implements Crawler {

    boolean loadImage = false;
    boolean loadEmbeddedFrame = false;
    boolean allowPopupWindow = false;
    boolean javaScriptEnabled = true;
    String url = null;
    String encoding = null;
    CrawlerSettings crawlerSettings;
    int sec = 1000;
    int delay = 10 * sec;
    int httpStatus = 0;
    int requestNumber = 0;
    int maxRequestNumber = 1;
    Map<String, String> header = new HashMap<String, String>(2);
    Logger l = Logger.getLogger(HtmlUnitWebCrawler.class);

    private WebClient getWebClient() {
        final WebClient webClient = new WebClient();
        webClient.setJavaScriptEnabled(isJavaScriptEnabled());
        webClient.setCssEnabled(false);
        webClient.setTimeout(delay);
        webClient.setPopupBlockerEnabled(allowPopupWindow);
        webClient.setRedirectEnabled(true);
        webClient.setThrowExceptionOnScriptError(false);
        webClient.setThrowExceptionOnFailingStatusCode(false);
        if (!header.isEmpty()) {
            for (String iter : header.keySet()) {
                webClient.addRequestHeader(iter, header.get(iter));
            }
        }

        new WebConnectionWrapper(webClient) {

            @Override
            public WebResponse getResponse(WebRequest request) throws IOException {
                System.err.println("__________" + request.getUrl());
                ++requestNumber;
//                if (requestNumber > maxRequestNumber) {
//                    request.setUrl(...);
//                }
                WebResponse response = super.getResponse(request);
                if (response.getContentType().contains("text")) {
                    String charset = response.getContentCharset();
                    charset = null != charset ? charset : "";
                    if (StringUtil.isNotEmpty(encoding)) {
                        if (!charset.equalsIgnoreCase(encoding)) {
                            String html = response.getContentAsString(encoding);
                            response = new StringWebResponse(html, encoding, request.getUrl());
                        }
                    } else if (charset.equalsIgnoreCase("ISO-8859-1") || !StringUtil.isNotEmpty(charset)) {
                        String html = response.getContentAsString(defaultEncoding);
                        response = new StringWebResponse(html, defaultEncoding, request.getUrl());
                    }
                }
                return response;
            }
        };

        return webClient;
    }

    private WebPage getWebPage(final Page page) throws ParserConfigurationException {
        WebPage ret = new WebPage(this);
        ret.setUrl(url);
        if (page instanceof HtmlPage) {
            HtmlPage htmlPage = (HtmlPage) page;
            final List<FrameWindow> window = htmlPage.getFrames();
            if (null != window && window.size() > 0) {
                Document[] docs = new Document[window.size() + 1];
                String[] sources = new String[window.size() + 1];
                docs[0] = (HtmlPage) page;
                sources[0] = page.getWebResponse().getContentAsString();
                for (int i = 0; i < window.size(); i++) {
                    docs[i + 1] = (HtmlPage) window.get(i).getEnclosedPage();
                    sources[i + 1] = window.get(i).getEnclosedPage().getWebResponse().getContentAsString();
                }
                ret.setDocs(docs);
                ret.setHtmlSource(sources);
                return ret;
            } else {
                ret.setDocs(new Document[]{(HtmlPage) page});
                ret.setHtmlSource(new String[]{page.getWebResponse().getContentAsString()});
                return ret;
            }
        } else if (page instanceof SgmlPage) {
            ret.setDocs(new Document[]{(SgmlPage) page});
            ret.setHtmlSource(new String[]{page.getWebResponse().getContentAsString()});
            return ret;
        } else if (page instanceof XmlPage) {
            ret.setDocs(new Document[]{(XmlPage) page});
            ret.setHtmlSource(new String[]{page.getWebResponse().getContentAsString()});
            return ret;
        } else if (page instanceof XHtmlPage) {
            XHtmlPage htmlPage = (XHtmlPage) page;
            final List<FrameWindow> window = htmlPage.getFrames();
            if (null != window && window.size() > 0) {
                Document[] docs = new Document[window.size() + 1];
                String[] sources = new String[window.size() + 1];
                docs[0] = (XHtmlPage) page;
                sources[0] = page.getWebResponse().getContentAsString();
                for (int i = 0; i < window.size(); i++) {
                    docs[i + 1] = (HtmlPage) window.get(i).getEnclosedPage();
                    sources[i + 1] = window.get(i).getEnclosedPage().getWebResponse().getContentAsString();
                }
                ret.setDocs(docs);
                ret.setHtmlSource(sources);
                return ret;
            } else {
                ret.setDocs(new Document[]{(HtmlPage) page});
                ret.setHtmlSource(new String[]{page.getWebResponse().getContentAsString()});
                return ret;
            }
        } else if (page instanceof TextPage) {
            TextPage text = (TextPage) page;
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true); // never forget this!
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element html = doc.createElement("HTML");
            Element body = doc.createElement("BODY");
            body.setTextContent(text.getContent());
            html.appendChild(body);
            doc.appendChild(html);
            ret.setDocs(new Document[]{doc});
            ret.setHtmlSource(new String[]{page.getWebResponse().getContentAsString()});
            return ret;
        } else if (page instanceof JavaScriptPage) {
            return null;
        } else if (page instanceof UnexpectedPage) {
            return null;
        } else if (page instanceof BinaryPage) {
            return null;
        } else {
            return null;
        }
    }

    @Override
    public Document[] crawl(String url) {
        WebPage page = crawlWebPage(url);
        if (null != page) {
            return page.getDocs();
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

    public boolean isJavaScriptEnabled() {
        return javaScriptEnabled;
    }

    public void setJavaScriptEnabled(boolean javaScriptEnabled) {
        this.javaScriptEnabled = javaScriptEnabled;
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
        final WebClient webClient = getWebClient();
        try {
            URL dest = new URL(url);
            final Page page = webClient.getPage(dest);
            httpStatus = page.getWebResponse().getStatusCode();
            this.url = page.getUrl().toString();
            return getWebPage(page);
        } catch (IOException ex) {
            l.error(ex);
        } catch (FailingHttpStatusCodeException ex) {
            l.error(ex);
            httpStatus = ex.getStatusCode();
        } catch (ParserConfigurationException ex) {
            l.error(ex);
        } finally {
            webClient.closeAllWindows();
        }
        return null;
    }
}
