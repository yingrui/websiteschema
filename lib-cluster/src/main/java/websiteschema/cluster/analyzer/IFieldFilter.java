/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.cluster.analyzer;

import org.w3c.dom.Document;
import websiteschema.common.base.JObject;

/**
 *
 * @author ray
 */
public interface IFieldFilter extends JObject {

    /**
     * 指明分析的字段类型，例如分析时间字段就返回PUBLISHDATE。
     * @return
     */
    public String getFieldName();

    public void setFieldName(String fieldName);

    /**
     * 过滤数据，会删除、添加或修改字段
     * @param doc
     * @return
     */
    public void filtering(Doc doc);

}