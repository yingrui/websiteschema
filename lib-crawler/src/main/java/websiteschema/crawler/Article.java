/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.w3c.dom.Document;
import websiteschema.cluster.analyzer.Link;
import websiteschema.crawler.fb.FBLinksExtractor;

/**
 * a data model containing several documents
 *
 * @author mgd
 */
@Deprecated
public class Article {

    private String urlFirstPage = null;
    private Map<String, Document> docsMap = null;
    private Iterator<String> it = null;

    public Article(String onePageUrl, Document doc) {

        this.docsMap = new TreeMap<String, Document>(new UrlCompator());
        this.docsMap.put(onePageUrl, null);

        FBLinksExtractor ext = new FBLinksExtractor();
        ext.in = doc;
        ext.xpath = "/html/body/*";
        ext.url = onePageUrl;
        ext.extract();

        List<Link> result = ext.links;
        if (null != result && !result.isEmpty()) {

            for (Link lnk : result) {
                String str = lnk.getHref();
                if (!docsMap.containsKey(str) && isNeededUrl(onePageUrl, str)) {
                    this.docsMap.put(str, null);
                }
            }
        }

        this.urlFirstPage = this.docsMap.keySet().iterator().next();
        this.it = this.docsMap.keySet().iterator();
    }

    public String getFirstUrl() {

        return this.urlFirstPage;
    }

     public boolean hasNext() {
        if (null != this.it) {
            return this.it.hasNext();
        } else {
            return false;
        }
    }

    public String getNextUrl() {

        return it.next();
    }

    public Document getPage(String key) {

        return this.docsMap.get(key);
    }

    public Document put(String key, Document value) {

        return this.docsMap.put(key, value);
    }

    public Document[] getDocuments() {
        int len = docsMap.size();
        Document[] docs = new Document[len];
        return docsMap.values().toArray(docs);
    }

    private boolean isNeededUrl(String pageUrl, String anyUrl) {
        if (null == pageUrl || null == anyUrl) {
            return false;
        }
        if (Math.abs(pageUrl.length() - anyUrl.length()) > 3) {
            return false;
        }
        int start;
        int end;
        int len1 = pageUrl.length();
        int len2 = anyUrl.length();
        String longerStr;
        if (len1 < len2) {
            start = pageUrl.length();
            end = anyUrl.length();
            longerStr = anyUrl;
        } else {
            start = anyUrl.length();
            end = pageUrl.length();
            longerStr = pageUrl;
        }
        for (int i = 0; i < start; ++i) {
            if (pageUrl.charAt(i) != anyUrl.charAt(i)) {
                start = i;
                break;
            }
        }
        while (len1 > 0 && len2 > 0) {
            if (pageUrl.charAt(--len1) != anyUrl.charAt(--len2)) {
                break;
            }
            --end;
        }

        if (end <= 0 || start <= 0 || start > end) {
            return false;
        } else {
            start -= 2;// if (start < 0)  return false;
            String diffStr = longerStr.substring(start, end);
            if (diffStr.matches(".{0,2}[-_=]([0-9]{1,2})")) {
                return true;
            } else {
                return false;
            }
        }
    }

    // the comparator used to sort the key of this.docsMap
    class UrlCompator implements Comparator<String> {

        @Override
        public int compare(String str1, String str2) {
            int minus = str1.length() - str2.length();
            if (minus == 0) {
                return str1.compareTo(str2);
            } else {
                return minus;
            }
        }
    }

    // 单元测试
    public static void main(String[] arg) {
        String str1 = "http://www.cien.com.cn/html/Home/report/60344-1.htm";
//        String str2 = "http://house.chinadaily.com.cn/2012-02/20/content_14644300_3.htm";
        String str2 = "http://www.cien.com.cn/html/Home/node/60344-1.htm";
        String str = Article.diffURL(str1, str2);
        System.out.println(str);
    }

    @Deprecated
    // 仅适用于两个正常URL进行对比的情况，不适用于任意两个字符串
    private static String diffURL(String str1, String str2) {
        if (null == str1) {
            return str2;
        } else if (null == str2) {
            return str1;
        } else if ("".equals(str1)) {
            return str2;
        } else if ("".equals(str2)) {
            return str1;
        }
        int start;
        int end;
        int len1 = str1.length();
        int len2 = str2.length();
        String longerStr;
        if (len1 < len2) {
            start = str1.length();
            end = str2.length();
            longerStr = str2;
        } else {
            start = str2.length();
            end = str1.length();
            longerStr = str1;
        }
        for (int i = 0; i < start; ++i) {
            if (str1.charAt(i) != str2.charAt(i)) {
                start = i;
                break;
            }
        }
        while (len1 > 0 && len2 > 0) {
            if (str1.charAt(--len1) != str2.charAt(--len2)) {
                break;
            }
            --end;
        }
        if (end <= 0 || start <= 0 || start > end) {
            return null;
        } else {
            return longerStr.substring(start, end);
        }
    }
}
