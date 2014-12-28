/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer;

import java.util.Collection;
import java.util.Map;
import org.w3c.dom.Document;
import websiteschema.common.base.JObject;
import websiteschema.element.XPathAttributes;

/**
 *
 * @author ray
 */
public interface IFieldExtractor extends JObject {

    /**
     * 指明分析的字段类型，例如分析时间字段就返回PUBLISHDATE。
     * @return
     */
    public String getFieldName();

    public void setFieldName(String fieldName);

    /**
     * 抽取数据，结果可能是一个集合
     * @param doc
     * @return
     */
    public Collection<String> extract(Document doc, String pageSource);

    /**
     * 抽取数据，结果可能是一个集合
     * @param doc
     * @return
     */
    public Collection<Map<String, String>> extractExtData(Document doc, String pageSource);

    /**
     * 设置基本分析结果
     * @param basicAnalysisResult
     * @return
     */
    public void setBasicAnalysisResult(BasicAnalysisResult basicAnalysisResult);

    /**
     * 设置用来生成XPath的配置
     * @param xpathAttr
     */
    public void setXPathAttr(XPathAttributes xpathAttr);
}
