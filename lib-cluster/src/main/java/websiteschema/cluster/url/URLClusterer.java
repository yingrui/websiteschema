/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.url;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import websiteschema.utils.EditDistance;

/**
 *
 * @author ray
 */
public class URLClusterer {

    int clusterSize = 25;
    double th = 0.7;

    public List<URLCluster> clustering(List<URI> setURLs) {
        List<URLCluster> ret = init(setURLs);

        //当类的数量不大于size的时候，终止聚类。
        Set<Integer> ignore = new HashSet<Integer>();
        while (ret.size() > clusterSize) {
            int oriSize = ret.size();
            ret = merge(ret, ignore);
            if (oriSize == ret.size()) {
                System.out.println("\nCan not merge!\n");
                break;
            }
        }

        return ret;
    }

    public int getClusterSize() {
        return clusterSize;
    }

    public void setClusterSize(int clusterSize) {
        this.clusterSize = clusterSize;
    }

    /**
     * 随机取出一个类，然后找到最相似的一个另一类，然后合并两个类
     * @param clusters
     * @param ignore - 有些孤立的类要记下来，下次忽略掉他们
     * @return
     */
    private List<URLCluster> merge(List<URLCluster> clusters, Set<Integer> ignore) {
        int range = clusters.size() - 1;
        if (range <= 0) {
            throw new RuntimeException("only one cluster, could not merge!");
        }

        int index = -1;
        for (int i = 0; i < clusters.size(); i++) {
            if (!ignore.contains(i)) {
                double membership = 0.0;
                URLCluster c1 = clusters.get(i);
                for (int j = i + 1; j < clusters.size(); j++) {
                    int i2 = (j) % (clusters.size());
                    URLCluster c2 = clusters.get(i2);
                    double sim = membership(c1, c2);
                    if (sim > membership && sim > th) {
                        membership = sim;
                        index = i2;
                    }
                }
                if (index >= 0) {
                    URLCluster c2 = clusters.get(index);
                    c2.append(c1);
                    clusters.remove(i);
                    //一旦合并完任意两类，终止循环
                    break;
                } else {
                    //记住孤立的类，因为他找不到相似的其他类
                    ignore.add(i);
                }
            }
        }
        return clusters;
    }

    private List<URLCluster> init(List<URI> setURLs) {
        List<URLCluster> ret = new ArrayList<URLCluster>();
        for (URI uri : setURLs) {
            URLCluster c = new URLCluster();
            c.append(new URLObj(uri));
            ret.add(c);
        }
        return ret;
    }

    private double membership(URLCluster c1, URLCluster c2) {
//        List<URLObj> set1 = c1.sampling(1);
//        List<URLObj> set2 = c2.sampling(1);
//
//        double sim = 0.0D;
//        for (URLObj u1 : set1) {
//            for (URLObj u2 : set2) {
//                double s = similarity(u1, u2);
//                if (s > sim) {
//                    sim = s;
//                }
//            }
//        }

        URLObj u1 = c1.getMembers().get(0);
        URLObj u2 = c2.getMembers().get(0);
        double sim = similarity(u1, u2);
        return sim;
    }

    private double similarity(URLObj u1, URLObj u2) {
        double ret = 0.0D;
        double w[] = {0.5, 0.1, 0.3, 0.1, 0.1};

        EditDistance ld = new EditDistance();
        if (null != u1.getHost() && null != u2.getHost()) {
            int eq = u1.getHost().equalsIgnoreCase(u2.getHost()) ? 1 : 0;
            ret += w[0] * eq;
        }
        if (null != u1.getSchema() && null != u2.getSchema()) {
            int eq = u1.getSchema().equalsIgnoreCase(u2.getSchema()) ? 1 : 0;
            ret += w[1] * eq;
        }
        if (null != u1.getPath() && null != u2.getPath()) {
            String path1 = u1.getPath().replaceAll("/[A-Z0-9]+\\.", "/CODE.").replaceAll("[0-9]", "d");
            String path2 = u2.getPath().replaceAll("/[A-Z0-9]+\\.", "/CODE.").replaceAll("[0-9]", "d");
            ret += w[2] * ld.caculateSimilarityBetweenStrings(path1, path2);
            int slashCount1 = count(path1, "/");
            int slashCount2 = count(path2, "/");
            int eq = slashCount1 == slashCount2 ? 1 : 0;
            ret += w[3] * eq;
        }
        if (null != u1.getQuery() && null != u2.getQuery()) {
            ret += w[4] * ld.caculateSimilarityBetweenStrings(u1.getQuery(), u2.getQuery());
        }
        return ret;
    }

    private int count(String str, String s) {
        int ret = 0;
        int pos = 0;
        pos = str.indexOf(s, pos);
        while (pos >= 0) {
            ret++;
            pos = str.indexOf(s, pos + 1);
        }
        return ret;
    }
}
