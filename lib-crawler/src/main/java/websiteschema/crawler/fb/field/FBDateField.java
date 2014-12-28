/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.crawler.fb.field;

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
@EI(name = {"EI:CHANGE"})
@EO(name = {"EO"})
@Description(desc = "修改日期字段")
public class FBDateField extends FunctionBlock {

    @DI(name = "DOC", desc = "输入的Doc对象", relativeEvents = {"EI"})
    @DO(name = "DOC", desc = "输出的Doc对象", relativeEvents = {"EO"})
    public Doc doc;
    @DI(name = "TAG", desc = "日期标签", relativeEvents = {"EI"})
    public String tag = "DATE";

    public String now = "";

    public String minAgo = "(\\d+)\\s*分钟前";
    public String hourAgo = "(\\d+)\\s*小时前";
    public String dateAgo = "(\\d+)\\s*天前";
    public String todayHM = "今天\\s*(\\d+):(\\d+)";

    @Algorithm(name = "CHANGE", desc = "")
    public void tagging() {
        
    }
}
