package websiteschema.element;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 * 由IElement生成XPATH的时候，需要设定的参数。
 * @author ray
 */
public class XPathAttributes {

    private boolean usingPosition = false;
    private boolean usingClass = false;
    private boolean usingId = false;
    private String specifyAttr = null;

    public XPathAttributes() {
    }

    public XPathAttributes(boolean usingPosition, boolean usingClass, boolean usingId, String specifyAttr) {
        this.specifyAttr = specifyAttr;
        this.usingClass = usingClass;
        this.usingId = usingId;
        this.usingPosition = usingPosition;
    }

    public String getSpecifyAttr() {
        return specifyAttr;
    }

    public void setSpecifyAttr(String specifyAttr) {
        this.specifyAttr = specifyAttr;
    }

    public boolean isUsingClass() {
        return usingClass;
    }

    public void setUsingClass(boolean usingClass) {
        this.usingClass = usingClass;
    }

    public boolean isUsingId() {
        return usingId;
    }

    public void setUsingId(boolean usingId) {
        this.usingId = usingId;
    }

    public boolean isUsingPosition() {
        return usingPosition;
    }

    public void setUsingPosition(boolean usingPosition) {
        this.usingPosition = usingPosition;
    }
}
