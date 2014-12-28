/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.element.factory;

//import com.sun.webkit.dom.HTMLDocumentImpl;
import com.webrenderer.swing.dom.IDocument;
import com.webrenderer.swing.dom.IElement;
import com.webrenderer.swing.dom.IStyleRule;
import com.webrenderer.swing.dom.IStyleSheet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.css.CSSStyleDeclaration;
import websiteschema.element.CSSProperties;
import websiteschema.element.StyleSheet;

/**
 *
 * @author ray
 */
public class StyleSheetFactory {

//    public StyleSheet createStyleSheet(HTMLDocumentImpl doc) {
//        StyleSheet ret = new StyleSheet();
//
////        IStyleSheet[] styleSheets = doc.getStyleSheets();
////        for (IStyleSheet styleSheet : styleSheets) {
////            int len = styleSheet.rulesLength();
////            for (int i = 0; i < len; i++) {
////                IStyleRule rule = styleSheet.getRule(i);
////                if (null != rule) {
//////                    System.out.println(rule.getSelectorText() + " -- " + rule.getCSSText());
////                    ret.put(rule.getSelectorText(), rule.getCSSText());
////                }
////            }
////        }
//
//        return ret;
//    }
//
//    public void createStyleSheet(IDocument doc, StyleSheet styleSheet) {
//        IStyleSheet[] styleSheets = doc.getStyleSheets();
//        for (IStyleSheet style : styleSheets) {
//            int len = style.rulesLength();
//            for (int i = 0; i < len; i++) {
//                IStyleRule rule = style.getRule(i);
//                if (null != rule) {
////                    System.out.println(rule.getSelectorText() + " -- " + rule.getCSSText());
//                    styleSheet.put(rule.getSelectorText(), rule.getCSSText());
//                }
//            }
//        }
//    }
//
//    /**
//     * 不能获取全部的CSS信息，暂时只取明确定位在某一特定tag、class和id上的CSS属性，且不包括继承到的。
//     * @param styleSheet
//     * @param ele
//     * @return
//     */
//    public CSSProperties createCSSProperties(StyleSheet styleSheet, IElement ele) {
//        CSSProperties ret = new CSSProperties();
//
//        if (null != styleSheet) {
//            String[] selectors = getSelector(ele);
//
//            for (String selector : selectors) {
//                Map<String, String> properties = styleSheet.get(selector);
//                if (null != properties) {
//                    for (String key : properties.keySet()) {
//                        ret.put(key, properties.get(key));
//                    }
//                }
//            }
//        }
//
//        return ret;
//    }
//
//    public CSSProperties createCSSProperties(CSSStyleDeclaration styleSheet) {
//        CSSProperties ret = new CSSProperties();
//
//        if (null != styleSheet) {
//
//            for (int i = 0; i < styleSheet.getLength(); i++) {
//                String item = styleSheet.item(i);
//                ret.put(item, styleSheet.getPropertyValue(item));
//            }
//        }
//
//        return ret;
//    }
//
//    private String[] getSelector(IElement ele) {
//        String id = ele.getId();
//        String className = ele.getClassName();
//        String tagName = ele.getTagName();
//        String pTagName = null != ele.getParentElement() ? ele.getParentElement().getTagName() : null;
//        List<String> list = new ArrayList<String>();
//        list.add(tagName);
//        if (null != id && !"".equals(id)) {
//            list.add("#" + id);
//            list.add(tagName + "#" + id);
//        }
//        if (null != className && !"".equals(className)) {
//            list.add("." + className);
//            list.add(tagName + "." + className);
//        }
//
//        return list.toArray(new String[0]);
//    }
}
