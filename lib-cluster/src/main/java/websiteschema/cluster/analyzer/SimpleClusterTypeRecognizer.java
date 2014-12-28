/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer;

import java.util.ArrayList;
import java.util.List;
import websiteschema.model.domain.cluster.Cluster;
import websiteschema.model.domain.cluster.Dimension;
import websiteschema.model.domain.cluster.DocVector;
import websiteschema.model.domain.cluster.FeatureInfo;
import websiteschema.model.domain.cluster.FeatureStatInfo;
import websiteschema.model.domain.cluster.Sample;
import websiteschema.weka.WekaClassifier;

/**
 *
 * @author ray
 */
public class SimpleClusterTypeRecognizer implements IClusterTypeRecognizer {

    BasicAnalysisResult result = null;
    AnalyzerUtil analyzer = new AnalyzerUtil();

    WekaClassifier wc = new WekaClassifier();

    public SimpleClusterTypeRecognizer() {
        wc.setModel("websiteschema/model/cluster_type.model");
    }

    public void recognizeClusterType(Cluster[] clusters, List<DocVector> space, List<Sample> samples, FeatureStatInfo statInfo) {
        List<String> allSamples = null;
        for (Cluster cluster : clusters) {// 遍历所有文档簇
            if (cluster.getSamples().size() > 0) {
                allSamples = cluster.getSamples();
                List<String> rowKeys = new ArrayList<String>();
                int count = allSamples.size() > 10 ? 10 : allSamples.size();
                for (int i = 0; i < count; i++) {// 从每一个簇中取一个样本集进行分析，样本集中样本个数最多为10个
                    rowKeys.add(allSamples.get(i));
                }
                List<DocVector> vectors = analyzer.getVectors(rowKeys, space);// 获取样本中能够表示XPath的数字
//                List<Sample> clusterSamples = analyzer.getSamples(rowKeys, samples);
//                Set<String> commonNodes = analyzer.findCommonNodes(vectors, statInfo);
                double textWeight = 0.0;// 非链接（<a></a>）中的内容的平均字符数量
                int textCount = 0;// 累计非<a></a>
                double anchorWeight = 0.0;
                int anchorCount = 0;// 累计一个文档中链接（<a></a>）的数量
                int weightGt = 0;// 记录XPath权重大于门限值的节点的数量
                int maxWeight = 0;// 记录一个文档中权重最大一个节点的权重
//                DocVector v = vectors.get(0);
                for (DocVector v : vectors) {// 遍历样本集中样本
                    Dimension dims[] = v.getDims();
                    for (Dimension dim : dims) {// 遍历样本的XPath & Weight
                        int dimId = dim.getId();
                        int value = dim.getValue();
                        FeatureInfo feature = statInfo.getList()[dimId];// 由数字（XPath）到特征信息
                        String xpath = feature.getName();
                        if (!result.getInvalidNodes().contains(xpath)) {// 只对有效节点进行分析
                            xpath = xpath.toLowerCase();
                            if (feature.getWeight() > 0) {
                                if (isAnchor(xpath)) {
                                    anchorWeight += feature.getWeight();
                                    anchorCount++;
                                } else {
                                    textWeight += feature.getWeight();
                                    textCount++;
                                }

                                int freq = feature.getFrequence();// 相同节点（XPath）出现的频度
                                if (freq > 0) {
                                    int weightOfEachNode = feature.getWeight() / freq;
                                    if (weightOfEachNode >= 34) {// 平均每个节点包含的字符数
                                        weightGt++;
                                    }
                                }

                                if (feature.getWeight() > maxWeight) {
                                    maxWeight = feature.getWeight();
                                }
                            }
                        }
                    }
                }
                double ratio = textWeight / (textWeight + anchorWeight);// 非链接节点权重的比率
                double countRatio = (double) textCount / (double) (textCount + anchorCount);// 非内容节点数量的比率

                double[] vec = {ratio, countRatio, maxWeight, weightGt};
                String type = null;
                switch (wc.classify(vec)) {
                    case 0:
                        type = TYPE_DOCUMENT;
                        break;
                    case 1 :
                        type = TYPE_LINK;
                        break;
                    case 2 :
                        type = TYPE_INVALID;
                        break;
                    default :
                        type = TYPE_INVALID;
                        break;
                }
                cluster.setType(type);
            } else {
                String type = TYPE_INVALID;
                cluster.setType(type);
            }
        }
    }

//    public void recognizeClusterType(Cluster[] clusters, List<DocVector> space, List<Sample> samples, FeatureStatInfo statInfo) {
//        List<String> allSamples = null;
//        for (Cluster cluster : clusters) {// 遍历所有文档簇
//            if (cluster.getSamples().size() > 0) {
//                allSamples = cluster.getSamples();
//                List<String> rowKeys = new ArrayList<String>();
//                int count = allSamples.size() > 10 ? 10 : allSamples.size();
//                for (int i = 0; i < count; i++) {// 从每一个簇中取一个样本集进行分析，样本集中样本个数最多为10个
//                    rowKeys.add(allSamples.get(i));
//                }
//                List<DocVector> vectors = analyzer.getVectors(rowKeys, space);// 获取样本中能够表示XPath的数字
////                List<Sample> clusterSamples = analyzer.getSamples(rowKeys, samples);
////                Set<String> commonNodes = analyzer.findCommonNodes(vectors, statInfo);
//                double textWeight = 0.0;// 非链接（<a></a>）中的内容的平均字符数量
//                int textCount = 0;// 累计非<a></a>
//                double anchorWeight = 0.0;
//                int anchorCount = 0;// 累计一个文档中链接（<a></a>）的数量
//                int weightGt = 0;// 记录XPath权重大于门限值的节点的数量
//                int maxWeight = 0;// 记录一个文档中权重最大一个节点的权重
////                DocVector v = vectors.get(0);
//                for (DocVector v : vectors) {// 遍历样本集中样本
//                    Dimension dims[] = v.getDims();
//                    for (Dimension dim : dims) {// 遍历样本的XPath & Weight
//                        int dimId = dim.getId();
//                        int value = dim.getValue();
//                        FeatureInfo feature = statInfo.getList()[dimId];// 由数字（XPath）到特征信息
//                        String xpath = feature.getName();
//                        if (!result.getInvalidNodes().contains(xpath)) {// 只对有效节点进行分析
//                            xpath = xpath.toLowerCase();
//                            if (feature.getWeight() > 0) {
//                                if (isAnchor(xpath)) {
//                                    anchorWeight += feature.getWeight();
//                                    anchorCount++;
//                                } else {
//                                    textWeight += feature.getWeight();
//                                    textCount++;
//                                }
//
//                                int freq = feature.getFrequence();// 相同节点（XPath）出现的频度
//                                if (freq > 0) {
//                                    int weightOfEachNode = feature.getWeight() / freq;
//                                    if (weightOfEachNode >= 34) {// 平均每个节点包含的字符数
//                                        weightGt++;
//                                    }
//                                }
//
//                                if (feature.getWeight() > maxWeight) {
//                                    maxWeight = feature.getWeight();
//                                }
//                            }
//                        }
//                    }
//                }
//                double ratio = textWeight / (textWeight + anchorWeight);// 非链接节点权重的比率
//                double countRatio = (double) textCount / (double) (textCount + anchorCount);// 非内容节点数量的比率
////                System.out.println("cluster: " + cluster.getCustomName() + " text ratio: " + ratio + "text count ratio: " + countRatio);
//                String type = "LINKS";
//                if ((textWeight + anchorWeight) < 0.01D) {
//                    type = "INVALID";
//                } else if (ratio > 0.5) {
//                    type = "DOCUMENT";
//                }
//                cluster.setType(type);
//                SimpleLogger.record_line(ratio + "," + countRatio + "," + maxWeight + "," + weightGt + "," + type);
////                System.out.println(ratio + "," + countRatio + "," + maxWeight + "," + weightGt + "," + type);
//            } else {
//                String type = "INVALID";
//                cluster.setType(type);
//            }
//        }
//        if (null != allSamples && allSamples.size() > 0) {
//            SimpleLogger.record_line(allSamples.get(0));
//        }
//        SimpleLogger.end_block();
//    }
    // 判断一个XPath对应的是不是链接（<a></a>）标签
    private boolean isAnchor(String xpath) {
        boolean ret = false;
        if (xpath.endsWith("/a")) {
            ret = true;
        } else if (xpath.contains("/a[") || xpath.contains("/a/")) {
            ret = true;
        }

        return ret;
    }

    public void setBasicAnalysisResult(BasicAnalysisResult analysisResult) {
        this.result = analysisResult;
    }
}
