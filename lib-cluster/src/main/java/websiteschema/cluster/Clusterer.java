/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster;

import java.util.*;
import websiteschema.model.domain.cluster.*;

/**
 *
 * @author ray
 */
public abstract class Clusterer {

    Cluster[] clusters = null;
    List<Sample> samples = new ArrayList<Sample>();
    FeatureStatInfo statInfo;
    String siteId = "";

    public Clusterer(String siteId) {
        this.siteId = siteId;
    }

    public abstract ClusterModel clustering();

    public abstract Cluster classify(Sample sample);

    public abstract double membershipDegree(DocVector v1, DocVector v2);

    public void appendCluster(List<Cluster> list) {
        if (null != clusters) {
            int pos = clusters.length;
            Cluster[] tmp = new Cluster[pos + list.size()];
            System.arraycopy(this.clusters, 0, tmp, 0, pos);
            for (int i = 0; i < list.size(); i++) {
                tmp[pos + i] = list.get(i);
            }
            this.clusters = tmp;
        } else {
            Cluster[] tmp = new Cluster[list.size()];
            for (int i = 0; i < list.size(); i++) {
                tmp[i] = list.get(i);
            }
            this.clusters = tmp;
        }
    }

    public void appendSample(List<Sample> list) {
        this.samples.addAll(list);
    }

    public void statFeature() {
        statInfo = featureInfoStatistic(samples);
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public void init(ClusterModel cm) {
        this.statInfo = cm.getStatInfo();
        clusters = cm.getClusters();
    }

    private FeatureStatInfo featureInfoStatistic(List<Sample> samples) {
        FeatureStatHelper statHelper = new FeatureStatHelper();
        for (Sample sample : samples) {
            //列出每个样本
            if (0 != sample.getHttpStatus()) {
                DocUnits units = sample.getContent();
                Unit array[] = units.getUnits();
                if (null != array) {
                    List<Feature> list = statisticeSingleSample(array);
                    for (Feature f : list) {
                        FeatureInfo feature = statHelper.getDim(f.xpath);
                        int oldWeight = feature.getWeight() * feature.getDocumentFrequence();
                        feature.setTotalCount(feature.getTotalCount() + f.count);
                        feature.setDocumentFrequence(feature.getDocumentFrequence() + 1);
                        feature.setFrequence(feature.getTotalCount() / feature.getDocumentFrequence());
                        feature.setWeight((oldWeight + f.weight) / feature.getDocumentFrequence());
//                        if (f.xpath.equals("HTML/BODY/TABLE/TBODY/TR/TD[@id='mainBody']/TABLE/TBODY/TR/TD/BLOCKQUOTE/P")) {
//                            System.out.println("ID" + statHelper.getDimId(f.xpath) + "----------- Weight " + feature.getWeight());
//                        }
                    }
                }
            }
        }
        return statHelper.getFinalResults();
    }

    class Feature {

        String xpath;
        int count = 0;
        int weight = 0;
    }

    private List<Feature> statisticeSingleSample(Unit array[]) {
        Map<String, Feature> map = new LinkedHashMap<String, Feature>();
        for (Unit unit : array) {
            //对每个样本中的每一个元素都进行统计
            String xpath = unit.getXpath();
            String text = unit.getText();
            //忽略SCRIPT和STYLE两种标签
            if (!xpath.endsWith("SCRIPT") && !xpath.endsWith("STYLE")) {
                Feature f = null;
                if (map.containsKey(xpath)) {
                    f = map.get(xpath);
                } else {
                    f = new Feature();
                    f.xpath = xpath;
                    map.put(xpath, f);
                }
                f.count++;
                f.weight += null != text ? text.trim().length() : 0;
            }
        }

        List<Feature> ret = new ArrayList<Feature>();
        for (String xpath : map.keySet()) {
            ret.add(map.get(xpath));
        }
        map = null;
        return ret;
    }
}
