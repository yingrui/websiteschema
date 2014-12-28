/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import websiteschema.cluster.analyzer.Link;

/**
 *
 * @author ray
 */
public class BeanWrapperTest {

    @Test
    public void test() throws InterruptedException, ClassNotFoundException {

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("clicked", "123");
        map.put("reply", "2");
        map.put("href", "123.html");
        map.put("text", "title");
        data.add(map);

        FBBeanWrapper wrapper = new FBBeanWrapper();
        wrapper.classType = Link.class.getName();
        wrapper.input = data;

        wrapper.wrap();

        List<Object> output = wrapper.output;
        for (Object obj : output) {
            Link lnk = (Link) obj;
            System.out.println(lnk.getText() + " " + lnk.getHref() + " " + lnk.getClicked() + " " + lnk.getReply());
            assert (2 == lnk.getReply());
        }
    }
}
