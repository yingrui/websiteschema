/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer.fields;

import websiteschema.utils.StringUtil;
import websiteschema.element.W3CDOMUtil;
import websiteschema.element.XPathAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import websiteschema.element.DocumentUtil;
import websiteschema.element.factory.XPathAttrFactory;
import static websiteschema.utils.StringUtil.*;

/**
 *
 * @author ray
 */
public class BBSThreadExtractor extends AbstractFieldExtractor {

    Logger l = Logger.getLogger(BBSThreadExtractor.class);
    private String THREAD_AREA_XPATH = "";
    private String THREAD_DATE_XPATH = "";
    private String THREAD_DATE_FORMAT = "";
    private String THREAD_DATE_PATTERN = "";
    private String THREAD_AUTHOR_XPATH = "";
    private String THREAD_AUTHOR_PREFIX = "";
    private String THREAD_AUTHOR_SUFFIX = "";
    private String THREAD_CONTENT_XPATH = "";
    private String THREAD_CONTENT_PREFIX = "";
    private String THREAD_CONTENT_SUFFIX = "";
    private String SUBJECT_AREA_XPATH = "";
    private String SUBJECT_DATE_XPATH = "";
    private String SUBJECT_DATE_FORMAT = "";
    private String SUBJECT_DATE_PATTERN = "";
    private String SUBJECT_AUTHOR_XPATH = "";
    private String SUBJECT_AUTHOR_PREFIX = "";
    private String SUBJECT_AUTHOR_SUFFIX = "";
    private String SUBJECT_CONTENT_XPATH = "";
    private String SUBJECT_CONTENT_PREFIX = "";
    private String SUBJECT_CONTENT_SUFFIX = "";
    private boolean GET_AREA_XPATH = false;
    public final static String THREAD_AREA_XPATH_KEY = "ThreadAreaXPath";
    public final static String THREAD_DATE_XPATH_KEY = "ThreadDateXPath";
    public final static String THREAD_DATE_FORMAT_KEY = "ThreadDateFormat";
    public final static String THREAD_DATE_PATTERN_KEY = "ThreadDatePattern";
    public final static String THREAD_AUTHOR_XPATH_KEY = "ThreadAuthorXPath";
    public final static String THREAD_AUTHOR_PREFIX_KEY = "ThreadAuthorPrefix";
    public final static String THREAD_AUTHOR_SUFFIX_KEY = "ThreadAuthorSuffix";
    public final static String THREAD_CONTENT_XPATH_KEY = "ThreadContentXPath";
    public final static String THREAD_CONTENT_PREFIX_KEY = "ThreadContentPrefix";
    public final static String THREAD_CONTENT_SUFFIX_KEY = "ThreadContentSuffix";
    public final static String GET_AREA_XPATH_KEY = "GetAreaXPath";
    public final static String SUBJECT_AREA_XPATH_KEY = "SubjectAreaXPath";
    public final static String SUBJECT_DATE_XPATH_KEY = "SubjectDateXPath";
    public final static String SUBJECT_DATE_FORMAT_KEY = "SubjectDateFormat";
    public final static String SUBJECT_DATE_PATTERN_KEY = "SubjectDatePattern";
    public final static String SUBJECT_AUTHOR_XPATH_KEY = "SubjectAuthorXPath";
    public final static String SUBJECT_AUTHOR_PREFIX_KEY = "SubjectAuthorPrefix";
    public final static String SUBJECT_AUTHOR_SUFFIX_KEY = "SubjectAuthorSuffix";
    public final static String SUBJECT_CONTENT_XPATH_KEY = "SubjectContentXPath";
    public final static String SUBJECT_CONTENT_PREFIX_KEY = "SubjectContentPrefix";
    public final static String SUBJECT_CONTENT_SUFFIX_KEY = "SubjectContentSuffix";

