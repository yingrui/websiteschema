/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb.field;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import websiteschema.cluster.analyzer.Doc;
import websiteschema.fb.annotation.Description;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;

/**
 *
 * @author ray
 */
@EI(name = {"EI:CHANGE", "BAT:BATCH"})
@EO(name = {"EO", "BAT_OUT"})
@Description(desc = "从时间字段中提取日期字段")
public class FBTimeToDate extends FunctionBlock {

    @DI(name = "DOC", desc = "输入的Doc对象", relativeEvents = {"EI"})
    @DO(name = "DOC", desc = "输出的Doc对象", relativeEvents = {"EO"})
    public Doc doc;
    @DI(name = "DOCS", desc = "输入的Doc对象", relativeEvents = {"BAT"})
    @DO(name = "DOCS", desc = "输出的Doc对象", relativeEvents = {"BAT_OUT"})
    public List<Doc> docs = null;
    @DI(name = "DATE_TAG", desc = "日期标签", relativeEvents = {"EI"})
    public String tag = "STATDATE";
    @DI(name = "TIME_TAG", desc = "日期标签", relativeEvents = {"EI"})
    public String tagTime = "PUBLISHDATE";
    private static final Pattern pat = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2}).*");

    @Algorithm(name = "CHANGE", desc = "将时间转成日期")
    public void timeToDate() {
        timeToDate(doc, tag, tagTime);
        triggerEvent("EO");
    }

    @Algorithm(name = "BATCH", desc = "将时间转成日期")
    public void batch() {
        if (null != docs && !docs.isEmpty()) {
            for (Doc d : docs) {
                timeToDate(d, tag, tagTime);
            }
        }
        triggerEvent("BAT_OUT");
    }

    private void timeToDate(Doc doc, String tag, String tagTime) {
        String time = null;
        if (tagTime.contains("/")) {
            List<String> values = doc.getExtValue(tagTime);
            if (null != values && !values.isEmpty()) {
                time = values.get(0);
            }
        } else {
            time = doc.getValue(tagTime);
        }
        if (null != time) {
            Matcher m = pat.matcher(time);
            if (m.matches()) {
                String date = m.group(1) + "-" + m.group(2) + "-" + m.group(3);
                doc.remove(tag);
                doc.addField(tag, date);
            }
        }
    }
}
