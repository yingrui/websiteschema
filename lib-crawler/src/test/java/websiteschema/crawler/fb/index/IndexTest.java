/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb.index;

import java.io.IOException;
import java.util.*;
import org.junit.Test;
import org.w3c.dom.Document;
import websiteschema.cluster.analyzer.Doc;
import websiteschema.conf.Configure;
import websiteschema.element.DocumentUtil;
import websiteschema.fb.core.RuntimeContext;

/**
 *
 * @author ray
 */
public class IndexTest {

//    @Test
    public void index() throws IOException {
        Document d = DocumentUtil.getDocument("fb/index/idx.xml");
        Doc doc = new Doc(d);
        HttpIndexer indexer = new HttpIndexer("localhost", 9601, "http");
        indexer.setCommand("/DREADDDATA");
        Map<String, String> params = new HashMap<String, String>();
        params.put("documentFormat", "XML");
        indexer.setSuffix("#DREENDDATANOOP");
        indexer.post(params, DocumentUtil.getXMLString(doc.toW3CDocument()));
    }

//    @Test
    public void receiveAndSend() throws IOException {
        FBIndexQueue indexer = new FBIndexQueue();
        RuntimeContext context = new RuntimeContext();
        context.setConfig(Configure.getDefaultConfigure());
        indexer.setContext(context);
        indexer.host = "localhost";
        indexer.queueName = "index_queue";
        indexer.indexHost = "localhost";
        indexer.indexPort = 9601;
        indexer.tempDir = "temp";

        indexer.receive();
    }
}
