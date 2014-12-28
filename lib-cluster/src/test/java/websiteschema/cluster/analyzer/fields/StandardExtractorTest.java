/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer.fields;

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import websiteschema.utils.StringUtil;

/**
 *
 * @author ray
 */
public class StandardExtractorTest {

    @Test
    public void shouldFilterPrefixAndSuffix_for_Author() {
        StandardExtractor extractor = new StandardExtractor();
        Map<String, String> param = new HashMap<String, String>();
        param.put(StandardExtractor.prefixKey, "作者：");
        extractor.init(param);

        String res = "来源：资本证券网 作者：张三";
        res = extractor.filterByPattern(res);
        Assert.assertEquals(res, "张三");
    }

    @Test
    public void shouldFilterPrefixAndSuffix_for_Source() {
        StandardExtractor extractor = new StandardExtractor();
        Map<String, String> param = new HashMap<String, String>();
        param.put(StandardExtractor.prefixKey, "来源：");
        param.put(StandardExtractor.suffixKey, "作者：");
        extractor.init(param);

        String res = "来源：资本证券网 作者：张三";
        res = StringUtil.trim(extractor.filterByPattern(res));
        Assert.assertEquals(res, "资本证券网");
    }
}
