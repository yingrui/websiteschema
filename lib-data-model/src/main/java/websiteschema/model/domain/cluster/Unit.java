/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain.cluster;

import java.util.Map;

/**
 *
 * @author ray
 */
public class Unit {

    private String xpath;
    private String text;
    private Map<String, String> attributes;

    public Unit() {
        xpath = null;
        text = null;
    }

    public Unit(String xpath, String text) {
        this.xpath = xpath;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getAttribute(String attr) {
        return null != attributes ? attributes.get(attr) : null;
    }
}
