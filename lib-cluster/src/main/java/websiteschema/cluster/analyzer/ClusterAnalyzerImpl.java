/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import websiteschema.model.domain.cluster.Cluster;
import websiteschema.model.domain.cluster.ClusterModel;
import websiteschema.model.domain.cluster.DocVector;
import websiteschema.model.domain.cluster.FeatureStatInfo;
import websiteschema.model.domain.cluster.Sample;

/**
 *
 * @author ray
 */
public class ClusterAnalyzerImpl implements ClusterAnalyzer {

    AnalysisResult analysisResult = new AnalysisResult();
    BasicAnalysisResult result = null;
    AnalyzerUtil analyzer = AnalyzerUtil.getInstance();
    IClusterTypeRecognizer clusterTypeRecognizer = new SimpleClusterTypeRecognizer();
    IBasicClusterAnalyzer simpleAnalyzer = new SimpleBasicClusterAnalyzer();
    List<IFieldAnalyzer> fieldAnalyzers = null;

    //对聚类结果进行基本分析
    public Map<String, String> analysis(Map<String, String> old, ClusterModel cm, List<Sample> samples) {
        if (null == cm || null == samples) {
            return old;
        }

        Cluster[] clusters = cm.getClusters();
        FeatureStatInfo statInfo = cm.getStatInfo();
        if (null != clusters) {
            List<DocVector> space = analyzer.convertSamples(samples, statInfo);

            //查找每个类中的相同节点，识别其中的有效节点、无效节点、标题前缀和后缀。
            findBasicParameter(clusters, space, samples, statInfo);
            //确定每个类的类型
            confirmClusterType(clusters, space, samples, statInfo);
            //对每个类进行分析，挖掘各个字段的配置，例如标题、作者等
            analyzeFields(clusters, samples, statInfo);
        }

        //整理结果并返回
        return createResult(old);
    }

    private void findBasicParameter(Cluster[] clusters, List<DocVector> space, List<Sample> samples, FeatureStatInfo statInfo) {
        result = simpleAnalyzer.analysis(clusters, space, samples, statInfo);
        analysisResult.setBasicAnalysisResult(result);
    }

    private Map<String, String> createResult(Map<String, String> old) {
        return analysisResult.getResult(old);
    }

    private void analyzeFields(Cluster[] clusters, List<Sample> samples, FeatureStatInfo statInfo) {
        if (null != fieldAnalyzers) {
            for (Cluster cluster : clusters) {
                List<String> allSamples = cluster.getSamples();
                List<String> rowKeys = new ArrayList<String>();
                int count = allSamples.size() > 10 ? 10 : allSamples.size();
                for (int i = 0; i < count; i++) {
                    rowKeys.add(allSamples.get(i));
                }
                //每个类在进行分析的时候，只分析十个样本
                List<Sample> clusterSamples = analyzer.getSamples(rowKeys, samples);
                analyzeEachCluster(cluster, statInfo, clusterSamples);
            }
        }
    }

    /**
     *
     * @param cluster
     * @param statInfo
     * @param samples
     * @param old - 用来保存最终的结果
     */
    private void analyzeEachCluster(Cluster cluster, FeatureStatInfo statInfo, List<Sample> samples) {
        for (IFieldAnalyzer fieldAnalyzer : fieldAnalyzers) {
            String[] types = fieldAnalyzer.getProperClusterType();
            for (String type : types) {
                if (cluster.getType().equals(type)) {
                    try {
                        fieldAnalyzer.setBasicAnalysisResult(result);
                        Map<String, String> res = fieldAnalyzer.analyze(cluster, statInfo, samples);
                        //保存结果res存到old中，key是fieldAnalyzer.getFieldName()
                        analysisResult.setFieldAnalysisResult(
                                cluster.getCustomName(), //聚类的名称
                                fieldAnalyzer.getFieldName(), //参数名称
                                fieldAnalyzer.getClass().getName(), //类名
                                res //参数
                                );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    private void confirmClusterType(Cluster[] clusters, List<DocVector> space, List<Sample> samples, FeatureStatInfo statInfo) {
        this.clusterTypeRecognizer.setBasicAnalysisResult(result);
        this.clusterTypeRecognizer.recognizeClusterType(clusters, space, samples, statInfo);
    }

    @Override
    public BasicAnalysisResult getBasicAnalysisResult() {
        return this.result;
    }

    @Override
    public void setFieldAnalyzers(List<IFieldAnalyzer> fieldAnalyzers) {
        this.fieldAnalyzers = fieldAnalyzers;
    }

    public List<IFieldAnalyzer> getFieldAnalyzers() {
        return fieldAnalyzers;
    }
}
