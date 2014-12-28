/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import websiteschema.element.DocumentUtil;
import websiteschema.utils.FileUtil;

/**
 *
 * @author ray
 */
public class DocTest {

    @Test
    public void should_Contains_Create_Time_Field() {
        try {
            Document doc = loadDocument("guba.xml");
            Doc d = new Doc(doc);

            String crDate = d.getValue("CREATE_TIME");
            System.out.println(crDate);
            assert (null != crDate && crDate.matches("\\d+"));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void should_load_XML_File_with_Ext_Fields() {
        try {
            Document doc = loadDocument("guba.xml");
            Doc d = new Doc(doc);

            Collection<Map<String, String>> threads = d.getExtValues("THREADS");
            assert (null != threads && threads.size() == 7);

            List<String> dates = d.getExtValue("THREADS/DATE");
            assert (null != dates && dates.size() == 7);
            assert ("2012-03-13 07:15:37".equals(dates.get(6)));
            dates = d.getExtValue("THREADS", "DATE");
            assert ("2012-03-12 22:19:45".equals(dates.get(0)));
            
            Document doc2 = d.toW3CDocument();
            String text = DocumentUtil.getXMLString(doc2);
            System.out.println(text);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void should_Change_Field_Name_When_Output_to_W3C_Document() {
        try {
            Document guba1 = loadDocument("guba.xml");
            Document guba2 = loadDocument("guba_2.xml");
            Doc d = new Doc(guba1);
            Doc d2 = new Doc(guba2);

            Document index = createNewDocument();
            Element root = index.createElement("ROOT");
            index.appendChild(root);

            Map<String, String> map = new HashMap<String, String>();
            map.put("DATE", "PUBLISHDATE");
            map.put("TITLE", "DRETITLE");
            map.put("URL", "DREREFERENCE");
            map.put("CONTENT", "DRECONTENT");

            Element eleDoc1 = index.createElement("DOCUMENT");
            root.appendChild(eleDoc1);
            d.toW3CDocument(index, eleDoc1, map);
            Element eleDoc2 = index.createElement("DOCUMENT");
            root.appendChild(eleDoc2);
            d2.toW3CDocument(index, eleDoc2, map);
            String text = DocumentUtil.getXMLString(index);
            System.out.println(text);
            assert(text.contains("PUBLISHDATE"));
            assert(text.contains("DRETITLE"));
            assert(text.contains("DREREFERENCE"));
            assert(text.contains("DRECONTENT"));
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    private Document loadDocument(String file) throws ParserConfigurationException, IOException, SAXException {
        String content = FileUtil.readResource(file);
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true); // never forget this!
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(content.getBytes()));
        return doc;
    }

    private Document createNewDocument() {
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true); // never forget this!
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document doc = builder.newDocument();
            return doc;
        } catch (Exception ex) {
            return null;
        }
    }
}
