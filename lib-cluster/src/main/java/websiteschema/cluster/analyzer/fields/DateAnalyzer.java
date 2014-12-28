/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer.fields;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import websiteschema.cluster.analyzer.AnalyzerUtil;
import websiteschema.cluster.analyzer.IFieldAnalyzer;
import websiteschema.utils.CollectionUtil;
import websiteschema.element.DocumentUtil;
import websiteschema.model.domain.cluster.Cluster;
import websiteschema.model.domain.cluster.DocVector;
import websiteschema.model.domain.cluster.FeatureInfo;
import websiteschema.model.domain.cluster.FeatureStatInfo;
import websiteschema.model.domain.cluster.Sample;
import websiteschema.utils.EditDistance;

/**
 *
 * @author ray
 */
public class DateAnalyzer extends AbstractFieldExtractor implements IFieldAnalyzer {

    private String xpath = "";
    private String pattern = "";
    private String format = "yyyy-MM-dd";
    public final static String xpathKey = "XPath";
    public final static String patternKey = "Pattern";
    public final static String formatKey = "Format";
    public final static String typeKey = "Type";
    public final static String Type_XPath = "XPath";
    public final static String Type_URL = "URL";
    public final static String Type_HTTP_Response = "HTTP";

    class Feature {

        boolean hasYMD = false;
        boolean hasSourceOrAuthor = false;
        boolean hasPublishDate = false;
        boolean hasHMS = false;
        boolean hasMultiYMD = false;
        boolean hasWeather = false;
        boolean hasWeek = false;
    }

    public DateAnalyzer() {
        setFieldName("DATE");
    }

    public String[] getProperClusterType() {
        return new String[]{"DOCUMENT"};
    }

    public Map<String, String> analyze(Cluster cluster, FeatureStatInfo statInfo, List<Sample> samples) {
        if (samples.size() > 0) {
            AnalyzerUtil util = AnalyzerUtil.getInstance();
            List<DocVector> vectors = AnalyzerUtil.getInstance().convertSamples(samples, statInfo);
            Set<String> commonNodes = AnalyzerUtil.getInstance().findCommonNodes(vectors, statInfo);
            double maxScore = 0.2;
            Map<String, Double> propNodes = new HashMap<String, Double>();
            for (String node : commonNodes) {
                FeatureInfo info = statInfo.getFeatureInfo(node);
                // 如果标签的出现频率大于3，则不可能是时间。
                // 如果标签的包含文本的长度，平均大于100，则不可能是时间。
                if (info.getFrequence() > 0 && info.getFrequence() <= 3 && info.getWeight() < 100) {
                    Map<String, String> nodeTexts = util.getText(node, samples);
                    Feature f = findFeature(nodeTexts);
                    double score = decision(f);
                    if (score > maxScore) {
                        maxScore = score;
                        propNodes.put(node, maxScore);
                    }
                }
            }
            if (!propNodes.isEmpty()) {
                //选出最好的两个备选
                List<Entry<String, Double>> sorted = CollectionUtil.sortMapDesc(propNodes);
                if (propNodes.size() > 1) {
                    Entry<String, Double> xpath1 = sorted.get(0);
                    Entry<String, Double> xpath2 = sorted.get(1);
                    if (xpath1.getValue() - xpath2.getValue() < 0.05001) {
                        EditDistance ld = new EditDistance();
                        Map<String, String> nodeTexts1 = util.getText(xpath1.getKey(), samples);
                        Map<String, String> nodeTexts2 = util.getText(xpath2.getKey(), samples);
                        double sim1 = ld.caculateSimilarity(nodeTexts1.keySet());
                        double sim2 = ld.caculateSimilarity(nodeTexts2.keySet());
                        // 选择一个相似度较低的节点作为日期节点
                        if (sim1 > sim2) {
                            this.xpath = xpath1.getKey();
                        } else {
                            this.xpath = xpath2.getKey();
                        }
                    } else {
                        this.xpath = xpath1.getKey();
                    }
                } else {
                    this.xpath = sorted.get(0).getKey();
                }
                Map<String, String> nodeTexts = util.getText(xpath, samples);
                analyzePattern(nodeTexts);
            }
        }

        return createResult();
    }

    private void analyzePattern(Map<String, String> nodeTexts) {
        List<String> list = new ArrayList<String>();
        for (String key : nodeTexts.keySet()) {
            list.add(nodeTexts.get(key));
        }
        String text = list.get(0);
        this.pattern = DateDetectUtil.getInstance().detectPattern(text);
        if (null != pattern) {
            if (pattern.contains("HH")) {
                this.format = "yyyy-MM-dd HH";
                if (pattern.contains("mm")) {
                    format += ":mm";
                    if (pattern.contains("SS")) {
                        format += ":SS";
                    }
                }
            }
        }
    }

