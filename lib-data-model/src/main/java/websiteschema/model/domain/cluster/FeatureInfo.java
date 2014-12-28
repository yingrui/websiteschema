/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain.cluster;

/**
 *
 * @author ray
 */
public class FeatureInfo {

    // XPath
    private String name = null;
    // 在所有文档中出现的总次数
    private int totalCount = 0;
    //Average frequence of each document
    // 平均在每篇文档中出现的次数（平均的基数是所有文档的数目，以下相同）
    private int frequence = 0;
    // 在多少篇文档中出现过
    private int documentFrequence = 0;
    // 平均在每篇文档中包含的有效字符的数量
    private int weight = 0;
    // 每篇文档中包含的字符之间的相似度
    private double similarity = 0.0;

    public int getDocumentFrequence() {
        return documentFrequence;
    }

    public void setDocumentFrequence(int documentFrequence) {
        this.documentFrequence = documentFrequence;
    }

    public int getFrequence() {
        return frequence;
    }

    public void setFrequence(int frequence) {
        this.frequence = frequence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

}
