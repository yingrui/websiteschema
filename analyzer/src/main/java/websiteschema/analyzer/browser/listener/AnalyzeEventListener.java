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
    IMozillaBrowserCanvas browser;
    String analyzerTips = BrowserContext.getConfigure().getProperty("AnalyzerTips");
    String urlAnalyzerTips = BrowserContext.getConfigure().getProperty("URLAnalyzerTips");
    SimpleBrowser simpleBrowser;

    public AnalyzeEventListener(SimpleBrowser simpleBrowser) {
        this.simpleBrowser = simpleBrowser;
    }

    public void onClick(JSObject event) {
        String siteId = ((JSObject)event.getMember("data")).getMember("siteId").toString();
        String url = ((JSObject)event.getMember("data")).getMember("url").toString();
        simpleBrowser.setFocusTab(1);
        System.out.println(siteId + " -> " + url);
        simpleBrowser.startAnalysis(siteId, url);


//        String nodeType = ElementUtil.getInstance().getNodeType(ele);
//        l.trace("node type: " + nodeType + " node name: " + ele.getTagName());
//        String[] attrs = ele.getAttributes();
//        boolean containAnalyzerTips = false;
//        boolean containUrlAnalyzerTips = false;
//        if (null != attrs) {
//            for (String attr : attrs) {
//                l.trace(attr);
//                if (attr.contains(analyzerTips)) {
//                    containAnalyzerTips = true;
//                } else if (attr.contains(urlAnalyzerTips)) {
//                    containUrlAnalyzerTips = true;
//                }
//            }
//            if (containAnalyzerTips) {
//                // receive analyze command.
//                IElement tr = ele.getParentElement().getParentElement().getParentElement();
//                IElementCollection children = tr.getChildElements();
//                String siteId = ElementUtil.getInstance().getText(children.item(2));
//                String url = ElementUtil.getInstance().getText(children.item(7));
//                l.info("Starting analysis site: " + siteId);
//                simpleBrowser.startAnalysis(siteId, url);
//                simpleBrowser.setFocusTab(1);
//            } else if (containUrlAnalyzerTips) {
//                // receive analyze command.
//                IElement tr = ele.getParentElement().getParentElement().getParentElement();
//                IElementCollection children = tr.getChildElements();
//                String siteId = ElementUtil.getInstance().getText(children.item(3));
//                String url = ElementUtil.getInstance().getText(children.item(2));
//                l.info("Starting analysis url: " + url);
//                simpleBrowser.startAnalysis(siteId, url);
//                simpleBrowser.setFocusTab(1);
//
//            }
//        }
    }
}
