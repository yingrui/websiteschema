/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer.fields;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import websiteschema.element.DocumentUtil;
import websiteschema.utils.StringUtil;

/**
 *
 * @author ray
 */
public class StandardExtractor extends AbstractFieldExtractor {

    private String xpath = null;
    private String prefix = null;
    private String suffix = null;
    private String regex = null;
    public final static String xpathKey = "XPath";
    public final static String prefixKey = "PrefixString";
    public final static String suffixKey = "SuffixString";
    public final static String regexKey = "Regex";
    private String[] htmlTags = {"a", "abbr", "acronym", "address", "applet", "area",
        "article", "aside", "audio", "b", "base", "basefont", "bdo", "big",
        "blockquote", "body", "br", "button", "canvas", "caption", "center", "cite",
        "code", "col", "colgroup", "command", "datalist", "dd", "del", "details",
        "dfn", "dir", "div", "dl", "dt", "em", "embed", "fieldset", "figcaption",
        "figure", "font", "footer", "form", "frame", "frameset", "h1", "h2", "h3",
        "h4", "h5", "h6", "head", "header", "hgroup", "hr", "html", "i", "iframe",
        "img", "input", "ins", "keygen", "isindex", "kbd", "label", "legend", "li",
        "link", "map", "mark", "menu", "meta", "meter", "nav", "noframes", "noscript",
        "object", "ol", "optgroup", "option", "output", "p", "param", "pre", "progress",
        "q", "rp", "rt", "ruby", "s", "samp", "script", "section", "select", "small",
        "source", "span", "strike", "strong", "style", "sub", "summary", "sup",
        "table", "tbody", "td", "textarea", "tfoot", "th", "thead", "time",
        "title", "tr", "tt", "u", "ul", "var", "video", "xmp"};
    private Set<String> standardHTMLTags = new HashSet<String>(Arrays.asList(htmlTags));
    private final static Pattern pat = Pattern.compile("</?([A-Za-z]+\\d?)( ([\\w\\d]+([ ]+)?=([ ]+)?((['\"]).*?(['\"])|[^ <>]+?) ?)*)*/?>");
    private boolean isEliminateAll = true;
    private Set<String> eliminateTags = new HashSet<String>();
    private Set<String> replaceNewLineTags = new HashSet<String>();

    public StandardExtractor() {
    }

    public Collection<String> extract(Document doc, String pageSource) {
        if (null != xpath && !"".equals(xpath)) {
            return extractByXPath(doc);
        } else if (!"".equals(prefix) && !"".equals(suffix)) {
            return extractByPattern(pageSource);
        }
        return null;
    }

    private Collection<String> extractByXPath(Document doc) {
        List<String> ret = new ArrayList<String>();
        if (null != doc) {
            List<Node> nodes = DocumentUtil.getByXPath(doc, xpath.trim());
            for (Node node : nodes) {
                String res = ExtractUtil.getInstance().getNodeText(node);
                if (StringUtil.isEmpty(res)) {
                    res = ExtractUtil.getInstance().getNodeTextRecusive(node);
                }
                if (StringUtil.isNotEmpty(res)) {
                    res = StringUtil.trim(filterByPattern(res));
                    if (null != regex && !"".equals(regex)) {
                        if (res.matches(regex) && !ret.contains(res)) {
                            ret.add(res);
                        }
                    } else if (!ret.contains(res)) {
                        ret.add(res);
                    }
                }
            }
        }
        return ret;
    }

    public String filterByPattern(String content) {
        String ret = content;
        if (StringUtil.isNotEmpty(prefix) && ret.contains(prefix)) {
            ret = ret.substring(ret.indexOf(prefix) + prefix.length());
        }

        if (StringUtil.isNotEmpty(suffix) && ret.contains(suffix)) {
            ret = ret.substring(0, ret.indexOf(suffix));
        }
        return ret;
    }

    private List<String> extractByPattern(String pageSource) {
        List<String> ret = new ArrayList<String>();

        int start = pageSource.indexOf(prefix);
        while (start >= 0) {
            int end = pageSource.indexOf(suffix, start + prefix.length());
            if (end > 0) {
                String res = pageSource.substring(start + prefix.length(), end);
                if (null != res && !"".equals(res)) {
                    res = filterHtmlTag(res);
                    res = StringUtil.trim(res);
                    if (null != regex && !"".equals(regex)) {
                        if (res.matches(regex) && !ret.contains(res)) {
                            ret.add(res);
                        }
                    } else if (!ret.contains(res)) {
                        ret.add(res);
                    }
                }
                start = pageSource.indexOf(prefix, end + suffix.length());
            } else {
                break;
            }
        }
        return ret;
    }

    public void init(Map<String, String> params) {
        xpath = params.containsKey(xpathKey) ? params.get(xpathKey) : "";
        prefix = params.containsKey(prefixKey) ? params.get(prefixKey) : "";
        suffix = params.containsKey(suffixKey) ? params.get(suffixKey) : "";
        regex = params.containsKey(regexKey) ? params.get(regexKey) : "";
    }

    public String filterHtmlTag(String content) {
        StringBuilder sb = new StringBuilder(content);

        Matcher m = pat.matcher(content);

        int start = 0;
        Stack<List> stack = new Stack<List>();
        while (m.find(start)) {
            String tag = m.group(1);
//            System.out.println(m.group() + ": start " + m.start() + " end " + m.end() + " and tag is: " + tag);
            start = m.start();
            if (isEliminate(tag)) {
                List pos = new ArrayList(3);
                pos.add(start);
                pos.add(m.end());
                pos.add(tag);
                stack.push(pos);
            }
            start++;
        }

        while (!stack.empty()) {
            List pos = stack.pop();
            String tag = (String) pos.get(2);
            if (replaceWithNewLine(tag)) {
                sb.replace((Integer) pos.get(0), (Integer) pos.get(1), "\n");
            } else {
                sb.delete((Integer) pos.get(0), (Integer) pos.get(1));
            }
        }

        return sb.toString();
    }

    private boolean isEliminate(String tag) {
        if (isEliminateAll) {
            return standardHTMLTags.contains(tag.toLowerCase());
        } else {
            return eliminateTags.contains(tag.toLowerCase());
        }
    }

    private boolean replaceWithNewLine(String tag) {
        return replaceNewLineTags.contains(tag.toLowerCase());
    }
}
