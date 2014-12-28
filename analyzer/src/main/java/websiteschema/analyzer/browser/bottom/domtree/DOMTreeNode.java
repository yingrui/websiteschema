/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.analyzer.browser.bottom.domtree;

import com.webrenderer.swing.dom.IElement;
import com.webrenderer.swing.dom.IElementCollection;
import java.util.ArrayList;
import java.util.List;
import websiteschema.element.W3CDOMUtil;
import org.w3c.dom.Node;
import websiteschema.utils.ElementUtil;
import websiteschema.utils.StringUtil;

/**
 *
 * @author ray
 */
public class DOMTreeNode {

    IElement ele = null;
    DOMTreeNode parent;
    List<DOMTreeNode> children;

    public DOMTreeNode(IElement ele) {
        this.ele = ele;
        if (null != ele) {
            IElementCollection childElements = ele.getChildElements();
            if (null != childElements) {
                children = new ArrayList<DOMTreeNode>();
                for (int i = 0; i < childElements.length(); i++) {
                    IElement e = childElements.item(i);
                    if ("ELEMENT_NODE".equals(ElementUtil.getInstance().getNodeType(e))) {
                        DOMTreeNode node = new DOMTreeNode(e);
                        node.setParent(this);
                        children.add(node);
                    }
                }
            }
        }
    }

    public IElement getEle() {
        return ele;
    }

    public int getChildLength() {
        if (null != children) {
            return children.size();
        } else {
            return 0;
        }
    }

    public DOMTreeNode getChildAt(int index) {
        if (index < children.size()) {
            return children.get(index);
        }
        return null;
    }

    public List<DOMTreeNode> getChildren() {

        return children;
    }

    public DOMTreeNode getParent() {
        return parent;
    }

    public void setParent(DOMTreeNode parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DOMTreeNode) {
            DOMTreeNode other = (DOMTreeNode) obj;
            return ele.equals(other.ele);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ele.hashCode();
    }

    @Override
    public String toString() {
        String tagName = ele.getTagName();
        String[] attrs = ele.getAttributes();
        StringBuilder attr = new StringBuilder();
        if (null != attrs && attrs.length > 0) {
            for (String key : attrs) {
                if (StringUtil.isNotEmpty(key.trim())) {
                    attr.append(" ").append(key);
                }
            }
        }
        if (getChildLength() == 0) {
            String text = ElementUtil.getInstance().getText(ele);
            return "<" + tagName + attr.toString() + ">" + text + "</" + tagName + ">";
        } else {
            return "<" + tagName + attr.toString() + ">";
        }
    }
}
