/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import websiteschema.utils.PojoMapper;

/**
 *
 * @author mgd
 */
public class UnitExtractorTest {

    private Document create() throws Exception {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        // domFactory.setNamespaceAware(true); // never forget this!
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.parse(getClass().getClassLoader().getResourceAsStream("guba.xml"));
        return doc;
    }

    @Test
    public void testUnitExtract() throws Exception {
        String json = "[{\"regex\":\"(\\\\d+)\",\"name\":\"点击\",\"path\":\"td[1]/text()\"},{\"name\":\"回复\",\"path\":\"td[2]/text()\"},{\"name\":\"标题\",\"path\":\"td[3]/a[1]/text()\"},{\"name\":\"链接\",\"path\":\"td[3]/a[1]/@href\"},{\"name\":\"来源\",\"path\":\"td[3]/a[2]/text()\"},{\"name\":\"作者\",\"path\":\"td[4]/div/a/text()\"}]";
        List<Map<String, String>> conf = PojoMapper.fromJson(json, List.class);
        FBUnitExtractor ue = new FBUnitExtractor();
        Document doc = create();
        ue.doc = doc;
        ue.url = "http://guba.sina.com.cn/";
        ue.points = conf;
        ue.unitXPath = "/html/body/div/div/div/div/div/table/tr";
        ue.extractUnits();
        ue.printUnits();

    }

    //@Test
    public void testJson() throws Exception {
        List<Map<String, String>> conf = new ArrayList<Map<String, String>>();
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("name", "点击");
        map1.put("path", "td[1]/text()");
        map1.put("regex", "(\\d+)");
        conf.add(map1);
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("name", "回复");
        map2.put("path", "td[2]/text()");
        conf.add(map2);
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("name", "标题");
        map3.put("path", "td[3]/a[1]/text()");
        conf.add(map3);
        Map<String, String> map4 = new HashMap<String, String>();
        map4.put("name", "链接");
        map4.put("path", "td[3]/a[1]/@href");
        conf.add(map4);
        Map<String, String> map5 = new HashMap<String, String>();
        map5.put("name", "来源");
        map5.put("path", "td[3]/a[2]/text()");
        conf.add(map5);
        Map<String, String> map6 = new HashMap<String, String>();
        map6.put("name", "作者");
        map6.put("path", "td[4]/div/a/text()");
        conf.add(map6);

        String json = PojoMapper.toJson(conf);
        System.out.println(json);
    }

    // @Test
    public void testXPathSelect() throws Exception {
        Document doc = create();
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression xPathExp = xpath.compile("//table/tr");
        NodeList nodesUnit = (NodeList) xPathExp.evaluate(doc, XPathConstants.NODESET);
        int hrefNum = nodesUnit.getLength();
        System.out.println(hrefNum);

        xPathExp = xpath.compile("child::td");
        NodeList nodesPoint = (NodeList) xPathExp.evaluate(nodesUnit.item(1), XPathConstants.NODESET);
        System.out.println(nodesPoint.getLength());

        for (int i = 0; i < hrefNum; ++i) {
            nodesPoint = (NodeList) xPathExp.evaluate(nodesUnit.item(i), XPathConstants.NODESET);
            for (int j = 0; j < nodesPoint.getLength(); ++j) {
                System.out.println(nodesUnit.item(j).toString());
            }
        }

    }
}
