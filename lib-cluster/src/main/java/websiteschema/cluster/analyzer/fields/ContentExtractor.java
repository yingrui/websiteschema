/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer.fields;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import websiteschema.element.DocumentUtil;
import websiteschema.element.factory.XPathAttrFactory;
import websiteschema.utils.StringUtil;

/**
 *
 * @author ray
 */
public class ContentExtractor extends AbstractFieldExtractor {

    Logger l = Logger.getLogger(ContentExtractor.class);
    private String xpath = null;
    private String startXPath = null;
    private String endXPath = null;
    private String prefix = null;
    private String suffix = null;
    private boolean excludeInvalidNode = true;
    private boolean includeValidNodeOnly = true;
    private boolean keepHTMLTag = false;
    private boolean reached = false;
    public final static String xpathKey = "XPath";
    public final static String startXPathKey = "StartXPath";
    public final static String endXPathKey = "EndXPath";
    public final static String prefixKey = "Prefix";
    public final static String suffixKey = "Suffix";
    public final static String excludeInvalidKey = "ExcludeInvalidNode";
    public final static String includeValidKey = "IncludeValidNodeOnly";
    public final static String keepHTMLTagKey = "KeepHTMLTag";

    public Collection<String> extract(Document doc, String pageSource) {
        reached = false;
        String content = extractContentText(doc);
        if (null != content) {
            Set<String> ret = new HashSet<String>(1);
            ret.add(content.trim());
            return ret;
        }
        return null;
    }

    public String extractContentText(Document doc) {
        if (null != doc) {
            final StringBuilder content = new StringBuilder();
            long t1 = System.currentTimeMillis();
            if (valid(prefix) && valid(suffix)) {
                content.append(extractByPattern(prefix, suffix, doc));
            } else {
                Set<String> validNodes = this.toUppercase(getBasicAnalysisResult().getValidNodes());
                Set<String> invalidNodes = this.toUppercase(getBasicAnalysisResult().getInvalidNodes());
                if (StringUtil.isNotEmpty(xpath)) {
                    List<Node> nodes = DocumentUtil.getByXPath(doc, xpath.trim());
                    if (null != nodes && !nodes.isEmpty()) {
                        for (Node n : nodes) {
                            String parentXPath = XPathAttrFactory.getInstance().create(n.getParentNode(), getXPathAttr());
                            traversal(validNodes, invalidNodes, n, content, parentXPath);
                        }
                    } else {
                        traversal(validNodes, invalidNodes, doc.getDocumentElement(), content, null);
                    }
                } else {
                    traversal(validNodes, invalidNodes, doc.getDocumentElement(), content, null);
                }
            }
            long t2 = System.currentTimeMillis();
            l.debug("----- elaspe times : " + (t2 - t1) + " millseconds.");
            return StringEscapeUtils.unescapeHtml(filterByPattern(content.toString()));
        }
        return null;
    }

