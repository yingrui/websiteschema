/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.apache.xerces.util.DOMUtil;
import org.w3c.dom.Element;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import websiteschema.cluster.analyzer.Link;
import websiteschema.utils.FileUtil;

/**
 *
 * @author ray
 */
public class LinksExtractorTest {

    //@Test
    public void test() throws Exception {
        Document doc = create();
        String xpath = "//DIV[@class='main_content clearfix']";
        String url = "http://focus.news.163.com/";
        FBLinksExtractor extractor = new FBLinksExtractor();
        extractor.in = doc;
        extractor.xpath = xpath;
        extractor.url = url;
        extractor.extract();
        List<Link> results = extractor.links;
        for (Link lnk : results) {
            System.out.println(lnk.getHref());
        }
        assert (results.size() == 26);
    }

    private Document create() throws Exception {
        String content = FileUtil.readResource("test.xml");
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true); // never forget this!
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(content.getBytes("UTF-8")));
        return doc;
    }

    @Test
    public void testXPathSelect() throws Exception {
        String content = FileUtil.readResource("test.xml");
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(content.getBytes("UTF-8")));

        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression xPathExp = null;
        NodeList nodes = null;
        try {
            xPathExp = xpath.compile("//DIV[@class='main_content clearfix']");
            nodes = (NodeList) xPathExp.evaluate(doc, XPathConstants.NODESET);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        int hrefNum = nodes.getLength();

        List<String> retLinks = new ArrayList<String>(50);// 经验值
        NodeList nodeList = null;
        for (int i = 0; i < hrefNum; ++i) {
            xPathExp = xpath.compile("DIV//A");
            Element ele = (Element) nodes.item(i);
            nodeList = (NodeList) xPathExp.evaluate(ele, XPathConstants.NODESET);
            for (int j = 0; j < nodeList.getLength(); ++j) {
                String href = DOMUtil.getAttrValue((Element) nodeList.item(j), "href");
                if (null != href) {
                    retLinks.add(href);
                    System.out.println(href);
                }
            }
        }

        //        int hrefNum = nodes.getLength();
//
//        List<String> retLinks = new ArrayList<String>(50);// 经验值
//        for (int i = 0; i < hrefNum; ++i) {
//            NodeList nodeList = ((Element)nodes.item(i)).getElementsByTagName("A");
//            for (int j = 0; j < nodeList.getLength(); ++j) {
//                String href = DOMUtil.getAttrValue((Element) nodeList.item(j), "href");
//                if (null != href) {
//                    retLinks.add(href);
//                    System.out.println(href);
//                }
//            }
//        }

        assert (retLinks.size() == 26);
    }
}
