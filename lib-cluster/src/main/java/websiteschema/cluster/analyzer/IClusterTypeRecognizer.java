/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer;

import java.util.List;
import websiteschema.model.domain.cluster.Cluster;
import websiteschema.model.domain.cluster.DocVector;
import websiteschema.model.domain.cluster.FeatureStatInfo;
import websiteschema.model.domain.cluster.Sample;

/**
 *
 * @author ray
 */
public interface IClusterTypeRecognizer {

    public final static String TYPE_DOCUMENT = "DOCUMENT";
    public final static String TYPE_INVALID = "INVALID";
    public final static String TYPE_LINK = "LINKS";

    /**
     * 识别Cluster的类型
     * @param clusters
     * @param space
     * @param samples
     * @param statInfo
     */
    public void recognizeClusterType(Cluster[] clusters, List<DocVector> space, List<Sample> samples, FeatureStatInfo statInfo);

    public void setBasicAnalysisResult(BasicAnalysisResult analysisResult);
}
