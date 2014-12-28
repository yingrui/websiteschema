/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import java.util.Queue;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import websiteschema.fb.core.RuntimeContext;
import websiteschema.fb.core.app.Application;
import websiteschema.model.domain.Websiteschema;
import websiteschema.persistence.Mapper;
import websiteschema.utils.FileUtil;

/**
 *
 * @author mgd
 */
public class DOMExtractorTest {

    @Test
    public void test() throws Exception {

        Document source = create();
        String siteId_str = "www_163_com_1";
        Application app = new Application();
        RuntimeContext context = app.getContext();
        context.loadConfigure("fb/crawler.app");
        Mapper<Websiteschema> mapper = context.getSpringBeanFactory().getBean("websiteschemaMapper", Mapper.class);
        Websiteschema websiteschema = mapper.get(siteId_str);
        FBDOMExtractor extractor = new FBDOMExtractor();
        extractor.in = source;
        extractor.schema = websiteschema;
        extractor.clusterName = "DOCUMENT";
        System.out.println("开始尝试抽取页面：\n");
        extractor.extract();
        System.out.println("抽取结束：");

        printNodes(extractor.out.toW3CDocument());
    }

    // 逐层打印DOM树
    private void printNodes(Node root) {
        Queue<Node> q = new LinkedList<Node>();
        q.add(root);
        Node iter_node = null;
        while (q.isEmpty()) {
            iter_node = q.poll();
            NodeList children = iter_node.getChildNodes();
            if (null == children) {
                String cont_str = iter_node.getTextContent();
                System.out.println(cont_str);
            } else {
                for (int i = 0; i < children.getLength(); i++) {
                    q.add(children.item(i));
                }
            }
        }
    }

    private Document create() throws Exception {
        String content = FileUtil.readResource("test.xml");
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(content.getBytes("UTF-8")));
        return doc;
    }
}