    @Override
    public void init(Map<String, String> params) {
        THREAD_AREA_XPATH = params.containsKey(THREAD_AREA_XPATH_KEY) ? params.get(THREAD_AREA_XPATH_KEY) : "";
        THREAD_DATE_XPATH = params.containsKey(THREAD_DATE_XPATH_KEY) ? params.get(THREAD_DATE_XPATH_KEY) : "";
        THREAD_DATE_FORMAT = params.containsKey(THREAD_DATE_FORMAT_KEY) ? params.get(THREAD_DATE_FORMAT_KEY) : "";
        THREAD_DATE_PATTERN = params.containsKey(THREAD_DATE_PATTERN_KEY) ? params.get(THREAD_DATE_PATTERN_KEY) : "";
        THREAD_AUTHOR_XPATH = params.containsKey(THREAD_AUTHOR_XPATH_KEY) ? params.get(THREAD_AUTHOR_XPATH_KEY) : "";
        THREAD_AUTHOR_PREFIX = params.containsKey(THREAD_AUTHOR_PREFIX_KEY) ? params.get(THREAD_AUTHOR_PREFIX_KEY) : "";
        THREAD_AUTHOR_SUFFIX = params.containsKey(THREAD_AUTHOR_SUFFIX_KEY) ? params.get(THREAD_AUTHOR_SUFFIX_KEY) : "";
        THREAD_CONTENT_XPATH = params.containsKey(THREAD_CONTENT_XPATH_KEY) ? params.get(THREAD_CONTENT_XPATH_KEY) : "";
        THREAD_CONTENT_PREFIX = params.containsKey(THREAD_CONTENT_PREFIX_KEY) ? params.get(THREAD_CONTENT_PREFIX_KEY) : "";
        THREAD_CONTENT_SUFFIX = params.containsKey(THREAD_CONTENT_SUFFIX_KEY) ? params.get(THREAD_CONTENT_SUFFIX_KEY) : "";
        if (params.containsKey(GET_AREA_XPATH_KEY)) {
            GET_AREA_XPATH = Boolean.valueOf(params.get(GET_AREA_XPATH_KEY));
        }
        SUBJECT_AREA_XPATH = params.containsKey(SUBJECT_AREA_XPATH_KEY) ? params.get(SUBJECT_AREA_XPATH_KEY) : "";
        SUBJECT_DATE_XPATH = params.containsKey(SUBJECT_DATE_XPATH_KEY) ? params.get(SUBJECT_DATE_XPATH_KEY) : "";
        SUBJECT_DATE_FORMAT = params.containsKey(SUBJECT_DATE_FORMAT_KEY) ? params.get(SUBJECT_DATE_FORMAT_KEY) : "";
        SUBJECT_DATE_PATTERN = params.containsKey(SUBJECT_DATE_PATTERN_KEY) ? params.get(SUBJECT_DATE_PATTERN_KEY) : "";
        SUBJECT_AUTHOR_XPATH = params.containsKey(SUBJECT_AUTHOR_XPATH_KEY) ? params.get(SUBJECT_AUTHOR_XPATH_KEY) : "";
        SUBJECT_AUTHOR_PREFIX = params.containsKey(SUBJECT_AUTHOR_PREFIX_KEY) ? params.get(SUBJECT_AUTHOR_PREFIX_KEY) : "";
        SUBJECT_AUTHOR_SUFFIX = params.containsKey(SUBJECT_AUTHOR_SUFFIX_KEY) ? params.get(SUBJECT_AUTHOR_SUFFIX_KEY) : "";
        SUBJECT_CONTENT_XPATH = params.containsKey(SUBJECT_CONTENT_XPATH_KEY) ? params.get(SUBJECT_CONTENT_XPATH_KEY) : "";
        SUBJECT_CONTENT_PREFIX = params.containsKey(SUBJECT_CONTENT_PREFIX_KEY) ? params.get(SUBJECT_CONTENT_PREFIX_KEY) : "";
        SUBJECT_CONTENT_SUFFIX = params.containsKey(SUBJECT_CONTENT_SUFFIX_KEY) ? params.get(SUBJECT_CONTENT_SUFFIX_KEY) : "";
    }

    @Override
    public Collection<String> extract(Document doc, String pageSource) {
        return null;
    }

    @Override
    public Collection<Map<String, String>> extractExtData(Document doc, String pageSource) {
        List<Map<String, String>> ret = new ArrayList<Map<String, String>>();
        Set<String> invalidNodes = null;
        if (null != getBasicAnalysisResult()) {
            invalidNodes = this.toUppercase(getBasicAnalysisResult().getInvalidNodes());
        }
        Extractor extSubject = new Extractor(SUBJECT_AREA_XPATH,
                SUBJECT_DATE_XPATH, SUBJECT_DATE_FORMAT, SUBJECT_DATE_PATTERN,
                SUBJECT_AUTHOR_XPATH, SUBJECT_AUTHOR_PREFIX, SUBJECT_AUTHOR_SUFFIX,
                SUBJECT_CONTENT_XPATH, SUBJECT_CONTENT_PREFIX, SUBJECT_CONTENT_SUFFIX,
                invalidNodes, getXPathAttr());

        Collection<Map<String, String>> subject = extSubject.extract(doc);
        if (null != subject && !subject.isEmpty()) {
            ret.add(0, subject.iterator().next());
        }

        if (GET_AREA_XPATH) {
            MultiThreadExtractor extThread = new MultiThreadExtractor(THREAD_AREA_XPATH,
                    THREAD_DATE_XPATH, THREAD_DATE_FORMAT, THREAD_DATE_PATTERN,
                    THREAD_AUTHOR_XPATH, THREAD_AUTHOR_PREFIX, THREAD_AUTHOR_SUFFIX,
                    THREAD_CONTENT_XPATH, THREAD_CONTENT_PREFIX, THREAD_CONTENT_SUFFIX,
                    invalidNodes, getXPathAttr());
            Collection<Map<String, String>> threads = extThread.extract(doc);
            if (null != threads) {
                ret.addAll(threads);
            }
        } else {
            Extractor extThread = new Extractor(THREAD_AREA_XPATH,
                    THREAD_DATE_XPATH, THREAD_DATE_FORMAT, THREAD_DATE_PATTERN,
                    THREAD_AUTHOR_XPATH, THREAD_AUTHOR_PREFIX, THREAD_AUTHOR_SUFFIX,
                    THREAD_CONTENT_XPATH, THREAD_CONTENT_PREFIX, THREAD_CONTENT_SUFFIX,
                    invalidNodes, getXPathAttr());
            Collection<Map<String, String>> threads = extThread.extract(doc);
            if (null != threads) {
                ret.addAll(threads);
            }
        }

        return ret.isEmpty() ? null : ret;
    }

