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
import org.junit.Test;
import org.w3c.dom.Document;
import websiteschema.cluster.analyzer.Doc;
import websiteschema.utils.FileUtil;

/**
 *
 * @author ray
 */
public class ValidatorTest {

    @Test
    public void test() throws Exception {
        List<String> fields = new ArrayList<String>();
        fields.add("PUBLISHDATE");
        fields.add("TITLE");
        fields.add("AUTHOR");
        FBValidate validator = new FBValidate();
        validator.doc = new Doc(create());
        validator.listNotEmpty = fields;
        validator.validateEmpty();
        assert(validator.reason.contains("AUTHOR"));
    }

    private Document create() throws Exception {
        String content = FileUtil.readResource("doc.xml");
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true); // never forget this!
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(content.getBytes("UTF-8")));
        return doc;
    }
}