    private boolean valid(String str) {
        return null != str && !"".equals(str);
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

    /**
     * 根据节点的XPath，处理文本。
     * @param text
     * @param xpath
     * @param content
     * @param validNodes
     * @param invalidNodes
     * @return 返回true，表示到达结束节点
     */
    private void processText(String text, String xpath, StringBuilder content, final Set<String> validNodes, final Set<String> invalidNodes) {
        if (null != xpath) {
            xpath = xpath.toUpperCase();
            if (xpath.equalsIgnoreCase(this.startXPath)) {
                //如果是开始路径，则删除之前所有数据。
                content.delete(0, content.length());
            } else {
                boolean passed = false;
                if (includeValidNodeOnly) {
                    //仅接受有效节点的文本
                    if (isValidNode(xpath, validNodes)) {
                        passed = true;
                    }
                } else if (this.excludeInvalidNode) {
                    //接受所有不是无效节点的文本
                    if (!isInvalidNode(xpath, invalidNodes)) {
                        passed = true;
                    }
                } else {
                    //表示所有文本都可以接受，这种时候，一般都要设置开始节点和结束节点
                    passed = true;
                }
                if (passed) {
                    content.append(text);
                }
            }
        }
    }

    private boolean ignore(String tagName) {
        return "style".equalsIgnoreCase(tagName) || "script".equalsIgnoreCase(tagName);
    }

    private boolean isValidNode(String xpath, final Set<String> validNodes) {
        return validNodes.contains(xpath);
    }

    private boolean isInvalidNode(String xpath, final Set<String> invalidNodes) {
        return invalidNodes.contains(xpath);
    }

    private void traversal(final Set<String> validNodes, final Set<String> invalidNodes, Node node, StringBuilder ret, String parentXPath) {
        //如果没有碰到结束节点，就继续，如果碰到结束节点了，就结束返回
        if (!reached) {
            String nodeName = node.getNodeName();
            if (!isTextNode(node) && !ignore(nodeName)) {
                String xp = XPathAttrFactory.getInstance().create(node, getXPathAttr(), parentXPath);
                if (null != xp && !"".equals(xp)) {
                    //如果路径是结束路径，则返回true，表示到达结束节点。
                    if (xp.equalsIgnoreCase(this.endXPath)) {
                        reached = true;
                        return;
                    }
                    NodeList children = node.getChildNodes();
                    if (null != children) {
                        for (int i = 0; i < children.getLength(); i++) {
                            Node child = children.item(i);
                            if (isTextNode(child)) {
                                //处理文本节点
                                processText(child.getNodeValue(), xp, ret, validNodes, invalidNodes);
                            } else if (Node.ELEMENT_NODE == child.getNodeType()) {
                                traversal(validNodes, invalidNodes, child, ret, xp);
                            }
                        }
                        if (breakLine(nodeName)) {
                            ret.append("\n");
                        }
                    }

                }
            }
        }
    }

    private boolean breakLine(String node) {
        if (node.equalsIgnoreCase("BR") || node.equalsIgnoreCase("P")) {
            return true;
        }
        return false;
    }

    private boolean isTextNode(Node node) {
        return Node.TEXT_NODE == node.getNodeType();
    }

    private Set<String> toUppercase(Set<String> set) {
        Set<String> tmp = new HashSet<String>();
        for (String str : set) {
            tmp.add(str.toUpperCase());
        }
        set.clear();
        set.addAll(tmp);
        return set;
    }

    private String extractByPattern(String prefix, String suffix, Document doc) {
        String ret = null;
        String text = DocumentUtil.getXMLString(doc);
        int start = text.indexOf(prefix);
        if (start >= 0) {
            int end = text.indexOf(suffix, start + prefix.length());
            if (end > 0) {
                String title = text.substring(start + prefix.length(), end);
                if (null != title && !"".equals(title)) {
                    ret = title;
                }
            }
        }
        return ret;
    }

    public void init(Map<String, String> params) {
        xpath = params.containsKey(xpathKey) ? params.get(xpathKey) : "";
        startXPath = params.containsKey(startXPathKey) ? params.get(startXPathKey) : "";
        endXPath = params.containsKey(endXPathKey) ? params.get(endXPathKey) : "";
        prefix = params.containsKey(prefixKey) ? params.get(prefixKey) : "";
        suffix = params.containsKey(suffixKey) ? params.get(suffixKey) : "";
        if (params.containsKey(excludeInvalidKey)) {
            excludeInvalidNode = Boolean.valueOf(params.get(excludeInvalidKey));
        }
        if (params.containsKey(includeValidKey)) {
            includeValidNodeOnly = Boolean.valueOf(params.get(includeValidKey));
        }
    }

    public boolean isExcludeInvalidNode() {
        return excludeInvalidNode;
    }

    public boolean isIncludeValidNodeOnly() {
        return includeValidNodeOnly;
    }

    public boolean isKeepHTMLTag() {
        return keepHTMLTag;
    }
}
