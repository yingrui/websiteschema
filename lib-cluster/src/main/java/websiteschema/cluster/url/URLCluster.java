/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.url;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author ray
 */
public class URLCluster {

    List<URLObj> members = new ArrayList<URLObj>();

    public List<URLObj> getMembers() {
        return members;
    }

    public void append(URLObj obj) {
        members.add(obj);
    }

    public void append(URLCluster cluster) {
        members.addAll(cluster.getMembers());
    }

    /**
     * 抽样指定的样本数
     * @param num
     * @return
     */
    public List<URLObj> sampling(int num) {
        List<URLObj> ret = new ArrayList<URLObj>();

        int n = num > getMembers().size() ? getMembers().size() : num;

        ret.addAll(getMembers());
        // 需要删除的元素数量
        int d = getMembers().size() - n;
        for (int i = 0; i < d; i++) {
            int index = new Random(ret.size()).nextInt(ret.size());
            ret.remove(index);
        }

        return ret;
    }
}
