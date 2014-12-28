/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer;

import java.util.List;
import java.util.Map;
import websiteschema.model.domain.cluster.Cluster;
import websiteschema.model.domain.cluster.FeatureStatInfo;
import websiteschema.model.domain.cluster.Sample;

/**
 * 
 * @author ray
 */
public interface IFieldAnalyzer extends IFieldExtractor {

    /**
     * 返回适合的ClusterType，例如{"DOCUMENT"}，表明只能针对内容性文档进行分析
     * @return
     */
    public String[] getProperClusterType();

    /**
     * 在特定的cluster中，针对某一字段进行分析。
     * @param cluster - 用于分析的对象
     * @param resultAnalysis - ClusterAnalyzer的基本分析结果，主要是指一些无效节点、有效节点等
     * @param samples - cluster中的样本
     * @return 分析结果
     */
    public Map<String, String> analyze(Cluster cluster, FeatureStatInfo statInfo, List<Sample> samples);
}
