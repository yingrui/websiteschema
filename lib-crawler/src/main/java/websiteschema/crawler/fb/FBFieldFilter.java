/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.util.*;
import websiteschema.cluster.analyzer.Doc;
import websiteschema.cluster.analyzer.IFieldFilter;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.Description;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;

/**
 *
 * @author ray
 */
@EI(name = {"EI:FILTERING"})
@EO(name = {"EO"})
@Description(desc = "检查文档中的字段是否符合规范，如果不符合，则需要裁减")
public class FBFieldFilter extends FunctionBlock {

    @DI(name = "FILTER", desc = "抽取后的内容")
    public Map<String, String> filters = null;
    @DI(name = "DOC", desc = "抽取后的内容")
    @DO(name = "DOC", relativeEvents = {"EO"})
    public Doc doc = null;

    @Algorithm(name = "FILTERING")
    public void filtering() {
        try {
            if (null != filters && null != doc) {
                for (String field : filters.keySet()) {
                    IFieldFilter filter = createFieldFilter(field, filters.get(field));
                    if (null != filter) {
                        filter.filtering(doc);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.triggerEvent("EO");
    }

    private IFieldFilter createFieldFilter(String field, String clazzName) {
        try {
            Class clazz = Class.forName(clazzName);
            IFieldFilter filter = (IFieldFilter) clazz.newInstance();
            filter.setFieldName(field);
            return filter;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