    private Set<String> toUppercase(Set<String> set) {
        if (null != set) {
            Set<String> tmp = new HashSet<String>();
            for (String str : set) {
                tmp.add(str.toUpperCase());
            }
            set.clear();
            set.addAll(tmp);
            return set;
        }
        return null;
    }

    public boolean isGetAreaXPath() {
        return GET_AREA_XPATH;
    }

    class Extractor {

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

        Extractor(String AREA_XPATH,
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
                        Map<String, String> thread = extractThread(node);
                        if (null != thread && !thread.isEmpty()) {
                            ret.add(thread);
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
        private Map<String, String> extractThread(Node node) {
            Map<String, String> ret = new LinkedHashMap<String, String>();

            StringBuilder html = new StringBuilder();

            W3CDOMUtil.getInstance().getNodeTextRecursive(node, html);

            //作者
            String author = extractAuthor(node, html.toString(), AUTHOR_XPATH, AUTHOR_PREFIX, AUTHOR_SUFFIX);
            if (isNotEmpty(author)) {
                ret.put("AUTHOR", author);
            }
            //日期
            String date = extractDate(node, html.toString(), DATE_XPATH, DATE_PATTERN, DATE_FORMAT);
            if (isNotEmpty(date)) {
                ret.put("DATE", date);
            }
            //正文
            String content = extractContent(node, html.toString(), CONTENT_XPATH, CONTENT_PREFIX, CONTENT_SUFFIX);
            if (isNotEmpty(content)) {
                ret.put("CONTENT", content);
            }
            return ret.isEmpty() ? null : ret;
        }

        private String extractAuthor(Node node, String nodeHtml, String ax, String ap, String as) {
            if (isNotEmpty(ax)) {
                // 遍历节点，找到相应XPath的节点。
                Node target = find(node, ax);
                if (null != target) {
                    StringBuilder content = new StringBuilder();
                    W3CDOMUtil.getInstance().getNodeTextRecursive(target, content);
                    String nodeText = content.toString();
                    if (isNotEmpty(ap) && nodeText.contains(ap)) {
                        nodeText = nodeText.substring(nodeText.indexOf(nodeText) + ap.length());
                    }
                    if (isNotEmpty(as) && nodeText.contains(as)) {
                        nodeText = nodeText.substring(0, nodeText.indexOf(as));
                    }
                    //截取nodeText前缀和后缀之间的子串作为作者
                    return StringUtil.trim(nodeText);
                }
            } else {
                String res = substring(nodeHtml, ap, as);
                return res;
            }
            return null;
        }

        private String extractDate(Node node, String nodeHtml, String dateXPath, String datePattern, String dateFormat) {
            if (isNotEmpty(dateXPath)) {
                // 遍历节点，找到相应XPath的节点。
                Node target = find(node, dateXPath);
                if (null != target) {
                    //获取dateText并对这个串进行截取，获得日期格式相匹配的日期格式                   
                    StringBuilder content = new StringBuilder();
                    W3CDOMUtil.getInstance().getNodeTextRecursive(target, content);
                    String date = content.toString();
                    if (null != date) {
                        date = date.replaceAll("[\r\n]+", " ");
                        date = StringUtil.trim(date);
                        date = DateDetectUtil.getInstance().parseDate(date, datePattern, dateFormat);
                    }
                    return date;
                }
            } else {
                //                nodeHtml
            }
            return null;
        }

        private String extractContent(Node node, String nodeHtml, String cx, String cp, String cs) {
            if (isNotEmpty(cx)) {
                Node target = find(node, cx);
                if (null != target) {
                    StringBuilder content = new StringBuilder();
                    traversal(invalidNodes, target, content);
                    return content.toString();
                }
                return null;
            } else {
                String res = substring(nodeHtml, cp, cs);
                return res;
            }
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

        private Node find(Node cur, String targetXPath) {
            if (!isTextNode(cur)) {
                String xpath = XPathAttrFactory.getInstance().create(cur, xpathAttr);
                if (xpath.equalsIgnoreCase(targetXPath)) {
                    return cur;
                } else {
                    NodeList children = cur.getChildNodes();
                    if (null != children) {
                        for (int i = 0; i < children.getLength(); i++) {
                            Node child = children.item(i);
                            Node target = find(child, targetXPath);
                            if (null != target) {
                                return target;
                            }
                        }
                    }
                }
            } else {
                String xpath = XPathAttrFactory.getInstance().create(cur, xpathAttr);
                if (xpath.equalsIgnoreCase(targetXPath)) {
                    return cur;
                }
                return null;
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
}
