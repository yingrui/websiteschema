/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer.fields;

import org.junit.Test;
import websiteschema.cluster.analyzer.Doc;

/**
 *
 * @author ray
 */
public class SourceNameFilterTest {

    @Test
    public void should_recognize_sourcename_from_multi_candidates() {
        SourceNameFilter snf = new SourceNameFilter();
        Doc doc = new Doc();
        doc.addField("SOURCENAME", "2012年01月25日19:58");
//        doc.addField("SOURCENAME", "腾讯科技 悦潼");
        doc.addField("SOURCENAME", "来源:京华时报 作者:古晓宇 选稿:");
        snf.filtering(doc);
        System.out.println(doc.getValues("SOURCENAME"));
        assert (doc.getValue("SOURCENAME").equals("京华时报"));
    }

    @Test
    public void shoud_recognize_sourcename_when_sourcename_and_author_in_same_field() {
        SourceNameFilter snf = new SourceNameFilter();
        Doc doc = new Doc();
        doc.addField("SOURCENAME", "2012年01月25日19:58");
        doc.addField("SOURCENAME", "腾讯科技 悦潼");
        snf.filtering(doc);
        System.out.println(doc.getValues("SOURCENAME"));
        assert (doc.getValue("SOURCENAME").equals("腾讯科技"));
    }

    @Test
    public void shoud_recognize_sourcename_with_separator() {
        SourceNameFilter snf = new SourceNameFilter();
        Doc doc = new Doc();
        doc.addField("SOURCENAME", "来源：中国证券网-中证网");
        doc.addField("SOURCENAME", "[字体");
        snf.filtering(doc);
        System.out.println(doc.getValues("SOURCENAME"));
        assert (doc.getValue("SOURCENAME").equals("中国证券网-中证网"));
    }

    @Test
    public void shoud_recognize_channel_as_sourcename() {
        SourceNameFilter snf = new SourceNameFilter();
        String res = snf.filtering("新华网天津房产频道  唐淑倩");
        System.out.println(res);
        assert (res.equals("新华网天津房产频道"));
    }
}
