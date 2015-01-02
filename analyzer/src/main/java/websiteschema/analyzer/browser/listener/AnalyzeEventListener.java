/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.analyzer.browser.listener;

import com.sun.webkit.dom.HTMLElementImpl;
import com.webrenderer.swing.IMozillaBrowserCanvas;
import com.webrenderer.swing.dom.IElement;
import com.webrenderer.swing.dom.IElementCollection;
import com.webrenderer.swing.event.MouseEvent;
import com.webrenderer.swing.event.MouseListener;
import netscape.javascript.JSObject;
import org.apache.log4j.Logger;
import websiteschema.analyzer.browser.SimpleBrowser;
import websiteschema.analyzer.context.BrowserContext;
import websiteschema.utils.ElementUtil;

/**
 *
 * @author ray
 */
public class AnalyzeEventListener {

    Logger l = Logger.getRootLogger();
    SimpleBrowser simpleBrowser;

    public AnalyzeEventListener(SimpleBrowser simpleBrowser) {
        this.simpleBrowser = simpleBrowser;
    }

    public void onClick(JSObject event) {
        String siteId = ((JSObject)event.getMember("data")).getMember("siteId").toString();
        String url = ((JSObject)event.getMember("data")).getMember("url").toString();

        if("undefined".equals(url)) {
            url = ((JSObject)event.getMember("data")).getMember("startURL").toString();
        }

        simpleBrowser.setFocusTab(1);
        l.debug(siteId + " -> " + url);
        simpleBrowser.startAnalysis(siteId, url);
    }
}
