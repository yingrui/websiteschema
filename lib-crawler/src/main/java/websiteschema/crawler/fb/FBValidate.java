/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import websiteschema.cluster.analyzer.Doc;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.Description;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.utils.StringUtil;

/**
 *
 * @author ray
 */
@EI(name = {"EI:VALID", "BAT:BAT"})
@EO(name = {"YES", "NO", "BAT_OUT"})
@Description(desc = "检查文档中是否包含必要的字段")
public class FBValidate extends FunctionBlock {

    @DI(name = "MUSTHAVE", desc = "抽取后的内容")
    public List<String> listNotEmpty = null;
    @DI(name = "DOC", desc = "抽取后的内容")
    public Doc doc = null;
    @DI(name = "DOCS", desc = "抽取后的内容")
    @DO(name = "DOCS", relativeEvents = {"BAT_OUT"})
    public List<Doc> docs = null;
    @DO(name = "REASON", relativeEvents = {"NO"})
    public String reason = "";

    @Algorithm(name = "VALID", desc = "检查是否包含必须的字段")
    public void validateEmpty() {
        if (isValid(doc)) {
            triggerEvent("YES");
        } else {
            triggerEvent("NO");
        }
    }

    @Algorithm(name = "BAT", desc = "检查是否包含必须的字段")
    public void batchValidateEmpty() {
        if (null != docs && !docs.isEmpty()) {
            Iterator<Doc> it = docs.iterator();
            while (it.hasNext()) {
                Doc d = it.next();
                if (!isValid(d)) {
                    it.remove();
                }
            }
        }
        triggerEvent("BAT_OUT");
    }

    boolean isValid(Doc doc) {
        boolean valid = true;
        if (null != doc && null != listNotEmpty) {
            Set<String> fields = new HashSet<String>(listNotEmpty);
            for (String field : listNotEmpty) {
                Collection<String> values = doc.getValues(field);
                if(null != values && !values.isEmpty()) {
                    for(String value : values) {
                        if(StringUtil.isNotEmpty(value)) {
                            fields.remove(field);
                            break;
                        }
                    }
                }
            }
            if (valid && !fields.isEmpty()) {
                valid = false;
                reason = fields.toString() + " were not existed";
            }
        } else {
            valid = false;
        }
        return valid;
    }
}
