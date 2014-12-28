/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.webpage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import websiteschema.cluster.analyzer.Link;
import websiteschema.element.DocumentUtil;
import org.apache.xerces.util.DOMUtil;
import org.w3c.dom.Element;
import websiteschema.element.W3CDOMUtil;
import websiteschema.utils.StringUtil;
import websiteschema.utils.UrlLinkUtil;

/**
 *
 * @author ray
 */
public class NextPageAnalyzer {

    private Document doc;
    private String url;
    private List<String> possibleNextPages;
    private Logger l = Logger.getLogger(getClass());

    public NextPageAnalyzer(Document doc, String url) {
        this.doc = doc;
        this.url = url;
    }

    private NextPageAnalyzer() {
    }

    public void analysis() {
        List<Node> nodes = DocumentUtil.getByXPath(doc, "//a");
        if (null != nodes && !nodes.isEmpty()) {
            possibleNextPages = new ArrayList<String>();
            for (Node node : nodes) {
                Link lnk = getLink(node);
                if (null != lnk) {
                    if (isNextUrl(url, lnk)) {
                        if (!possibleNextPages.contains(lnk.getHref())) {
                            possibleNextPages.add(lnk.getHref());
                        }
                    }
                }
            }
            sort();
        }
    }

    private void sort() {
        class UrlComparator implements Comparator<String> {

            @Override
            public int compare(String str1, String str2) {
                int minus = str1.length() - str2.length();
                if (minus == 0) {
                    return str1.compareTo(str2);
                } else {
                    return minus;
                }
            }
        }
        Collections.sort(possibleNextPages, new UrlComparator());
    }

    public List<String> getResults() {
        return possibleNextPages;
    }

    private Link getLink(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            String href = DOMUtil.getAttrValue((Element) node, "href");
            if (null != href) {
                StringBuilder text = new StringBuilder();
                W3CDOMUtil.getInstance().getNodeTextRecursive(node, text);
                URL link = UrlLinkUtil.getInstance().getURL(url, href);
                if (null != link) {
                    Link ret = new Link();
                    ret.setHref(link.toString());
                    ret.setText(StringUtil.trim(text.toString()));
                    return ret;
                }
            }
        }
        return null;
    }

    private boolean isNextUrl(String pageUrl, Link link) {
        String anyUrl = link.getHref();
        if (null == pageUrl || null == anyUrl) {
            return false;
        }
        if (Math.abs(pageUrl.length() - anyUrl.length()) > 3) {
            return false;
        }
        String prefix = findPrefix(pageUrl, anyUrl);
        String suffix = findSuffix(pageUrl, anyUrl, prefix.length());
        try {
            String differ = anyUrl.substring(prefix.length(), anyUrl.length() - suffix.length());
            if (differ.matches("([\\p{Punct}])?(\\d{1,2})")) {
                String text = link.getText();
                if (null != text) {
                    text = StringUtil.trim(text);
                    if (text.matches("\\d+")) {
                        return true;
                    } else if (text.length() <= 5 && text.matches("(.*下一页.*)|(Next)|(.*[>》]+.*)")) {
                        return true;
                    }
                }
            }
        } catch (StringIndexOutOfBoundsException ex) {
            l.debug("no correct prefix and suffix found.");
        }
        return false;
    }

    private String findPrefix(String str, String prefix) {
        String ret = prefix;
        while (!str.startsWith(ret)) {
            ret = ret.substring(0, ret.length() - 1);
            if (ret.length() <= 0) {
                break;
            }
        }
        return ret;
    }

    private String findSuffix(String str, String suffix, int prefixLength) {
        String ret = "";
        if (suffix.length() > str.length()) {
            ret = suffix;
            int maxLength = str.length();
            while (!str.endsWith(ret)) {
                ret = ret.substring(1);
                if (ret.length() <= 0) {
                    break;
                }
            }
            while (prefixLength + ret.length() > maxLength && !ret.isEmpty()) {
                ret = ret.substring(1);
            }
        }
        return ret;
    }

    public static void main(String[] arg) {
        String str1 = "http://news.xinhuanet.com/sports/2012-06/01/c_112087172.htm";
        String str2 = "http://news.xinhuanet.com/sports/2012-06/01/c_112087172_2.htm";
        Link l = new Link();
        l.setHref(str2);
        l.setText("2");
        NextPageAnalyzer npa = new NextPageAnalyzer();
        System.out.println(npa.isNextUrl(str1, l));
    }
}
