/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import org.apache.xerces.util.DOMUtil;
import org.jaxen.JaxenException;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import websiteschema.cluster.analyzer.Link;
import websiteschema.crawler.WebPage;
import websiteschema.element.DocumentUtil;
import websiteschema.element.W3CDOMUtil;
import websiteschema.fb.annotation.Description;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.utils.StringUtil;
import websiteschema.utils.UrlLinkUtil;

/**
 *
 * @author ray
 */
@EI(name = {"EI:EXT"})
@EO(name = {"EO", "EMPTY"})
@Description(desc = "抽取DOM树中指定位置内的链接。")
public class FBLinksExtractor extends FunctionBlock {

    @DI(name = "MUSTHAVE")
    public List<String> mustHave;
    @DI(name = "DONTHAVE")
    public List<String> dontHave;
    @DI(name = "IN")
    public Document in;
    @DI(name = "DOCS")
    public Document[] docs;
    @DI(name = "PAGE")
    public WebPage page;
    @DI(name = "XPATH")
    public String xpath = null;
    @DI(name = "URL")
    public String url;
    @DI(name = "PAGING")
    public boolean paging = true;
    @DI(name = "MAX_PAGING_NUM")
    public int maxPagingNumber = 1;
    @DO(name = "OUT", relativeEvents = {"EO"})
    public List<Link> links = null;

