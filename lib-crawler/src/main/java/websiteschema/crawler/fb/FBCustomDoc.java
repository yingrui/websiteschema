/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import websiteschema.cluster.analyzer.Doc;
import websiteschema.element.DocumentUtil;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.utils.Escape;

/**
 *
 * @author ray
 */
@EO(name = {"EO"})
@EI(name = {"EI:CONVERT"})
public class FBCustomDoc extends FunctionBlock {

    @DI(name = "IN")
    public Doc doc = null;
    @DI(name = "MAP", desc = "标签名称的映射")
    public Map<String, String> map = null;
    @DI(name = "DEF", desc = "默认插入的数据")
    public Map<String, String> def = null;
    @DI(name = "ENCODE", desc = "需要Escape的标签")
    public List<String> encodeFields = null;
    @DO(name = "OUT", relativeEvents = {"EO"})
    public String out = "";

    @Algorithm(name = "CONVERT")
    public void convert() {
        try {
            Document d = convert(doc, map, def, encodeFields);
            out = DocumentUtil.getXMLString(d);
            triggerEvent("EO");
        } catch (Exception ex) {
            l.error(ex.getMessage(), ex);
            throw new RuntimeException("Doc不能转换成IDX, " + ex.getMessage());
        }
    }

    public Document convert(Doc doc, Map<String, String> map, Map<String, String> def, List<String> encodeFields) {
        if (null != doc) {
            if (null != def) {
                for (String key : def.keySet()) {
                    doc.addField(key, def.get(key));
                }
            }
            if (null != encodeFields) {
                for (String field : encodeFields) {
                    escape(doc, field);
                }
            }
            return doc.toW3CDocument(map);
        }
        return null;
    }

    private void escape(Doc doc, String field) {
        Collection<String> values = doc.getValues(field);
        if (null != values && !values.isEmpty()) {
            Collection<String> newValues = new ArrayList<String>();
            for (String value : values) {
                newValues.add(Escape.escape(value, "UTF-8"));
            }
            doc.remove(field);
            for (String value : newValues) {
                doc.addField(field, value);
            }
        }
    }
}
