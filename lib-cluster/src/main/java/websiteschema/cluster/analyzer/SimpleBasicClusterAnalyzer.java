/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import websiteschema.model.domain.cluster.Cluster;
import websiteschema.model.domain.cluster.DocVector;
import websiteschema.model.domain.cluster.FeatureStatInfo;
import websiteschema.model.domain.cluster.Sample;

/**
 *
 * @author ray
 */
public class SimpleBasicClusterAnalyzer implements IBasicClusterAnalyzer {

    AnalyzerUtil analyzer = AnalyzerUtil.getInstance();

    public BasicAnalysisResult analysis(Cluster[] clusters, List<DocVector> space, List<Sample> samples, FeatureStatInfo statInfo) {
        BasicAnalysisResult result = new BasicAnalysisResult();
        for (Cluster cluster : clusters) {
            if (cluster.getSamples().size() > 1) {
                List<String> allSamples = cluster.getSamples();
                List<String> rowKeys = new ArrayList<String>();
                int count = allSamples.size() > 10 ? 10 : allSamples.size();
                for (int i = 0; i < count; i++) {
                    rowKeys.add(allSamples.get(i));
                }
                List<DocVector> vectors = analyzer.getVectors(rowKeys, space);
                List<Sample> clusterSamples = analyzer.getSamples(rowKeys, samples);
                Set<String> commonNodes = analyzer.findCommonNodes(vectors, statInfo);

                analyzer.findTitlePrefixAndSuffix(result.getTitlePrefix(), result.getTitleSuffix(), clusterSamples);
                Set<String> invalidNodeSet = analyzer.findInvalidNodes(clusterSamples, commonNodes, 0.6);
                result.getInvalidNodes().addAll(invalidNodeSet);
                result.getValidNodes().addAll(commonNodes);
                for (String xpath : result.getInvalidNodes()) {
                    result.getValidNodes().remove(xpath);
                }

            } else {
            }
        }
        return result;
    }
}
