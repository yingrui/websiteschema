/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer.fields;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import websiteschema.element.DocumentUtil;
import websiteschema.utils.StringUtil;

/**
 *
 * @author ray
 */
public class XPathExtractor extends AbstractFieldExtractor {

    private String xpath = null;
    private String regex = null;
    public final static String xpathKey = "XPath";
    public final static String regexKey = "Regex";

    public Collection<String> extract(Document doc, String pageSource) {
        if (null != xpath && !"".equals(xpath)) {
            return extractByXPath(doc);
        }
        return null;
    }

    private Collection<String> extractByXPath(Document doc) {
        List<String> ret = new ArrayList<String>();
        List<Node> nodes = DocumentUtil.getByXPath(doc, xpath.trim());
        for (Node node : nodes) {
            String res = ExtractUtil.getInstance().getNodeText(node);
            if (null != res && !"".equals(res)) {
                res = StringUtil.trim(res);
                if (null != regex && !"".equals(regex)) {
                    if (res.matches(regex) && !ret.contains(res)) {
                        ret.add(res);
                    }
                } else if (!ret.contains(res)) {
                    ret.add(res);
                }
            }
        }
        return ret;
    }

    public void init(Map<String, String> params) {
        xpath = params.containsKey(xpathKey) ? params.get(xpathKey) : "";
        regex = params.containsKey(regexKey) ? params.get(regexKey) : "";
    }
}
