/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer.fields;

import java.util.Collection;
import java.util.Map;
import org.w3c.dom.Document;
import websiteschema.cluster.analyzer.BasicAnalysisResult;
import websiteschema.cluster.analyzer.IFieldExtractor;
import websiteschema.element.XPathAttributes;

/**
 *
 * @author ray
 */
public abstract class AbstractFieldExtractor implements IFieldExtractor {

    private String fieldName;
    private BasicAnalysisResult basicAnalysisResult;
    private XPathAttributes xPathAttr;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setBasicAnalysisResult(BasicAnalysisResult basicAnalysisResult) {
        this.basicAnalysisResult = basicAnalysisResult;
    }

    public BasicAnalysisResult getBasicAnalysisResult() {
        return basicAnalysisResult;
    }

    public void setXPathAttr(XPathAttributes xpathAttr) {
        this.xPathAttr = xpathAttr;
    }

    public XPathAttributes getXPathAttr() {
        return this.xPathAttr;
    }

    public Collection<Map<String, String>> extractExtData(Document doc, String pageSource) {
        return null;
    }
}
