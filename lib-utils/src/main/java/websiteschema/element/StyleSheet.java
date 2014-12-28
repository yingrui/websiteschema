package websiteschema.element;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author ray
 */
public class StyleSheet {

    Map<String, Map<String, String>> styles = new LinkedHashMap<String, Map<String, String>>();

    public void put(String selector, String rule) {
        styles.put(selector, wrap(rule));
    }

    public Map<String, String> get(String rule) {
        return styles.get(rule.toLowerCase());
    }

    private Map<String, String> wrap(String rule) {
        Map<String, String> ret = new LinkedHashMap<String, String>();
        String[] properties = rule.split(";");
        for (String property : properties) {
            String[] pair = property.trim().split(":");
            if (pair.length == 2) {
                ret.put(pair[0].trim(), pair[1].trim());
            }
        }
        return ret;
    }
}
