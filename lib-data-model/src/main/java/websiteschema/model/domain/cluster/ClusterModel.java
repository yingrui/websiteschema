/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain.cluster;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import websiteschema.model.domain.HBaseBean;
import websiteschema.persistence.hbase.annotation.ColumnFamily;
import websiteschema.persistence.hbase.annotation.RowKey;

/**
 *
 * @author ray
 */
public class ClusterModel implements HBaseBean {

    @RowKey
    String rowKey;
    @ColumnFamily
    Cluster[] clusters = null;
    @ColumnFamily
    FeatureStatInfo statInfo = null;
    @ColumnFamily
    int totalSamples = 0;
    @ColumnFamily
    String clustererType;

    public Cluster getCluster(int index) {
        return clusters[index];
    }

    public Cluster[] getClusters() {
        return clusters;
    }

    public void setClusters(Cluster[] clusters) {
        this.clusters = clusters;
    }

    public FeatureStatInfo getStatInfo() {
        return statInfo;
    }

    public void setStatInfo(FeatureStatInfo statInfo) {
        this.statInfo = statInfo;
    }

    public int getTotalSamples() {
        return totalSamples;
    }

    public void setTotalSamples(int totalSamples) {
        this.totalSamples = totalSamples;
    }

    public void printClusterInfo(PrintWriter pw) {
        if (null != clusters) {
            for (int i = 0; i < clusters.length; i++) {
                pw.println("Cluster name: " + clusters[i].getCustomName());
                List<String> sameKindInstancs = clusters[i].getSamples();
                for (String sample : sameKindInstancs) {
                    pw.println(sample);
                }
            }
        }
    }

    @Override
    public String getRowKey() {
        return rowKey;
    }

    @Override
    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public void delete(String cp) {
        if (null != clusters) {
            List<Cluster> list = new ArrayList<Cluster>();
            for (int i = 0; i < clusters.length; i++) {
                Cluster c = clusters[i];
                if (!cp.equals(c.getCentralPoint().getName())) {
                    list.add(c);
                }
            }

            clusters = list.toArray(new Cluster[0]);
        }
    }

    public String getClustererType() {
        return clustererType;
    }

    public void setClustererType(String clustererType) {
        this.clustererType = clustererType;
    }
}
