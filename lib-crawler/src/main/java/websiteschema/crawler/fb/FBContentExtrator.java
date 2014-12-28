/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import websiteschema.element.DocumentUtil;
import websiteschema.fb.annotation.*;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.model.domain.cluster.Cluster;

/**
 *
 * @author mgd
 */
@EI(name = {"EXTRACT:EXT"})
@EO(name = {"EO", "FATAL", "EMPTY"})
@Description(desc = "抽取DOM树中指定位置内的内容")
public class FBContentExtrator extends FunctionBlock {

    @DI(name = "IN")
    public Document in_doc;
    @DI(name = "XPATH")
    public String xpath_str = null;
    @DI(name = "URL")
    public String url_str;
    @DI(name = "SITE")
    public String siteId_str;
    @DI(name = "CLS")
    public Cluster cluster;
    @DO(name = "OUT", relativeEvents = {"EO"})
    public List<String> cont_list = null;

    @Algorithm(name = "EXT")
    public void extract() {
        try {
            extractContents();
            if (null != cont_list && !cont_list.isEmpty()) {
                this.triggerEvent("EO");
            } else {
                this.triggerEvent("EMPTY");
            }
        } catch (Exception ex) {
            l.error(this.getName() + " error when extract contents: " + ex.getMessage(), ex);
            this.triggerEvent("FATAL");
        }
    }

    private void extractContents() {
        if (null != in_doc && null != xpath_str && null != url_str) {
            List<Node> nodes = DocumentUtil.getByXPath(in_doc, xpath_str.trim());
            cont_list = new ArrayList<String>();
            for (int i = 0; i < nodes.size(); i++) {
                Node node = nodes.get(i);
                List<String> str_list = getContents(node);
                if (null != str_list) {
                    cont_list.addAll(str_list);
                }
            }
        }
    }

    private List<String> getContents(Node root) {
        if (null == root) {
            return null;
        }
        Queue<Node> q = new LinkedList<Node>();
        q.add(root);
        List<String> ret = new ArrayList<String>();
        Node iter_node = null;
        while (q.isEmpty()) {
            iter_node = q.poll();
            if (iter_node.getNodeType() == Node.TEXT_NODE) {
                String cont_str = iter_node.getTextContent();
                if (null != cont_str) {
                    ret.add(cont_str);
                }
            } else {
                NodeList children = iter_node.getChildNodes();
                if (null != children) {
                    for (int i = 0; i < children.getLength(); i++) {
                        q.add(children.item(i));
                    }
                }
            }
        }

        return ret;
    }
}