/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb.field;

import websiteschema.crawler.fb.index.*;
import java.io.IOException;
import java.util.*;
import org.junit.Test;
import org.w3c.dom.Document;
import websiteschema.cluster.analyzer.Doc;
import websiteschema.element.DocumentUtil;

/**
 *
 * @author ray
 */
public class TimeToDateTest {

    @Test
    public void timeToDate() throws IOException {
        Document d = DocumentUtil.getDocument("fb/index/idx.xml");
        Doc doc = new Doc(d);
        FBTimeToDate fb = new FBTimeToDate();
        fb.tagTime = "THREADS/DATE";
        fb.tag = "STATDATE";
        fb.doc = doc;
        fb.timeToDate();
        assert("2012-03-12".equals(doc.getValue("STATDATE")));
        System.out.println(DocumentUtil.getXMLString(fb.doc.toW3CDocument()));
    }

}
