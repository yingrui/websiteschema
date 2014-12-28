/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
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
public class FieldFilterTest {

    @Test
    public void test() throws Exception {
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("SOURCENAME", "websiteschema.cluster.analyzer.fields.SourceNameFilter");
        FBFieldFilter filter = new FBFieldFilter();
        filter.doc = new Doc(create());
        filter.filters = fields;
        filter.filtering();
        System.out.println(filter.doc.getValues("SOURCENAME"));
        assert ("金融时报".equals(filter.doc.getValue("SOURCENAME")));
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
