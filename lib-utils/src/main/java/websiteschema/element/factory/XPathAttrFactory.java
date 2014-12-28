/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.element.factory;

import com.webrenderer.swing.dom.IElement;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import websiteschema.element.XPathAttributes;

/**
 * 根据指定的设置（XPathAttributes），生成指定元素的xpath。
 * @author ray
 */
public class XPathAttrFactory {

    private final static XPathAttributes attr = new XPathAttributes();

    static {
        attr.setUsingPosition(true);
    }
    private final static XPathAttrFactory instance = new XPathAttrFactory();

    public static XPathAttrFactory getInstance() {
        return instance;
    }

    public String create(IElement ele, XPathAttributes attr) {
        return getXPath(ele, attr);
    }

    public String create(Element ele, XPathAttributes attr) {
        return getXPath(ele, attr);
    }

    public String create(Node node, XPathAttributes attr) {
        return getXPath(node, attr);
    }

    public String create(Node node, XPathAttributes attr, String parentXPath) {
        return getXPath(node, attr, parentXPath);
    }

    public String create(Element ele, XPathAttributes attr, String parentXPath) {
        return getXPath(ele, attr, parentXPath);
    }

    public String create(IElement ele) {
        return getXPath(ele, attr);
    }

    public String create(Element ele) {
        return getXPath(ele, attr);
    }

    public String create(Node node) {
        return getXPath(node, attr);
    }

    private String getXPath(IElement ele, XPathAttributes attr) {
        Node node = ele.convertToW3CNode();
        return getXPath(node, attr);
    }

    private String getXPath(Node ele, XPathAttributes attr) {
        String xpath = "";
        if (isTextNode(ele)) {
            xpath = "text()";
        } else {
            xpath = getElementXPath(ele, attr);
        }
        Node pEle = ele.getParentNode();
        if (pEle != null && !isTextNode(pEle) && Node.ELEMENT_NODE == pEle.getNodeType()) {
            xpath = getXPath((Element) pEle, attr) + "/" + xpath;
        }
        return xpath;
    }

    private String getXPath(Node ele, XPathAttributes attr, String parentXPath) {
        String xpath = "";
        if (isTextNode(ele)) {
            xpath = "text()";
        } else {
            xpath = getElementXPath(ele, attr);
        }
        if (null != parentXPath && !"".equals(parentXPath)) {
            xpath = parentXPath + "/" + xpath;
        }
        return xpath;
    }

    private String getElementXPath(Node ele, XPathAttributes attr) {
        String xpath = "";
        String tagName = ele.getNodeName();
        if (null != tagName) {
            String ns = ele.getPrefix();
            if (null != ns) {
                xpath = ns + ":" + tagName.toLowerCase();
            } else {
                xpath = tagName.toLowerCase();
            }
            String attrKeyValues = "";
            //如果要用位置来表示XPATH上的节点，则就没有必要使用Id和Class了。
            if (attr.isUsingPosition()) {
                //按照元素的顺序生成XPath
                Node p = ele.getParentNode();
                Element parent = null != p && Node.ELEMENT_NODE == p.getNodeType() ? (Element) p : null;
                //parent != null
                if (null != parent) {
                    NodeList siblings = parent.getChildNodes();
                    if (1 < siblings.getLength()) {
                        int pos = 1;
                        int count = 0;
                        boolean found = false;
                        for (int i = 0; i < siblings.getLength(); i++) {
                            String siblingName = siblings.item(i).getNodeName();
                            if (ele.equals(siblings.item(i))) {
                                found = true;
                            }
                            if (tagName.equalsIgnoreCase(siblingName)) {
                                count++;
                                //如果找到了自己，则记下自己的位置
                                if (!found) {
                                    pos++;
                                }
                            }
                        }
                        //如果只有自己1个，则不需要使用格式：div[1]
                        if (found && count > 1) {
                            attrKeyValues = String.valueOf(pos);
                        }
                    }
                }
            } else {
                NamedNodeMap attrs = ele.getAttributes();
                if (attrs != null) {
                    if (attr.isUsingClass()) {
                        //将class属性增加到XPath中
                        Node n = attrs.getNamedItem("class");
                        String className = null != n ? n.getNodeValue() : null;
                        if (null != className && !"".equals(className)) {
                            attrKeyValues += " @class='" + className + "'";
                        }
                    }
                    if (attr.isUsingId()) {
                        //将id属性增加到XPath中
                        Node n = attrs.getNamedItem("id");
                        String id = null != n ? n.getNodeValue() : null;
                        if (null != id && !"".equals(id)) {
                            if (!"".equals(attrKeyValues)) {
                                attrKeyValues += " and @id='" + id + "'";
                            } else {
                                attrKeyValues += " @id='" + id + "'";
                            }
                        }
                    }
                    String specifyAttr = attr.getSpecifyAttr();
                    if (null != specifyAttr && !"".equals(specifyAttr)) {
                        //将指定的属性增加到XPath中
                        Node n = attrs.getNamedItem(specifyAttr);
                        String attrValue = null != n ? n.getNodeValue() : null;
                        if (null != attrValue && !"".equals(attrValue)) {
                            if (!"".equals(attrKeyValues)) {
                                attrKeyValues += " and @" + specifyAttr + "='" + attrValue + "'";
                            } else {
                                attrKeyValues += " @" + specifyAttr + "='" + attrValue + "'";
                            }
                        }
                    }
                }
            }
            if (!"".equals(attrKeyValues)) {
                xpath += "[" + attrKeyValues.trim() + "]";
            }
        }
        return xpath;
    }

    private boolean isTextNode(Node node) {
        return Node.TEXT_NODE == node.getNodeType();
    }
}
