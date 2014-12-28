/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import websiteschema.cluster.DocVectorConvertor;
import websiteschema.model.domain.cluster.Dimension;
import websiteschema.model.domain.cluster.DocVector;
import websiteschema.model.domain.cluster.FeatureInfo;
import websiteschema.model.domain.cluster.FeatureStatInfo;
import websiteschema.model.domain.cluster.Sample;
import websiteschema.model.domain.cluster.Unit;
import websiteschema.utils.EditDistance;

/**
 *
 * @author ray
 */
public class AnalyzerUtil {

    private static AnalyzerUtil ins = new AnalyzerUtil();

    public static AnalyzerUtil getInstance() {
        return ins;
    }
    EditDistance ld = new EditDistance();

    public List<DocVector> convertSamples(List<Sample> samples, FeatureStatInfo statInfo) {
        List<DocVector> space = new ArrayList<DocVector>();
        DocVectorConvertor convertor = new DocVectorConvertor();

        for (Sample sample : samples) {
            DocVector v = convertor.convert(sample, statInfo);
            space.add(v);
        }

        return space;
    }

    public Set<String> findCommonNodes(List<DocVector> samples, FeatureStatInfo statInfo) {
        Set<String> ret = new HashSet<String>();
        FeatureInfo features[] = statInfo.getList();
        for (FeatureInfo f : features) {
            ret.add(f.getName());
        }
        for (int i = 0; i < samples.size(); i++) {
            DocVector vi = samples.get(i);
            Dimension[] dims = vi.getDims();
            Set<String> set = new HashSet<String>();
            for (Dimension dim : dims) {
                String name = features[dim.getId()].getName();
                if (ret.contains(name)) {
                    set.add(name);
                }
            }
            ret = set;
        }
        return ret;
    }

    public Set<String> findInvalidNodes(List<Sample> samples, Set<String> commonNodes, double th) {
        Set<String> ret = new HashSet<String>();
        for (String xpath : commonNodes) {
            Map<String, String> mapText = getText(xpath, samples);
            List<String> listText = new ArrayList<String>();
            for (String key : mapText.keySet()) {
                String text = mapText.get(key);
                text = null != text ? text : "";
                text = text.length() > 100 ? text.substring(0, 100) : text;
                listText.add(text);
            }
            double sim = ld.caculateSimilarity(listText);
            if (sim >= th) {
                ret.add(xpath);
            }
        }
        return ret;
    }

    /**
     * 获取指定xpath在样本中包含的文本，主要用来计算相似度，或查找相同的前缀和后缀。
     * @param xpath
     * @param samples
     * @return
     */
    public Map<String, String> getText(String xpath, List<Sample> samples) {
        Map<String, String> ret = new HashMap<String, String>();
        for (Sample sample : samples) {
            Unit[] units = sample.getUnits(xpath);
            StringBuilder sb = new StringBuilder();
            if (null != units) {
                for (Unit u : units) {
                    String text = u.getText();
                    if (null != text) {
                        sb.append(text.trim());
                    }
                }
            }
            ret.put(sample.getRowKey(), sb.toString());
        }
        return ret;
    }

    /**
     * 找到同组文档的标题，检查是否有相同的前缀和后缀
     * @param titlePrefix
     * @param titleSuffix
     * @param samples
     */
    public void findTitlePrefixAndSuffix(Set<String> titlePrefix, Set<String> titleSuffix, List<Sample> samples) {
        Map<String, String> mapText = getText("html/head/title", samples);
        List<String> listText = new ArrayList<String>();
        for (String key : mapText.keySet()) {
            String text = mapText.get(key);
            if (!"".equals(text)) {
                listText.add(text);
            }
        }
        if (listText.size() > 1) {
            String prefix = listText.get(0);
            String suffix = listText.get(0);
            for (int i = 1; i < listText.size(); i++) {
                String title = listText.get(i);
                prefix = findPrefix(title, prefix);
                suffix = findSuffix(title, suffix);
            }
            if (null != prefix && !"".equals(prefix)) {
                //必须要有标点符号结尾
                if (prefix.trim().matches(".*[\\p{Punct}]")) {
                    titlePrefix.add(prefix);
                }
            }
            if (null != suffix && !"".equals(suffix)) {
                //必须要有标点符号开头
                if (suffix.trim().matches("[\\p{Punct}].*")) {
                    titleSuffix.add(suffix);
                }
            }
        }
    }

    private String findPrefix(String str, String prefix) {
        String ret = prefix;
        while (!str.startsWith(ret)) {
            ret = ret.substring(0, ret.length() - 1);
            if (ret.length() <= 0) {
                break;
            }
        }
        return ret;
    }

    private String findSuffix(String str, String suffix) {
        String ret = suffix;
        while (!str.endsWith(ret)) {
            ret = ret.substring(1, ret.length());
            if (ret.length() <= 0) {
                break;
            }
        }
        return ret;
    }

    public List<Sample> getSamples(List<String> rowKeys, List<Sample> samples) {
        List<Sample> ret = new ArrayList<Sample>();
        Set<String> setRowKeys = new HashSet<String>(rowKeys);
        for (Sample vect : samples) {
            if (setRowKeys.contains(vect.getRowKey())) {
                ret.add(vect);
            }
        }
        return ret;
    }

    public List<DocVector> getVectors(List<String> rowKeys, List<DocVector> samples) {
        List<DocVector> ret = new ArrayList<DocVector>();
        Set<String> setRowKeys = new HashSet<String>(rowKeys);
        for (DocVector vect : samples) {
            if (null != vect) {
                if (setRowKeys.contains(vect.getName())) {
                    ret.add(vect);
                }
            }
        }
        return ret;
    }
}
