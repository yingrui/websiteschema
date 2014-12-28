/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer.fields;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import websiteschema.cluster.analyzer.AnalyzerUtil;
import websiteschema.cluster.analyzer.BasicAnalysisResult;
import websiteschema.cluster.analyzer.IFieldAnalyzer;
import websiteschema.element.DocumentUtil;
import websiteschema.model.domain.cluster.Cluster;
import websiteschema.model.domain.cluster.FeatureInfo;
import websiteschema.model.domain.cluster.FeatureStatInfo;
import websiteschema.model.domain.cluster.Sample;
import websiteschema.utils.EditDistance;
import websiteschema.utils.StringUtil;

/**
 *
 * @author ray
 */
public class TitleAnalyzer extends AbstractFieldExtractor implements IFieldAnalyzer {

    private String titleXPath = "";
    private String titlePrefixString = "";
    private String titleSuffixString = "";
    public final static String xpathKey = "XPath";
    public final static String prefixKey = "PrefixString";
    public final static String suffixKey = "SuffixString";

    public TitleAnalyzer() {
        setFieldName("TITLE");
    }

    public String[] getProperClusterType() {
        return new String[]{"DOCUMENT"};
    }

    public void init(Map<String, String> params) {
        titleXPath = params.containsKey(xpathKey) ? params.get(xpathKey) : "";
        titlePrefixString = params.containsKey(prefixKey) ? params.get(prefixKey) : "";
        titleSuffixString = params.containsKey(suffixKey) ? params.get(suffixKey) : "";
    }

    public Map<String, String> analyze(Cluster cluster, FeatureStatInfo statInfo, List<Sample> samples) {
        Set<String> validNodes = getBasicAnalysisResult().getValidNodes();
        AnalyzerUtil util = AnalyzerUtil.getInstance();
        Map<String, String> titleText = util.getText("html/head/title", samples);
        //将每篇文章的标题进行一次过滤
        for (String key : titleText.keySet()) {
            String title = titleText.get(key);
            title = trimTitle(title, getBasicAnalysisResult());
            titleText.put(key, title);
        }
        double sim = 0.4;
        String xpath = "";
        EditDistance ed = new EditDistance();
        for (String node : validNodes) {
            FeatureInfo info = statInfo.getFeatureInfo(node);
            //如果标签的出现频率大于2，则不可能是标题。
            //如果标签的包含文本的长度，平均大于200，则不可能是标题。
            if (info.getFrequence() <= 2 && info.getWeight() < 200) {
                Map<String, String> nodeText = util.getText(node, samples);
                double total = 0.0;
                double count = 0;
                for (String sample : nodeText.keySet()) {
                    String sampleText = nodeText.get(sample);
                    String title = titleText.get(sample);
                    if (null != sampleText && null != title && sampleText.length() < 200) {
                        total += ed.caculateSimilarityBetweenStrings(sampleText, title);
                        count += 1;
                    }
                }
                double arvgSim = count > 0 ? total / count : 0.000001;
                if (arvgSim > sim) {
                    sim = arvgSim;
                    xpath = node;
                }
            }
        }

        if (!"".equals(xpath)) {
            this.titleXPath = xpath;
        }

        return createResult();
    }

    private String trimTitle(String title, BasicAnalysisResult analysisResult) {
        Set<String> titlePrefix = analysisResult.getTitlePrefix();
        Set<String> titleSuffix = analysisResult.getTitleSuffix();

        for (String prefix : titlePrefix) {
            if (title.startsWith(prefix)) {
                title = title.substring(prefix.length());
                break;
            }
        }

        for (String suffix : titleSuffix) {
            if (title.endsWith(suffix)) {
                title = title.substring(0, title.length() - suffix.length());
                break;
            }
        }

        return title;
    }

    private Map<String, String> createResult() {
        Map<String, String> ret = new HashMap<String, String>();

        if (null != titleXPath && !"".equals(titleXPath)) {
            ret.put(xpathKey, titleXPath);
        }

        if (null != titlePrefixString && !"".equals(titlePrefixString)) {
            if (null != titleSuffixString && !"".equals(titleSuffixString)) {
                ret.put(prefixKey, titlePrefixString);
                ret.put(suffixKey, titleSuffixString);
            }
        }
        return ret;
    }

    public Collection<String> extract(Document doc, String pageSource) {
        if (!"".equals(titleXPath)) {
            return getTitle(titleXPath, doc, titlePrefixString, titleSuffixString);
        } else if (!"".equals(titlePrefixString) && !"".equals(titleSuffixString)) {
            return getTitle(titlePrefixString, titleSuffixString, doc);
        }

        return null;
    }

    private Set<String> getTitle(String xpath, Document doc, String prefix, String suffix) {
        Set<String> ret = new HashSet<String>();
        List<Node> nodes = DocumentUtil.getByXPath(doc, xpath);
        for (Node node : nodes) {
            StringBuilder sb = new StringBuilder();
            ExtractUtil.getInstance().extractNodeText(node, sb);
            String res = sb.toString();
            if (null != res) {
                res = StringUtil.trim(res);

                if (StringUtil.isNotEmpty(prefix) && res.contains(prefix)) {
                    res = res.substring(res.indexOf(res) + prefix.length());
                }

                if (StringUtil.isNotEmpty(suffix) && res.contains(suffix)) {
                    res = res.substring(0, res.indexOf(suffix));
                }

                if (StringUtil.isNotEmpty(res) && !ret.contains(res) && ret.size() < 2) {
                    //仅允许正副两个标题。
                    ret.add(res);
                }
            }
        }
        return ret;
    }

    private Set<String> getTitle(String prefix, String suffix, Document doc) {
        Set<String> ret = new HashSet<String>();

        String text = DocumentUtil.getXMLString(doc);
        int start = text.indexOf(prefix);
        if (start >= 0) {
            int end = text.indexOf(suffix, start + prefix.length());
            if (end > 0) {
                String title = text.substring(start + prefix.length(), end);
                if (null != title && !"".equals(title)) {
                    ret.add(title);
                }
            }
        }
        return ret;
    }
}
