/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import websiteschema.model.domain.Category;
import websiteschema.persistence.rdbms.CategoryMapper;
import websiteschema.utils.PojoMapper;

/**
 *
 * @author ray
 */
@Controller
@RequestMapping(value = "/category")
public class CategoryTree {

    Logger l = Logger.getLogger(CategoryTree.class);
    @Autowired
    CategoryMapper categoryMapper;

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public void index(HttpServletRequest request, HttpServletResponse response) throws IOException {
        OutputStream os = response.getOutputStream();
        response.setCharacterEncoding("utf-8");

        //得到根节点下的第一级数据
        SimpleTreeNode[] nodes = getNodes(0);
        String json = PojoMapper.toJson(nodes);

        os.write(json.getBytes("utf-8"));
    }

    @RequestMapping(value = "/all", method = {RequestMethod.GET, RequestMethod.POST})
    public void getAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        OutputStream os = response.getOutputStream();
        response.setCharacterEncoding("utf-8");

        //得到根节点下的第一级数据
        SimpleTreeNode[] nodes = getAllNodes();
        String json = PojoMapper.toJson(nodes);

        os.write(json.getBytes("utf-8"));
    }

    @RequestMapping(value = "{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public void getChildren(@PathVariable Long id, HttpServletResponse response) throws IOException {
        OutputStream os = response.getOutputStream();
        response.setCharacterEncoding("utf-8");

        //得到根节点下的第一级数据
        SimpleTreeNode[] nodes = getNodes(id);
        String json = PojoMapper.toJson(nodes);

        os.write(json.getBytes("utf-8"));
    }

    private SimpleTreeNode[] getNodes(long parentId) {
        List<Category> websites = categoryMapper.getChildCategories(parentId);
        if (null != websites) {
            for (Category site : websites) {
                l.debug(site.getName());
            }
        }

        return buildTree(websites);
    }

    private SimpleTreeNode[] getAllNodes() {
        List<Category> websites = categoryMapper.getAllCategories();
        if (null != websites) {
            for (Category site : websites) {
                l.debug(site.getName());
            }
        }

        return buildTree(websites);
    }

    private SimpleTreeNode[] buildTree(List<Category> websites) {
        if (null != websites) {
            List<SimpleTreeNode> nodes = new TreeUtil().createTreeNode(websites);
            return nodes.toArray(new SimpleTreeNode[0]);
        } else {
            return null;
        }
    }
}
