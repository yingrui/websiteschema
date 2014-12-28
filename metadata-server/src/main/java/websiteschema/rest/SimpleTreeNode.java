/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.rest;

/**
 *
 * @author ray
 */
public class SimpleTreeNode {

    String text;
    String id;
    String title;
    String cls = "cls";
    String iconCls = "icon-cls";
    String href;
    boolean leaf = true;
    boolean singleClickExpand = true;
    boolean checked = false;
    SimpleTreeNode[] children = null;

    public SimpleTreeNode[] getChildren() {
        return children;
    }

    public void setChildren(SimpleTreeNode[] children) {
        this.children = children;
        if (null != children && children.length > 0) {
            leaf = false;
        }
    }

    public void addChild(SimpleTreeNode child) {
        if(children == null) {
            children = new SimpleTreeNode[1];
        } else {
            SimpleTreeNode[] tmp = new SimpleTreeNode[children.length + 1];
            System.arraycopy(children, 0, tmp, 0, children.length);
            children = tmp;
        }
        children[children.length - 1] = child;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public boolean isSingleClickExpand() {
        return singleClickExpand;
    }

    public void setSingleClickExpand(boolean singleClickExpand) {
        this.singleClickExpand = singleClickExpand;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
