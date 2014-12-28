/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.cluster.analyzer;

import java.util.List;
import java.util.Map;
import websiteschema.model.domain.cluster.ClusterModel;
import websiteschema.model.domain.cluster.Sample;

/**
 *
 * @author ray
 */
public interface ClusterAnalyzer {

    /**
     * 对聚类模型中的每一个cluster进行分析。
     * @param old
     * @param cm
     * @param samples
     * @return
     */
    public Map<String, String> analysis(Map<String, String> old, ClusterModel cm, List<Sample> samples);

    /**
     * 仅在调用analysis方法之后，才可获得BasicAnalysisResult。
     * @return
     */
    public BasicAnalysisResult getBasicAnalysisResult();

    /**
     * 设定
     * @param fieldAnalyzers - 由IFieldAnalyzer的类名组成的List
     */
    public void setFieldAnalyzers(List<IFieldAnalyzer> fieldAnalyzers);

}
