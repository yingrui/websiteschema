/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer.fields;

import websiteschema.utils.StringUtil;
import java.util.*;
import org.apache.log4j.helpers.ThreadLocalMap;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import websiteschema.element.DocumentUtil;
import websiteschema.element.W3CDOMUtil;
import websiteschema.element.XPathAttributes;
import websiteschema.element.factory.XPathAttrFactory;
import static websiteschema.utils.StringUtil.*;

/**
 *
 * @author Administrator
 */
public class OtherMultiThreadExtractor {

    String AREA_XPATH = "";
    String DATE_XPATH = "";
    String DATE_FORMAT = "";
    String DATE_PATTERN = "";
    String AUTHOR_XPATH = "";
    String AUTHOR_PREFIX = "";
    String AUTHOR_SUFFIX = "";
    String CONTENT_XPATH = "";
    String CONTENT_PREFIX = "";
    String CONTENT_SUFFIX = "";
    Set<String> invalidNodes = null;
    XPathAttributes xpathAttr = null;

    OtherMultiThreadExtractor(String AREA_XPATH,
            String DATE_XPATH, String DATE_FORMAT, String DATE_PATTERN,
            String AUTHOR_XPATH, String AUTHOR_PREFIX, String AUTHOR_SUFFIX,
            String CONTENT_XPATH, String CONTENT_PREFIX, String CONTENT_SUFFIX,
            Set<String> invalidNodes, XPathAttributes xpathAttr) {
        this.AREA_XPATH = AREA_XPATH;
        this.DATE_XPATH = DATE_XPATH;
        this.DATE_FORMAT = DATE_FORMAT;
        this.DATE_PATTERN = DATE_PATTERN;
        this.AUTHOR_XPATH = AUTHOR_XPATH;
        this.AUTHOR_PREFIX = AUTHOR_PREFIX;
        this.AUTHOR_SUFFIX = AUTHOR_SUFFIX;
        this.CONTENT_XPATH = CONTENT_XPATH;
        this.CONTENT_PREFIX = CONTENT_PREFIX;
        this.CONTENT_SUFFIX = CONTENT_SUFFIX;
        this.invalidNodes = invalidNodes;
        this.xpathAttr = xpathAttr;
    }

    public Collection<Map<String, String>> extract(Document doc) {
        List<Map<String, String>> ret = null;
        if (!"".equals(AREA_XPATH)) {
            //如果包含区域XPATH
            ret = extractThreads(doc, AREA_XPATH);
        }
        return ret;
    }

    private List<Map<String, String>> extractThreads(Document doc, String areaXPath) {
        List<Map<String, String>> ret = null;
        if (!"".equals(areaXPath)) {
            List<Node> nodes = DocumentUtil.getByXPath(doc, areaXPath);
            if (null != nodes && !nodes.isEmpty()) {
                ret = new ArrayList<Map<String, String>>();
                for (Node node : nodes) {
                    Collection<Map<String, String>> thread = extractThread(node);
                    if (null != thread && !thread.isEmpty()) {
                        ret.addAll(thread);
                    }
                }
            }
        }
        return ret;
    }

    /**
     * Thread就是论坛中的帖子以及回帖
     *
     * @param node
     * @return
     */
    private Collection<Map<String, String>> extractThread(Node node) {
        List<Map<String, String>> ret = new ArrayList<Map<String, String>>();

        StringBuilder html = new StringBuilder();

        W3CDOMUtil.getInstance().getNodeTextRecursive(node, html);

        //作者
        List<String> author = extractAuthors(node, html.toString(), AUTHOR_XPATH, AUTHOR_PREFIX, AUTHOR_SUFFIX);
        //日期
        List<String> date = extractDates(node, html.toString(), DATE_XPATH, DATE_PATTERN, DATE_FORMAT);
        //内容
        List<String> content = extractContents(node, html.toString(), CONTENT_XPATH, CONTENT_PREFIX, CONTENT_SUFFIX);

        int authorListI = 0;
        if (!author.isEmpty()) {

            authorListI = author.size();
        }

        int dateListI = 0;
        if (!date.isEmpty()) {
            dateListI = date.size();
        }

        int contentListI = 0;
        if (content.isEmpty()) {
            contentListI = content.size();
        }

        int maxlength = 0;
        maxlength = authorListI > dateListI ? authorListI : dateListI;
        maxlength = maxlength > contentListI ? maxlength : contentListI;

        for (int i = 0; i < maxlength; i++) {
            Map<String, String> threadMap = new HashMap<String, String>(50);

            threadMap.put("AUTHOR", author.isEmpty() ? "" : author.get(i));
            threadMap.put("DATE", date.isEmpty() ? "" : date.get(i));
            threadMap.put("CONTENT", content.isEmpty() ? "" : content.get(i));
            if (null != threadMap && !threadMap.isEmpty()) {
                ret.add(threadMap);
            }
        }
        return ret;
    }

    private List<String> extractAuthors(Node node, String nodeHtml, String ax, String ap, String as) {
        List<String> authorList = new ArrayList<String>();
        if (isNotEmpty(ax)) {
            // 遍历节点，找到相应XPath的节点。
            List<Node> target = findNodes(node, ax);
            if (null != target && !target.isEmpty()) {
                for (Node nodes : target) {
                    String nodeText = W3CDOMUtil.getInstance().getNodeText(nodes);
                    if (isNotEmpty(ap) && nodeText.contains(ap)) {
                        nodeText = nodeText.substring(nodeText.indexOf(nodeText) + ap.length());
                    }
                    if (isNotEmpty(as) && nodeText.contains(as)) {
                        nodeText = nodeText.substring(0, nodeText.indexOf(as));
                    }
                    authorList.add(StringUtil.trim(nodeText));
                }
                return authorList;
            }
        }
        return null;
    }

