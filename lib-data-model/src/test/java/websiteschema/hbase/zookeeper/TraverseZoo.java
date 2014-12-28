/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.hbase.zookeeper;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 *
 * @author mgd
 */
public class TraverseZoo implements Watcher {

    String znode;
    ZooKeeper zk;

    public TraverseZoo(String hostPort) {
        try {
            zk = new ZooKeeper(hostPort, 3000, this);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent event) {
    }

    // 广度优先遍历树
    public List<String> traverseB(String root) throws KeeperException, InterruptedException {
        Queue<String> qNodes = new ArrayDeque<String>();
        qNodes.add(root);
        List<String> retList = new ArrayList<String>(50);// 经验值
        while (!qNodes.isEmpty()) {
            String iter = qNodes.poll();
            retList.add(iter);
            List<String> nodeNames = zk.getChildren(iter, false);
            if (null != nodeNames) {
                for (String str : nodeNames) {
                    String node;
                    if ("/".equals(iter)) {
                        node = iter + str;
                    } else {
                        node = iter + "/" + str;
                    }
                    qNodes.add(node);
                }
            }
        }

        return retList;
    }

    // 深度优先遍历树
    public List<String> traverseD(String root) throws KeeperException, InterruptedException {
        Stack<String> sNode = new Stack<String>();
        sNode.add(root);
        List<String> retList = new ArrayList<String>(50);// 经验值
        while (!sNode.isEmpty()) {
            String iter = sNode.pop();
            retList.add(iter);
            List<String> nodeNames = zk.getChildren(iter, false);
            if (null != nodeNames) {
                for (String str : nodeNames) {
                    String node;
                    if ("/".equals(iter)) {
                        node = iter + str;
                    } else {
                        node = iter + "/" + str;
                    }
                    sNode.push(node);
                }
            }
        }

        return retList;
    }

    public static void main(String[] args) throws KeeperException, InterruptedException {
        TraverseZoo tz = new TraverseZoo("hadoop2:2181");
        List<String> result = tz.traverseD("/");
        for (String str : result) {
            System.out.println(str);
            String data = new String(tz.zk.getData(str, false, null));
            System.out.println(data);
            System.out.println();
        }
//        System.out.println(tz.zk.getChildren("/hbase/rs", false).size());
    }

   
}
