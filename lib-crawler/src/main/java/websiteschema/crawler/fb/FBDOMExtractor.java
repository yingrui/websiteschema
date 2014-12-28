/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.util.ArrayList;
import java.util.Collection;
import websiteschema.cluster.analyzer.IFieldExtractor;
import websiteschema.cluster.analyzer.AnalysisResult;
import java.util.List;
import websiteschema.model.domain.Websiteschema;
import websiteschema.element.XPathAttributes;
import websiteschema.fb.annotation.DO;
import java.util.Map;
import org.w3c.dom.Document;
import websiteschema.cluster.analyzer.Doc;
import websiteschema.crawler.WebPage;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;

/**
 *
 * @author ray
 */
@EO(name = {"EO", "FATAL"})
@EI(name = {"EI:EXT"})
public class FBDOMExtractor extends FunctionBlock {

    private Map<String, String> prop;
    private XPathAttributes xpathAttr;
    private AnalysisResult analysisResult = new AnalysisResult();
    @DI(name = "IN")
    public Document in;
    @DI(name = "DOCS")
    public Document[] docs;
    @DI(name = "PAGE")
    public WebPage page;
    @DI(name = "SCHEMA")
    public Websiteschema schema;
    @DI(name = "CLS")
    public String clusterName = AnalysisResult.DefaultClusterName;
    @DI(name = "URL")
    public String url = null;
    @DI(name = "PAGING")
    public boolean paging = true;
    @DO(name = "OUT", relativeEvents = {"EO"})
    public Doc out;

    @Algorithm(name = "EXT")
    public void extract() {
        try {
            prop = schema.getProperties();
            xpathAttr = schema.getXpathAttr();
            analysisResult.init(prop);
            if (null != page) {
                out = getArticle(page);
            } else {
                //如果in为空，而docs又不为空，则in=docs[0]
                if (null == in && null != docs) {
                    in = docs[0];
                }
                //抽取标签
                out = extractFields(in, null, url, analysisResult.getFieldAnalyzers(), analysisResult.getFieldExtractors());
            }
            this.triggerEvent("EO");
        } catch (Exception ex) {
            l.error(ex);
            this.triggerEvent("FATAL");
        }
    }

    /**
     * 根据配置抽取每一个字段
     * @param in
     * @param fieldAnalyzerNames
     */
    private Doc extractFields(Document in, String pageSource, String url, Map<String, String> fieldAnalyzerNames, Map<String, String> fieldExtractorNames) {
        //初始化Document out
        Doc doc = createDocument(url);
        List<IFieldExtractor> list = new ArrayList<IFieldExtractor>();
        for (String fieldName : fieldAnalyzerNames.keySet()) {
            String clazzName = fieldAnalyzerNames.get(fieldName);
            IFieldExtractor extractor = createFieldExtractor(fieldName, clazzName);//创建字段抽取器
            if (null != extractor) {
                list.add(extractor);
            }
        }
        for (String fieldName : fieldExtractorNames.keySet()) {
            String clazzName = fieldExtractorNames.get(fieldName);
            IFieldExtractor extractor = createFieldExtractor(fieldName, clazzName);//创建字段抽取器
            if (null != extractor) {
                list.add(extractor);
            }
        }
        extractFields(in, pageSource, doc, list);
        return doc;
    }

    private void extractFields(Document in, String pageSource, Doc doc, List<IFieldExtractor> fields) {
        for (IFieldExtractor extractor : fields) {
            //创建字段抽取器
            extractor.setXPathAttr(xpathAttr);
            extractor.setBasicAnalysisResult(analysisResult.getBasicAnalysisResult());
            try {
                //读取字段抽取器的配置，这是一个List<Map>的配置
                List<Map<String, String>> listConfig = analysisResult.getListByField(clusterName, extractor.getFieldName());
                if (null != listConfig) {
                    for (Map<String, String> config : listConfig) {
                        List<String> properClusters = analysisResult.getProperCluster(config);
                        //对每一个配置都尝试抽取，如果配置和类的名称相符
                        if (properClusters.contains(clusterName)) {
                            extractor.init(config);
                            //开始抽取
                            Collection<String> result = extractor.extract(in, pageSource);
                            if (null != result && !result.isEmpty()) {
                                //添加抽取结果到doc中
                                for (String res : result) {
                                    doc.addField(extractor.getFieldName(), res);
                                }
                                //如果抽取到结果，就结束抽取
                                break;
                            }
                            //抽取扩展数据
                            Collection<Map<String, String>> extResult = extractor.extractExtData(in, pageSource);
                            if (null != extResult && !extResult.isEmpty()) {
                                //添加抽取结果到doc中
                                for (Map<String, String> res : extResult) {
                                    doc.addExtField(extractor.getFieldName(), res);
                                }
                                //如果抽取到结果，就结束抽取
                                break;
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private Doc getArticle(WebPage page) {
        Doc ret = extractFields(page.getDocs()[0], page.getHtmlSource()[0], page.getUrl(), analysisResult.getFieldAnalyzers(), analysisResult.getFieldExtractors());
        while (paging && page.hasNext()) {
            WebPage next = page.getNext();
            if (null != next) {
                Doc oneDoc = extractFields(next.getDocs()[0], page.getHtmlSource()[0], next.getUrl(), analysisResult.getFieldAnalyzers(), analysisResult.getFieldExtractors());
                //对某些参数进行过滤
                ret.addField(Doc.CONTENT_FIELD, oneDoc.getValue(Doc.CONTENT_FIELD));
            } else {
                break;
            }
        }
        return ret;
    }

    private IFieldExtractor createFieldExtractor(String fieldName, String clazzName) {
        try {
            Class clazz = Class.forName(clazzName);
            IFieldExtractor extractor = (IFieldExtractor) clazz.newInstance();
            extractor.setFieldName(fieldName);
            extractor.setXPathAttr(xpathAttr);
            extractor.setBasicAnalysisResult(analysisResult.getBasicAnalysisResult());
            return extractor;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Doc createDocument(String url) {
        Doc doc = new Doc();
        if (null != url) {
            doc.addField("URL", url);
        }
        return doc;
    }
}