    private List<String> extractDates(Node node, String nodeHtml, String dateXPath, String datePattern, String dateFormat) {
        List<String> dateList = new ArrayList<String>();
        if (isNotEmpty(dateXPath)) {
            // 遍历节点，找到相应XPath的节点。
            List<Node> target = findNodes(node, dateXPath);
            if (null != target && !target.isEmpty()) {
                for (Node dateNode : target) {
                    String date = W3CDOMUtil.getInstance().getNodeText(dateNode);
                    date = date.replaceAll("[\r\n]+", " ");
                    date = StringUtil.trim(date);
                    dateList.add(DateDetectUtil.getInstance().parseDate(date, datePattern, dateFormat));
                }
                return dateList;
            }
        } else {
//                nodeHtml
        }
        return null;
    }

    private List<String> extractContents(Node node, String nodeHtml, String cx, String cp, String cs) {
        List<String> contentList = new ArrayList<String>();
        if (isNotEmpty(cx)) {
            List<Node> target = findNodes(node, cx);
            if (null != target && !target.isEmpty()) {
                for (Node contentNode : target) {
                    StringBuilder content = new StringBuilder();
                    traversal(invalidNodes, contentNode, content);
                    String contentString = content.toString();
                    if (isNotEmpty(cp) && contentString.contains(cs)) {
                        contentString = contentString.substring(contentString.indexOf(contentString) + cp.length());
                    }
                    if (isNotEmpty(cp) && contentString.contains(cs)) {
                        contentString = contentString.substring(0, contentString.indexOf(cs));
                    }
                    contentList.add(contentString);
                }
            }
            return contentList;
        }
        return null;
    }

    private String substring(String text, String prefix, String suffix) {
        int start = text.indexOf(prefix);
        if (start >= 0) {
            int end = text.indexOf(suffix, start + prefix.length());
            if (end > 0) {
                String res = text.substring(start + prefix.length(), end);
                if (null != res && !"".equals(res)) {
                    res = trim(res);
                    if (isNotEmpty(res)) {
                        return res;
                    }
                }
                start = text.indexOf(prefix, end + suffix.length());
            }
        }
        return null;
    }

    /**
     * 根据节点的XPath，处理文本。
     *
     * @param text
     * @param xpath
     * @param content
     * @param validNodes
     * @param invalidNodes
     * @return 返回true，表示到达结束节点
     */
    private void processText(String text, String xpath, StringBuilder content, final Set<String> invalidNodes) {
        if (null != xpath) {
            xpath = xpath.toUpperCase();
            boolean passed = false;
//                if (xpath.equalsIgnoreCase(startXPath)) {
//                    //如果是开始路径，则删除之前所有数据。
//                    content.delete(0, content.length());
//                    passed = true;
//                } else
            {
                if (null != invalidNodes && invalidNodes.contains(xpath)) {
                    //仅接受不是无效节点的文本
                    passed = false;
                } else {
                    //表示所有文本都可以接受，这种时候，一般都要设置开始节点和结束节点
                    passed = true;
                }
            }
            if (passed) {
                content.append(text);
            }
        }
    }

    private boolean ignore(String tagName) {
        return "style".equalsIgnoreCase(tagName) || "script".equalsIgnoreCase(tagName);
    }

    private List<Node> findNodes(Node cur, String targetXPath) {
        if (!isTextNode(cur)) {
            List<Node> retNodes = new ArrayList<Node>(100);
            Queue<Node> qNodes = new ArrayDeque<Node>();
            qNodes.add(cur);
            while (!qNodes.isEmpty()) {
                cur = qNodes.poll();
                String xpath = XPathAttrFactory.getInstance().create(cur, xpathAttr);
                if (xpath.equalsIgnoreCase(targetXPath)) {
                    retNodes.add(cur);
                } else {
                    NodeList children = cur.getChildNodes();
                    if (null != children) {
                        for (int i = 0; i < children.getLength(); i++) {
                            Node child = children.item(i);
                            if (!isTextNode(child)) {
                                qNodes.add(child);
                            }
                        }
                    }
                }
            }
            return retNodes;
        }
        return null;
    }

    private void traversal(final Set<String> invalidNodes, Node node, StringBuilder ret) {
        String nodeName = node.getNodeName();
        if (!isTextNode(node) && !ignore(nodeName)) {
            String xpath = XPathAttrFactory.getInstance().create(node, xpathAttr);
            if (null != xpath && !"".equals(xpath)) {

                NodeList children = node.getChildNodes();
                if (null != children) {
                    for (int i = 0; i < children.getLength(); i++) {
                        Node child = children.item(i);
                        if (isTextNode(child)) {
                            //处理文本节点
                            processText(child.getNodeValue(), xpath, ret, invalidNodes);
                        } else if (Node.ELEMENT_NODE == child.getNodeType()) {
                            traversal(invalidNodes, child, ret);
                        }
                    }
                    if (breakLine(nodeName)) {
                        ret.append("\n");
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
}