    private double decision(Feature f) {
        double w[] = {0.3, 0.25, 0.25, 0.15, 0.05};
        double ret = 0.0;
        if (f.hasMultiYMD || f.hasWeather) {
            return 0.0;
        }
        ret = w[0] * (f.hasYMD ? 1 : 0)
                + w[1] * (f.hasSourceOrAuthor ? 1 : 0)
                + w[2] * (f.hasPublishDate ? 1 : 0)
                + w[3] * (f.hasHMS ? 1 : 0)
                + w[4] * (f.hasWeek ? 1 : 0);
        return ret;
    }

    private Feature findFeature(Map<String, String> nodeTexts) {
        Feature f = new Feature();
        boolean hasYMD = true;
        boolean hasSourceOrAuthor = false;
        boolean hasPublishDate = true;
        boolean hasHMS = true;
        boolean hasMultiYMD = true;
        boolean hasWeather = true;
        boolean hasWeek = true;
        int sampleCount = nodeTexts.size();
        int countSourceOrAuthor = 0;
        for (String sample : nodeTexts.keySet()) {
            String text = StringEscapeUtils.unescapeHtml(nodeTexts.get(sample));
            //如果有一个样本中不包含年月日，则hasYMD为false。
            if (!DateDetectUtil.getInstance().hasYMD(text)) {
                hasYMD = false;
            }
            //检查是否大多数样本中都包含**报，**网
            if (DateDetectUtil.getInstance().hasSourceOrAuthor(text)) {
                countSourceOrAuthor++;
            }
            //检查是否包含字符发布时间
            if (!DateDetectUtil.getInstance().hasPublishDate(text)) {
                hasPublishDate = false;
            }
            //检查是否包含时分秒
            if (!DateDetectUtil.getInstance().hasHMS(text)) {
                hasHMS = false;
            }
            //检查是否包含多个日期
            if (!DateDetectUtil.getInstance().hasMultiYMD(text)) {
                hasMultiYMD = false;
            }
            //检查是否包含天气
            if (!DateDetectUtil.getInstance().hasWeather(text)) {
                hasWeather = false;
            }
            //检查是否包含天气
            if (!DateDetectUtil.getInstance().hasWeek(text)) {
                hasWeek = false;
            }
        }


        double d = (double) countSourceOrAuthor / (double) sampleCount;
        if (d >= 0.5) {
            hasSourceOrAuthor = true;
        }

        f.hasYMD = hasYMD;
        f.hasSourceOrAuthor = hasSourceOrAuthor;
        f.hasPublishDate = hasPublishDate;
        f.hasHMS = hasHMS;
        f.hasMultiYMD = hasMultiYMD;
        f.hasWeather = hasWeather;
        f.hasWeek = hasWeek;
        return f;
    }

    private Map<String, String> createResult() {
        Map<String, String> ret = new HashMap<String, String>();
        if (null != xpath && !"".equals(xpath)) {
            ret.put(xpathKey, xpath);
            if (null != pattern && !"".equals(pattern)) {
                ret.put(patternKey, pattern);
                if (null != format && !"".equals(format)) {
                    ret.put(formatKey, format);
                }
            }
        }
        return ret;
    }

    public Collection<String> extract(Document doc, String pageSource) {
        Set<String> ret = new HashSet<String>();
        List<Node> nodes = DocumentUtil.getByXPath(doc, xpath);
        if (null != nodes) {
            for (Node node : nodes) {
                String text = ExtractUtil.getInstance().getNodeText(node);
                if (null != text && !"".equals(text)) {
                    //对多行的文本，则先将文本中的换行符替换，因为正则表达式支持多行比较麻烦
                    text = text.trim().replaceAll("[\r\n]+", " ");
                    String date = DateDetectUtil.getInstance().parseDate(text, pattern, format);
                    if (null != date && !"".equals(date)) {
                        ret.add(date);
                    }
                }
            }
        }
        return ret;
    }

    public void init(Map<String, String> params) {
        xpath = params.containsKey(xpathKey) ? params.get(xpathKey) : "";
        pattern = params.containsKey(patternKey) ? params.get(patternKey) : "";
        format = params.containsKey(formatKey) ? params.get(formatKey) : "";
    }
}
