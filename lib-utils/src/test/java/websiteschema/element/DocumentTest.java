/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.element;

import org.w3c.dom.Node;
import java.util.List;
import java.io.ByteArrayInputStream;
import websiteschema.utils.FileUtil;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.Test;
import org.xml.sax.SAXException;
import static websiteschema.element.DocumentUtil.*;
import static org.junit.Assert.*;

/**
 *
 * @author ray
 */
public class DocumentTest {

    @Test
    public void testBuildXPath() {
        assertEquals(buildXPath("//html", "pre"), "//pre:html");
        assertEquals(buildXPath("html", "pre"), "pre:html");
        assertEquals(buildXPath("html[@id='attr']/meta", "pre"), "pre:html[@id='attr']/pre:meta");
        assertEquals(buildXPath("//@id", "pre"), "//@id");
        assertEquals(buildXPath("//*", "pre"), "//*");
        assertEquals(buildXPath("//p/a/*", "pre"), "//pre:p/pre:a/*");
        assertEquals(buildXPath("//p[a='xy']/a/*", "bean"), "//bean:p[a='xy']/bean:a/*");
        assertEquals(buildXPath("//@id/p", "pre"), "//@id/pre:p");
        assertEquals(buildXPath("//a/p/@id", "pre"), "//pre:a/pre:p/@id");
        assertEquals(buildXPath("//html/META[@id='abc']", "pre"), "//pre:html/pre:META[@id='abc']");
        assertEquals(buildXPath("/html/META[@id='abc']", "pre"), "/pre:html/pre:META[@id='abc']");
        assertEquals(buildXPath("/html/META[@id='abc']/text()", "pre"), "/pre:html/pre:META[@id='abc']/text()");
        assertEquals(buildXPath("HTML/BODY/DIV[@id='wrapper']/DIV[@id='container']/DIV[@class='area']/DIV[@class='content focusnews']/DL/DD/DIV[@class='leftCont leftContMain']/UL[@class='hotnews' @id='hotnews']/LI[@class='top']/A[@class='a3']/FONT/text()", "pre"),
                "pre:HTML/pre:BODY/pre:DIV[@id='wrapper']/pre:DIV[@id='container']/pre:DIV[@class='area']/pre:DIV[@class='content focusnews']/pre:DL/pre:DD/pre:DIV[@class='leftCont leftContMain']/pre:UL[@class='hotnews' @id='hotnews']/pre:LI[@class='top']/pre:A[@class='a3']/pre:FONT/text()");

        assertEquals(buildXPath("/html/META[@id='abc'] | /html/body", "pre"), "/pre:html/pre:META[@id='abc'] | /pre:html/pre:body");
    }

    @Test
    public void testXPath() throws ParserConfigurationException, IOException, SAXException {
        String content = FileUtil.readResource("book.xml");
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true); // never forget this!
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(content.getBytes()));
        List<Node> nodes = getByXPath(doc, "//publisher");
        assert (nodes.size() == 3);
        for (Node node : nodes) {
            System.out.println(node.getNodeName() + " : " + node.getNodeValue() + " " + node.getTextContent());
        }
        nodes = getByXPath(doc, "inventory/book[1]/publisher[1]");
        for (Node node : nodes) {
            System.out.println(node.getNodeName() + " : " + node.getNodeValue() + " " + node.getTextContent());
        }
    }

    @Test
    public void testXPath2() throws ParserConfigurationException, IOException, SAXException {
        String content = FileUtil.readResource("same-id.xml");
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true); // never forget this!
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(content.getBytes()));
        List<Node> nodes = getByXPath(doc, "html/body/div[@id='123']/div[@id='123']/title");
        assert (nodes.size() == 1);
        for (Node node : nodes) {
            System.out.println(node.getNodeName() + " : " + node.getNodeValue() + " " + node.getTextContent());
        }
    }

    @Test
    public void testXML() throws ParserConfigurationException, IOException, SAXException {
        String content = FileUtil.readResource("book.xml");
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true); // never forget this!
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(content.getBytes()));
        System.out.println(DocumentUtil.getXMLString(doc));
    }
}
