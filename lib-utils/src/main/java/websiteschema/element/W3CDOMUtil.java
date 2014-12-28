/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.element;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author ray
 */
public class W3CDOMUtil {

    private static W3CDOMUtil ins = new W3CDOMUtil();

    public static W3CDOMUtil getInstance() {
        return ins;
    }

    public String getNodeText(Node node) {
        if (node != null) {
            StringBuilder ret = new StringBuilder();
            if (node.getNodeType() == Node.TEXT_NODE) {
                ret.append(node.getNodeValue());
            } else if (node.getNodeType() == Node.ELEMENT_NODE) {
                NodeList children = node.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    Node child = children.item(i);
                    if (Node.TEXT_NODE == child.getNodeType()) {
                        ret.append(child.getNodeValue());
                    }
                }
            }
            return ret.toString();
        }
        return null;
    }
    public void getNodeTextRecursive(Node node,StringBuilder content) {
        if (node != null) {
            if (node.getNodeType() == Node.TEXT_NODE) {
                content.append(node.getNodeValue());
            } else if (node.getNodeType() == Node.ELEMENT_NODE) {
                NodeList children = node.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    Node child = children.item(i);
                    if (Node.TEXT_NODE == child.getNodeType()) {
                        content.append(child.getNodeValue());
                    }else if (Node.ELEMENT_NODE == child.getNodeType()) {
                        getNodeTextRecursive(child,content);
                    }

                }
            }
        }
    }

}
