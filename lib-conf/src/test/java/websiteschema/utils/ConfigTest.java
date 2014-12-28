/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.utils;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import websiteschema.conf.Configure;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ray
 */
public class ConfigTest {

    @Test
    public void test() {
        Configure conf = new Configure("configure-site.ini");
        assertEquals(conf.getProperty("AnalyzerTips"), "abc");
    }

    /**
     * 从预先设定的prop中寻找是否需要替换。
     * 如果在prop有：abc = xyz
     * 如果value = ${abc}
     * 则value会被替换成xyz
     */
    @Test
    public void testFilter() {
        Configure conf0 = new Configure("configure-site.ini");
        assertEquals(conf0.getProperty("FiltedField"), "${abc}");
        //增加过滤
        Map<String, String> prop = new HashMap<String, String>();
        prop.put("abc", "xyz");
        prop.put("SITEID", "site_id");
        prop.put("JOBNAME", "job_name");
        prop.put("DBNAME", "db_name");
        prop.put("XPATH", "${XPATH}");
        Configure conf = new Configure("configure-site.ini", prop);
        assertEquals(conf.getProperty("FiltedField"), "xyz");
        assertEquals(conf.getProperty("XPATH"), "${XPATH}");
        assertEquals(conf.getProperty("TestField"), "CLS = xyz");
        System.out.println(conf.getMapProperty("TestField2"));
        assertEquals(conf.getMapProperty("TestField2").get("SITEID"), "site_id");

        System.out.println(conf.getMapProperty("TestField3"));
        assertEquals(conf.getMapProperty("TestField3").get("SITEID"), "site_id");
    }

    @Test
    public void testMultiLine() {
        Configure conf = new Configure("configure-site.ini");
        Map map = conf.getMapProperty("URLCharset", "CharsetMap");
        System.out.println(conf.getProperty("URLCharset", "CharsetMap"));
        assert (map.containsKey("x-gbk"));
        assertEquals(map.get("x-gbk"), "gbk");

        String value = conf.getProperty("Test", "key");
        System.out.println(value);
        assertEquals(value, "''''");

        assertEquals(conf.getProperty("Test", "abc"), "efg");
    }

    @Test
    public void testGetList() {
        Configure conf = new Configure("configure-site.ini");
        List<String> inlineNodeNames = conf.getListProperty("VIPS", "InlineNodeName");
        List<String> nodeNames = conf.getBean("VIPS", "InlineNodeName", List.class);
        for (int i = 0; i < inlineNodeNames.size(); i++) {
            String s1 = inlineNodeNames.get(i);
            String s2 = nodeNames.get(i);
            assertEquals(s1, s2);
        }
    }
}
