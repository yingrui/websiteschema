/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.utils;

import com.sun.webkit.dom.ElementImpl;
import com.sun.webkit.dom.TextImpl;
import com.webrenderer.swing.dom.IDocument;
import com.webrenderer.swing.dom.IElement;
import com.webrenderer.swing.dom.IElementCollection;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import websiteschema.element.Rectangle;
import websiteschema.element.factory.RectangleFactory;

/**
 *
 * @author ray
 */
public class ElementUtil {

    private final static ElementUtil instance = new ElementUtil();

    public static ElementUtil getInstance() {
        return instance;
    }

    public IElement getHead(IDocument doc) {
        if (null != doc) {
            IElement html = null != doc.getBody() ? doc.getBody().getParentElement() : null;
            if (null != html) {
                IElementCollection children = html.getChildElements();
                for(int i = 0; i < children.length(); i++) {
                    IElement head = children.item(i);
                    if("head".equalsIgnoreCase(head.getTagName())) {
                        return head;
                    }
                }
            }
        }
        return null;
    }

    public void drawRectangleInPage(IElement ele) {
        String lastStyle = ele.getAttribute("style", 0);
        String additionStyle = "border-style: solid; border-width: 3px;";
        if (null != lastStyle && !"".equals(lastStyle)) {
            ele.setAttribute("style", lastStyle + ";" + additionStyle, 0);
        } else {
            ele.setAttribute("style", additionStyle, 0);
        }
    }

    public void drawRectangleInPage(ElementImpl ele, String color) {
        String lastStyle = ele.getAttribute("style");
        String additionStyle = "border-style: solid; border-width: 3px;";
        if(null != color && !"".equals(color)) {
            additionStyle = "border-style: solid; border-width: 3px; border-color: " + color +";";
        }
        if (null != lastStyle && !"".equals(lastStyle)) {
            ele.setAttribute("style", lastStyle + ";" + additionStyle);
        } else {
            ele.setAttribute("style", additionStyle);
        }
    }


    public double getPageSize(IDocument doc) {
        if (null != doc) {
            Rectangle rect = RectangleFactory.getInstance().create(doc.getBody());
            double pageSize = rect.getHeight() * rect.getWidth();
            return pageSize;
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }

    public String getText(ElementImpl ele) {
        StringBuilder sb = new StringBuilder();

        NodeList children = ele.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof TextImpl) {
                sb.append(((TextImpl)child).getWholeText());
            } else {
                sb.append(getText((ElementImpl)child));
            }
        }

        return sb.toString();
    }

    public String getNodeType(IElement ele) {
        Node node = ele.convertToW3CNode();
        int type = node.getNodeType();
        return getNodeType(type);
    }

    public String getNodeType(int type) {
        String ret = null;
        switch (type) {
            case Node.ATTRIBUTE_NODE:
                ret = "ATTRIBUTE_NODE";
                break;
            case Node.CDATA_SECTION_NODE:
                ret = "CDATA_SECTION_NODE";
                break;
            case Node.COMMENT_NODE:
                ret = "COMMENT_NODE";
                break;
            case Node.DOCUMENT_FRAGMENT_NODE:
                ret = "DOCUMENT_FRAGMENT_NODE";
                break;
            case Node.DOCUMENT_NODE:
                ret = "DOCUMENT_NODE";
                break;
            case Node.DOCUMENT_TYPE_NODE:
                ret = "DOCUMENT_TYPE_NODE";
                break;
            case Node.ELEMENT_NODE:
                ret = "ELEMENT_NODE";
                break;
            case Node.ENTITY_NODE:
                ret = "ENTITY_NODE";
                break;
            case Node.ENTITY_REFERENCE_NODE:
                ret = "ENTITY_REFERENCE_NODE";
                break;
            case Node.NOTATION_NODE:
                ret = "NOTATION_NODE";
                break;
            case Node.PROCESSING_INSTRUCTION_NODE:
                ret = "PROCESSING_INSTRUCTION_NODE";
                break;
            case Node.TEXT_NODE:
                ret = "TEXT_NODE";
                break;
        }
        return ret;
    }
}
