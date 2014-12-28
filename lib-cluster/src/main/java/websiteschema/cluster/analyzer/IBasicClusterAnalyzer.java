/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer;

import java.util.List;
import websiteschema.model.domain.cluster.Cluster;
import websiteschema.model.domain.cluster.ClusterModel;
import websiteschema.model.domain.cluster.DocVector;
import websiteschema.model.domain.cluster.FeatureStatInfo;
import websiteschema.model.domain.cluster.Sample;

/**
 *
 * @author ray
 */
public interface IBasicClusterAnalyzer {

    /**
     * 查找每个类中的相同节点，识别其中的有效节点、无效节点、标题前缀和后缀。
     * @param clusters 经过聚类得到的结果
     * @param space 样本空间
     * @param samples 样本集合
     * @param statInfo 样本的统计信息
     * @return
     */
    public BasicAnalysisResult analysis(Cluster[] clusters, List<DocVector> space, List<Sample> samples, FeatureStatInfo statInfo);
}
