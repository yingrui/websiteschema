/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.browser;

import com.webrenderer.swing.event.NetworkEvent;
import com.webrenderer.swing.event.NetworkListener;
import org.apache.log4j.Logger;

/**
 *
 * @author ray
 */
public class MyNetworkListener implements NetworkListener {

    Logger l = Logger.getLogger(MyNetworkListener.class);
    boolean loadImage = false;
    boolean loadEmbeddedFrame = false;
    BrowserWebCrawler crawler;
    int count = 0;

    public MyNetworkListener(BrowserWebCrawler crawler) {
        this.crawler = crawler;
    }

    @Override
    public void onProgressChange(NetworkEvent ne) {
        l.debug("onProgressChange" + ((float) ne.getCurrentProgress() / (float) ne.getMaximumProgress()));
    }

    @Override
    public void onDocumentLoad(NetworkEvent ne) {
        l.debug("onDocumentLoad ");
    }

    private void process() {
        synchronized (crawler.lock) {
            l.debug("notify");
            crawler.lock.notify();
        }
    }

    @Override
    public void onDocumentComplete(NetworkEvent ne) {
        l.debug("onDocumentComplete " + ne.getURL());
        crawler.setUrl(ne.getURL());
        process();
    }

    @Override
    public void onNetworkStatus(NetworkEvent ne) {
        l.debug("onNetworkStatus " + ne.getStatus());
    }

    @Override
    public void onNetworkError(NetworkEvent ne) {
        l.debug("onNetworkError " + ne.getFailure());
    }

    @Override
    public void onHTTPResponse(NetworkEvent ne) {
//        l.debug("onHTTPResponse\n" + ne.getResponseHeaders());
        l.debug("onHTTPResponse\n" + ne.getURL() + " : " + ne.getStatusText() + " : " + ne.getStatus());
        if (0 == count++) {
            crawler.setHttpStatus(ne.getStatus());
        }
    }

    @Override
    public void onHTTPInterceptHeaders(NetworkEvent ne) {
        l.debug("onHTTPInterceptHeaders " + ne.getURL());
//        l.trace("Send Request Header:\n" + ne.getMutableRequestHeaders());
        if (!crawler.header.isEmpty()) {
            for (String iter : crawler.header.keySet()) {
                ne.getMutableRequestHeaders().addHeader(iter, crawler.header.get(iter), true);
            }
        }
    }

    public void setLoadImage(boolean yes) {
        this.loadImage = yes;
    }

    public boolean isLoadImage() {
        return loadImage;
    }

    public void setLoadEmbeddedFrame(boolean yes) {
        loadEmbeddedFrame = yes;
    }

    public boolean isLoadEmbeddedFrame() {
        return loadEmbeddedFrame;
    }
}