    @Algorithm(name = "EXT")
    public void extract() {
        try {
            extractLinks();
            if (null != links && !links.isEmpty()) {
                this.triggerEvent("EO");
            } else {
                this.triggerEvent("EMPTY");
            }
        } catch (Exception ex) {
            l.error(this.getName() + " error when extract links: " + ex.getMessage(), ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    private void extractLinks() {
        if (null != page && null != xpath && null != url) {
            links = extractLinks(page);
        } else if (null != docs && null != xpath && null != url) {
            links = new ArrayList<Link>();
            for (Document doc : docs) {
                List<Link> urls = extractLinks(doc, xpath);
                if (null != urls) {
                    for (Link lnk : urls) {
                        if (match(lnk, mustHave, dontHave)) {
                            links.add(lnk);
                        }
                    }
                }
            }
        } else if (null != in && null != xpath && null != url) {
            links = new ArrayList<Link>();
            List<Link> urls = extractLinks(in, xpath);
            if (null != urls) {
                for (Link lnk : urls) {
                    if (match(lnk, mustHave, dontHave)) {
                        links.add(lnk);
                    }
                }
            }
        } else if (null != docs && null != url) {
            links = new ArrayList<Link>();
            List<Link> urls = extractLinks(in, "//a");
            if (null != urls) {
                for (Link lnk : urls) {
                    if (match(lnk, mustHave, dontHave)) {
                        links.add(lnk);
                    }
                }
            }
        }
    }

    private List<Link> extractLinks(WebPage page) {
        List<Link> ret = extractOnePage(page);
        int num = maxPagingNumber;
        while (paging && --num > 0 && page.hasNext()) {
            WebPage next = page.getNext();
            if (null != next) {
                ret.addAll(extractOnePage(next));
            }
        }
        return ret;
    }

    private List<Link> extractOnePage(WebPage page) {
        List<Link> ret = new ArrayList<Link>();
        if (null != page && null != xpath && null != url) {
            if (null != page.getDocs()) {
                for (Document doc : page.getDocs()) {
                    List<Link> urls = extractLinks(doc, xpath);
                    if (null != urls) {
                        for (Link lnk : urls) {
                            if (match(lnk, mustHave, dontHave)) {
                                ret.add(lnk);
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }

    private boolean match(Link lnk, List<String> mustHave, List<String> dontHave) {
        boolean ret = true;

        if (null != mustHave) {
            String[] m = null != mustHave ? mustHave.toArray(new String[0]) : null;
            String[] d = null != dontHave ? dontHave.toArray(new String[0]) : null;
            ret = UrlLinkUtil.getInstance().matchOnePattern(lnk.getHref(), m, d);
        }

        return ret;
    }

    private List<Link> extractLinks(Document doc, String xpath) {
        if (null != doc && null != xpath && null != url) {
            List<Node> nodes = DocumentUtil.getByXPath(doc, xpath.trim());
            if (null != nodes) {
                List<Link> ret = new ArrayList<Link>();
                for (int i = 0; i < nodes.size(); i++) {
                    Node node = nodes.get(i);
                    List<Link> urls = getLinks(node);
                    if (null != urls) {
                        ret.addAll(urls);
                    }
                }
                return ret;
            }
        }
        return null;
    }

    private List<Link> getLinks(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            String nodeName = node.getNodeName();
            if (nodeName.equalsIgnoreCase("A")) {
                String href = DOMUtil.getAttrValue((Element) node, "href");
                StringBuilder text = new StringBuilder();
                W3CDOMUtil.getInstance().getNodeTextRecursive(node, text);
                if (null != href) {
                    URL link = UrlLinkUtil.getInstance().getURL(url, href);
                    if (null != link) {
                        List<Link> ret = new ArrayList<Link>();
                        Link lnk = new Link();
                        lnk.setHref(link.toString());
                        lnk.setText(StringUtil.trim(text.toString()));
                        ret.add(lnk);
                        return ret;
                    }
                }
            } else {
                List<Link> ret = new ArrayList<Link>();
                NodeList children = node.getChildNodes();
                if (null != children) {
                    for (int i = 0; i < children.getLength(); i++) {
                        Node child = children.item(i);
                        List<Link> urls = getLinks(child);
                        if (null != urls) {
                            ret.addAll(urls);
                        }
                    }
                }
                if (!ret.isEmpty()) {
                    return ret;
                }
            }
        }
        return null;
    }

    // 非递归版本（已通过单元测试）
    private List<Link> getLinks_nonRecur(Node root) {
        if (root.getNodeType() != Node.ELEMENT_NODE) {
            return null;
        }
        Queue<Node> qNodes = new ArrayDeque<Node>();
        qNodes.add(root);
        List<Link> retLinks = new ArrayList<Link>(50);// 经验值
        while (!qNodes.isEmpty()) {
            root = qNodes.poll();
            String nodeName = root.getNodeName();
            if (nodeName.equalsIgnoreCase("A")) {
                String href = DOMUtil.getAttrValue((Element) root, "href");
                StringBuilder text = new StringBuilder();
                W3CDOMUtil.getInstance().getNodeTextRecursive(root, text);
                if (null != href) {
                    URL link = UrlLinkUtil.getInstance().getURL(url, href);
                    if (null != link) {
                        Link lnk = new Link();
                        lnk.setHref(link.toString());
                        lnk.setText(StringUtil.trim(text.toString()));
                        retLinks.add(lnk);
                    }
                }
            } else {
                NodeList children = root.getChildNodes();
                if (null != children) {
                    for (int i = 0; i < children.getLength(); i++) {
                        Node child = children.item(i);
                        if (root.getNodeType() == Node.ELEMENT_NODE) {
                            qNodes.add(child);
                        }
                    }
                }
            }
        }
        return retLinks;
    }

    // XPath查询法（未通过单元测试）
    private List<Link> getLinks_bySelect(Node root) {
        DOMXPath xpath = null;
        List<Node> nodes = null;
        try {
            xpath = new DOMXPath("//A|//a");
            nodes = xpath.selectNodes(root);
        } catch (JaxenException ex) {
            ex.printStackTrace();
            return null;
        }

        List<Link> retLinks = new ArrayList<Link>(50);// 经验值
        for (Node node : nodes) {
            String href = DOMUtil.getAttrValue((Element) node, "href");
            StringBuilder text = new StringBuilder();
            W3CDOMUtil.getInstance().getNodeTextRecursive(node, text);
            if (null != href) {
                URL link = UrlLinkUtil.getInstance().getURL(url, href);
                if (null != link) {
                    Link lnk = new Link();
                    lnk.setHref(link.toString());
                    lnk.setText(StringUtil.trim(text.toString()));
                    retLinks.add(lnk);
                }
            }
        }

        return retLinks;
    }
}
