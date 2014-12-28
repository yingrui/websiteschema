/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import websiteschema.element.XPathAttributes;
import websiteschema.element.factory.XPathAttrFactory;
import websiteschema.model.domain.cluster.DocUnits;
import websiteschema.model.domain.cluster.Unit;

/**
 *
 * @author ray
 */
public class DocumentConvertor {

    XPathAttributes xpathAttr;

    public XPathAttributes getXpathAttr() {
        return xpathAttr;
    }

    public void setXpathAttr(XPathAttributes xpathAttr) {
        this.xpathAttr = xpathAttr;
    }

    public DocUnits convertDocument(Document document) {
        List<Unit> units = new ArrayList<Unit>();

//        traversal(document.getDocumentElement(), units);

        traversal(document.getDocumentElement(), units, null);

        DocUnits ret = new DocUnits();
        ret.setUnits(units.toArray(new Unit[0]));
        return ret;
    }

    private boolean ignore(String tagName) {
        boolean ret = false;
        if ("script".equalsIgnoreCase(tagName) || "style".equals(tagName)) {
            ret = true;
        }
        return ret;
    }

    private void traversal(Element ele, List<Unit> units, String parentXPath) {
        String tagName = ele.getTagName();
        if (!ignore(tagName)) {
            String xpath = XPathAttrFactory.getInstance().create(ele, xpathAttr, parentXPath);
            StringBuilder text = new StringBuilder();
            NodeList children = ele.getChildNodes();
            if (null != children) {
                for (int i = 0; i < children.getLength(); i++) {
                    Node child = children.item(i);
                    if (Node.TEXT_NODE == child.getNodeType()) {
                        text.append(child.getNodeValue());
                    } else if (Node.ELEMENT_NODE == child.getNodeType()) {
                        traversal((Element) child, units, xpath);
                    }
                }
            }

            Unit unit = new Unit(xpath, text.toString());
            //获取节点的其他属性
            if ("A".equalsIgnoreCase(tagName)) {
                Map<String, String> attrs = getNodeAttributes(ele);
                unit.setAttributes(attrs);
            }
            units.add(unit);
        }
    }

    private void traversal(Element ele, List<Unit> units) {
        String tagName = ele.getTagName();
        if (!ignore(tagName)) {
            String xpath = XPathAttrFactory.getInstance().create(ele, xpathAttr);
            StringBuilder text = new StringBuilder();
            NodeList children = ele.getChildNodes();
            if (null != children) {
                for (int i = 0; i < children.getLength(); i++) {
                    Node child = children.item(i);
                    if (Node.TEXT_NODE == child.getNodeType()) {
                        text.append(child.getNodeValue());
                    } else if (Node.ELEMENT_NODE == child.getNodeType()) {
                        traversal((Element) child, units);
                    }
                }
            }

            Unit unit = new Unit(xpath, text.toString());
            //获取节点的其他属性
            if ("A".equalsIgnoreCase(tagName)) {
                Map<String, String> attrs = getNodeAttributes(ele);
                unit.setAttributes(attrs);
            }
            units.add(unit);
        }
    }

    private Map<String, String> getNodeAttributes(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        if (null != attributes && attributes.getLength() > 0) {
            Map<String, String> attrs = new HashMap<String, String>(attributes.getLength());
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attr = attributes.item(i);
                String name = attr.getNodeName();
                String value = attr.getNodeValue();
                attrs.put(name, value);
            }
            return attrs;
        }
        return null;
    }
}
