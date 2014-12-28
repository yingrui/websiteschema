/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.analyzer.browser.listener;

import com.sun.webkit.dom.HTMLElementImpl;
import netscape.javascript.JSObject;
import org.apache.log4j.Logger;
import org.w3c.dom.css.CSSStyleDeclaration;
import websiteschema.analyzer.browser.SimpleBrowser;
import websiteschema.analyzer.context.BrowserContext;
import websiteschema.element.Rectangle;
import websiteschema.element.XPathAttributes;
import websiteschema.element.factory.RectangleFactory;
import websiteschema.element.factory.XPathAttrFactory;
import websiteschema.utils.ElementUtil;
import websiteschema.utils.UrlLinkUtil;

import java.net.URL;

/**
 *
 * @author ray
 */
public class SimpleMouseListener {

    XPathAttributes attr = new XPathAttributes();
    Logger l = Logger.getRootLogger();
    BrowserContext context;
    SimpleBrowser simpleBrowser = null;

    HTMLElementImpl lastClickedElement = null;

    public SimpleMouseListener(BrowserContext context, SimpleBrowser simpleBrowser) {
        attr.setUsingClass(true);
        attr.setUsingId(true);
        attr.setSpecifyAttr("name");
        attr.setUsingPosition(true);
        this.context = context;
        this.simpleBrowser = simpleBrowser;
    }


    private void clearLastClickedElementBorder() {
        if (lastClickedElement != null) {
            lastClickedElement.setAttribute("style", "");
        }
    }

    public void onClick(JSObject event) {
        clearLastClickedElementBorder();
        HTMLElementImpl element = (HTMLElementImpl) event.getMember("toElement");
        element.setAttribute("style", "border: 1px solid red;");
        lastClickedElement = element;

        {
            {
                //计算所选节点的两种XPATH
                Rectangle rect = new RectangleFactory().create(element);
                String xpath = XPathAttrFactory.getInstance().create(element);
                attr = simpleBrowser.getXPathAttr();
                String xpath2 = XPathAttrFactory.getInstance().create(element, attr);
                simpleBrowser.displaySelectedElement(xpath, xpath2); //将XPATH显示在UI上

                String text = element.getInnerHTML();
                simpleBrowser.displayNodeValue(text);//将所选节点的html标签显示在UI上

                l.debug("Elememnt Type: " + element.getTagName());
                if ("A".equals(element.getTagName())) {
                    //如果所选节点是A，则将URL显示在UI上
                    String href = element.getAttribute("href");
                    URL url = UrlLinkUtil.getInstance().getURL(context.getBrowser().getURL(), href);
                    if (null != url) {
                        simpleBrowser.displaySelectedAnchor(url.toString());
                    }
                }
                context.getConsole().log("Tag Name: " + element.getTagName() + " -- Node Type: " + ElementUtil.getInstance().getNodeType(element.getNodeType()) + " -- XPath: " + xpath);
                l.debug(rect);
                CSSStyleDeclaration style = element.getStyle();
                l.debug("CSS Properties: " + style);

//                VipsBlockExtractor be = new VipsBlockExtractor();
//                be.setContext(context);
//                String referrer2 = context.getBrowser().getDocument().getReferrer();
//                be.setReferrer(referrer2);
//                Rectangle rectBody = new RectangleFactory().create(context.getBrowser().getDocument().getBody());
//                be.setPageSize(rectBody.getHeight() * rectBody.getWidth());
//                be.analysisElement(ele);
            }
        }
    }

}
