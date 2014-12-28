/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster;

import java.util.*;
import org.apache.log4j.Logger;
import websiteschema.model.domain.cluster.*;

/**
 *
 * @author ray
 */
public class CosineClusterer extends Clusterer {

    double threshold = 0.9;
    Logger l = Logger.getLogger(CosineClusterer.class);

    public CosineClusterer(String siteId) {
        super(siteId);
    }

    private ClusterModel clustering(List<DocVector> space) {
        List<Cluster> listCluster = new ArrayList<Cluster>();
        // 增量聚类（包含历史数据的聚类）
        if (null != clusters) {
            listCluster.addAll(Arrays.asList(clusters));
        }

        for (DocVector vect : space) {
            add(vect, listCluster);
        }

        ClusterModel model = new ClusterModel();
        model.setClusters(listCluster.toArray(new Cluster[0]));
        model.setTotalSamples(space.size());
        model.setStatInfo(statInfo);
        model.setRowKey(siteId);
        model.setClustererType(getClass().getName());
        model.setTotalSamples(space.size());
        return model;
    }

    private void add(DocVector vect, List<Cluster> listCluster) {
        double defaultMembership = threshold;
        int index = -1;
        for (int i = 0; i < listCluster.size(); i++) {
            Cluster cluster = listCluster.get(i);
            double m = membership(vect, cluster);
            l.info("与Cluster " + i + " 的隶属度为: " + m);
            if (m > defaultMembership) {
                defaultMembership = m;
                index = i;
            }
        }
        if (index > -1) {
            //发现相似的类，将样本添加到该类中
            Cluster cluster = listCluster.get(index);
            List<String> sameKindInstancs = cluster.getSamples();
            if (!sameKindInstancs.contains(vect.getName())) {
                sameKindInstancs.add(vect.getName());
            }
        } else {
            //创建新的Cluster，然后增加进去。
            Cluster cluster = createCluster(vect);
            int size = findMaxCustomName(listCluster) + 1;
            cluster.setCustomName(String.valueOf(size));//cluster的名字默认以数字命名
            listCluster.add(cluster);
        }
        return;
    }

    /**
     * 遍历所有Cluster，找到以数字命名的Cluster，并返回最大的一个数字
     * @param listCluster
     * @return
     */
    private int findMaxCustomName(List<Cluster> listCluster) {
        int ret = -1;

        for (Cluster cluster : listCluster) {
            try {
                int num = Integer.parseInt(cluster.getCustomName());
                if (num > ret) {
                    ret = num;
                }
            } catch (Exception ex) {
            }
        }

        return ret;
    }

    private Cluster createCluster(DocVector vect) {
        Cluster cluster = new Cluster();
        cluster.setSiteId(siteId);
        cluster.setCentralPoint(vect);
        cluster.setThreshold(threshold);
        List<String> sameKindInstancs = new ArrayList<String>();
        sameKindInstancs.add(vect.getName());
        cluster.setSamples(sameKindInstancs);
        return cluster;
    }

    private double membership(DocVector vect, Cluster cluster) {
        DocVector cp = cluster.getCentralPoint();
        return membershipDegree(vect, cp);
    }

    @Override
    public double membershipDegree(DocVector v1, DocVector v2) {
        return cosine(v1, v2);
    }

    private double cosine(DocVector v1, DocVector v2) {
        double d = 0.0;

        d = dot(v1, v2) / (abs(v1) * abs(v2));

        return d;
    }

    private double abs(DocVector v) {
        double d = 0.0;
        for (Dimension dim : v.getDims()) {
            d += dim.getValue() * dim.getValue();
        }
        d = Math.sqrt(d);
        return d;
    }

    private double dot(DocVector v1, DocVector v2) {
        Dimension[] dims1 = v1.getDims();
        Dimension[] dims2 = v2.getDims();
        if (dims2.length > dims1.length) {
            Dimension[] dims = dims1;
            dims1 = dims2;
            dims2 = dims;
        }
        Map<Integer, Integer> mapV1 = new HashMap<Integer, Integer>();
        for (int i = 0; i < dims1.length; i++) {
            Dimension dim = dims1[i];
            mapV1.put(dim.getId(), dim.getValue());
        }
        double d = 0.0;
        for (int i = 0; i < dims2.length; i++) {
            Dimension dim2 = dims2[i];
            int value2 = dim2.getValue();
            int value1 = mapV1.containsKey(dim2.getId()) ? mapV1.get(dim2.getId()) : 0;
            d += value1 * value2;
        }
        return d;
    }

    @Override
    public Cluster classify(Sample sample) {
        DocVectorConvertor convertor = new DocVectorConvertor();
        DocVector vect = convertor.convert(sample, statInfo);
        int index = -1;
        double membership = 0.5;
        for (int i = 0; i < clusters.length; i++) {
            Cluster c = clusters[i];
            double sim = membershipDegree(vect, c.getCentralPoint());
            if (sim > membership) {
                membership = sim;
                index = i;
            }
        }
        if (index >= 0) {
            return clusters[index];
        } else {
            return null;
        }
    }

    @Override
    public ClusterModel clustering() {
        List<DocVector> space = new ArrayList<DocVector>();
        DocVectorConvertor convertor = new DocVectorConvertor();
        for (Sample sample : samples) {
            DocVector vect = convertor.convert(sample, statInfo);
            if (null != vect) {
                space.add(vect);
            }
        }
        return clustering(space);
    }
}
