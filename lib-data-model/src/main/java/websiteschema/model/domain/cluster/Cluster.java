/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain.cluster;

import java.util.List;

/**
 *
 * @author ray
 */
public class Cluster {

    // 样本的RowKey
    List<String> samples;
    DocVector centralPoint;
    double threshold;
    String siteId;
    String type;
    // 用户可以起的名字，默认为数字形式
    String customName;

    public DocVector getCentralPoint() {
        return centralPoint;
    }

    public void setCentralPoint(DocVector centralPoint) {
        this.centralPoint = centralPoint;
    }

    public List<String> getSamples() {
        return samples;
    }

    public void setSamples(List<String> samples) {
        this.samples = samples;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
