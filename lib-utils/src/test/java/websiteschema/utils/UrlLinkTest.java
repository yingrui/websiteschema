/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.utils;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 *
 * @author ray
 */
public class UrlLinkTest {

    @Test
    public void test() {
        //测试URL解析中，中文字符的问题
        String url = "http://www.chenmingpaper.com/xxlr.asp?tab=&menuid=241&menujb=3";
        String href = "FLMEN.ASP?MENULB=049新闻资讯&MENUJB=2";
        Map<String, String> charsetMap = new HashMap<String, String>();
        URI uri = UrlLinkUtil.getInstance().getURI(url, href, "GBK", charsetMap, "GBK");
        System.out.println(uri.toString());
        assert (uri.toString().equals("http://www.chenmingpaper.com/FLMEN.ASP?MENULB=049%D0%C2%CE%C5%D7%CA%D1%B6&MENUJB=2"));
    }

    @Test
    public void test2() {
        //测试URL解析
        String url = "http://mp3.sogou.com/tag.so?query=%u4F24%u611F&w=02200000";
        try {
            URL uri = UrlLinkUtil.getInstance().getURL("http://mp3.sogou.com/", url);
        } catch (Exception ex) {
            ex.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void test3() {
        //测试URL和URI解析URL
        String str = "http://www.chenmingpaper.com/xxlr.asp?tab=&menuid=241&menujb=3";
        try {
            URI uri = UrlLinkUtil.getInstance().getURI("http://www.chenmingpaper.com/", str);
            URL url = UrlLinkUtil.getInstance().getURL("http://www.chenmingpaper.com/", str);
            String rk1 = UrlLinkUtil.getInstance().convertUriToRowKey(uri, "siteId");
            String rk2 = UrlLinkUtil.getInstance().convertUrlToRowKey(url, "siteId");
            System.out.println(rk1);
            System.out.println(rk2);
            assert (rk1.equals(rk2));
        } catch (Exception ex) {
            ex.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void testResolveURL() {
        //测试URL解析
        String url = "http://house.chinadaily.com.cn/2012-02/27/content_14701935.htm";
        try {
            URL resolved = UrlLinkUtil.getInstance().getURL(url, "content_14701935_2.htm");
            System.out.println(resolved.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void testMustAndDontHave() {
        String url = "http://house.chinadaily.com.cn/2012-02/27/content_14701935.htm";
        String[] mustHave = {"house.chinadaily.com.cn", ".*\\.htm$", "content_"};
        String[] dontHave = {"special"};
        assert (UrlLinkUtil.getInstance().match(url, mustHave, dontHave));
        String[] mustHave2 = {"http://\\w+.chinadaily.com.cn/\\d{4}-\\d{2}/\\d{2}/content.*htm", "content_"};
        assert (UrlLinkUtil.getInstance().matchOnePattern(url, mustHave2, null));
    }
}
