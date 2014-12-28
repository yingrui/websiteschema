/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import websiteschema.model.domain.Category;
import websiteschema.model.domain.TreeNode;

/**
 *
 * @author ray
 */
public class TreeUtil {

    public List<SimpleTreeNode> createTreeNode(List<? extends TreeNode> list) {
        Set<SimpleTreeNode> ret = new LinkedHashSet<SimpleTreeNode>();
        Map<Long, TreeNode> nodes = new HashMap<Long, TreeNode>();
        Map<Long, SimpleTreeNode> cache = new HashMap<Long, SimpleTreeNode>();
        for (TreeNode obj : list) {
            nodes.put(obj.getId(), obj);
        }

        for (TreeNode obj : list) {
            createNode(ret, obj, cache, nodes);
        }

        nodes = null;
        cache = null;
        List<SimpleTreeNode> l = new ArrayList<SimpleTreeNode>();
        for (SimpleTreeNode stn : ret) {
            l.add(stn);
        }
        return l;
    }

    private SimpleTreeNode createNode(Set<SimpleTreeNode> ret, TreeNode site, Map<Long, SimpleTreeNode> cache, Map<Long, TreeNode> nodes) {
        long id = site.getId();
        long parentId = site.getParentId();
        SimpleTreeNode node = null;
        //创建新的TreeNode
        if (cache.containsKey(id)) {
            node = cache.get(id);
        } else {
            node = new SimpleTreeNode();
            node.setText(site.getName());
            node.setId(String.valueOf(site.getId()));
            node.setTitle(site.getName());
            node.setLeaf(site.isLeafNode());
            if (!node.isLeaf()) {
                //如果不是叶子节点，设置图标
                node.setIconCls("");
            }
            cache.put(id, node);
        }
        //检查是否有父亲，如果有，则将自己添加为其子节点
        if (nodes.containsKey(parentId)) {
            TreeNode pSite = nodes.get(parentId);
            SimpleTreeNode pNode = null;
            if (cache.containsKey(parentId)) {
                pNode = cache.get(parentId);
            } else {
                pNode = createNode(ret, pSite, cache, nodes);
            }
            //将父亲设置为非叶子节点
            if (null != pNode) {
                pNode.addChild(node);
            }
        } else {
            //没有父亲节点
            if (!ret.contains(node)) {
                ret.add(node);
            }
        }
        return node;
    }

    public static void main(String[] args) {
        Category s1 = new Category();
        s1.setId(1);
        s1.setName("1");
        s1.setParentId(0);
        Category s2 = new Category();
        s2.setId(2);
        s2.setName("2");
        s2.setParentId(0);
        Category s3 = new Category();
        s3.setId(3);
        s3.setName("3");
        s3.setParentId(2);
        Category s4 = new Category();
        s4.setId(4);
        s4.setName("4");
        s4.setParentId(3);
        Category s5 = new Category();
        s5.setId(5);
        s5.setName("5");
        s5.setParentId(3);
        Category s6 = new Category();
        s6.setId(6);
        s6.setName("6");
        s6.setParentId(9);
        List<Category> list = new ArrayList<Category>();
        list.add(s3);
        list.add(s4);
        list.add(s5);
        list.add(s6);
        list.add(s2);
        list.add(s1);

        List<SimpleTreeNode> tree = new TreeUtil().createTreeNode(list);
        System.out.println("tree length: " + tree.size());
    }
}
