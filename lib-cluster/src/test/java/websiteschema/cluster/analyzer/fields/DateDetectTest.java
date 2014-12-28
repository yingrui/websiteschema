/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.cluster.analyzer.fields;

import org.junit.Test;
/**
 *
 * @author ray
 */
public class DateDetectTest {

    @Test
    public void testHasYMD() {
        String text = "2008年10月21日";
        assert(DateDetectUtil.getInstance().hasYMD(text));

        text = "2008.10.21";
        assert(DateDetectUtil.getInstance().hasYMD(text));

        text = "销售服务热线:800-918-6818  资本运营部";
        assert(!DateDetectUtil.getInstance().hasYMD(text));

        text = "朝鲜国防委员长金正日12月17日上午去世";
        assert(!DateDetectUtil.getInstance().hasYMD(text));
    }

    @Test
    public void testHasSourceOrAuthor() {
        String text = "2011年12月7日 来自:";
        assert(DateDetectUtil.getInstance().hasSourceOrAuthor(text));

        text = "  2012-01-02 09:13     &nbsp;    南方日报    &nbsp;";
        assert(DateDetectUtil.getInstance().hasSourceOrAuthor(text));

        text = "发布时间：2011.12.14 09:54&nbsp;&nbsp;&nbsp;&nbsp;来源：齐鲁晚报&nbsp;&nbsp;&nbsp;&nbsp;作者：齐鲁晚报";
        assert(DateDetectUtil.getInstance().hasSourceOrAuthor(text));
    }

    @Test
    public void testPublishDate() {
        String text = "发布时间：2011.12.14 09:54&nbsp;&nbsp;&nbsp;&nbsp;来源：齐鲁晚报&nbsp;&nbsp;&nbsp;&nbsp;作者：齐鲁晚报";
        assert(DateDetectUtil.getInstance().hasPublishDate(text));
    }

    @Test
    public void testHMS() {
        String text = "2011.12.14 09:54";
        assert(DateDetectUtil.getInstance().hasHMS(text));
        text = "2011.12.14 09:54:00";
        assert(DateDetectUtil.getInstance().hasHMS(text));
        text = "2011.12.14 09 ";
        assert(!DateDetectUtil.getInstance().hasHMS(text));
    }

    @Test
    public void testMultiYMD() {
        String text = "2011.12.14 09:54 2011.12.14 09:54 2011.12.14 09:54";
        assert(DateDetectUtil.getInstance().hasMultiYMD(text));
        text = "2011.12.14 09:54张三";
        assert(!DateDetectUtil.getInstance().hasMultiYMD(text));
        text = "2011年12月31日08:01";
        assert(!DateDetectUtil.getInstance().hasMultiYMD(text));
        text = "2011年12月31日08:012011年12月31日08:01";
        assert(DateDetectUtil.getInstance().hasMultiYMD(text));
    }

    @Test
    public void testParseDate() {
        String date = "2011.12.14 09:54 2011.11.14 09:54 2011.10.14 09:54";
        String pat = "yyyy.MM.dd HH:mm";
        String res = DateDetectUtil.getInstance().parseDate(date, pat, "yyyy-MM-dd");
        assert("2011-12-14".equals(res));
        date = "2012年1月1日21:36";
        pat = "yy年MM月dd日HH:mm";
        res = DateDetectUtil.getInstance().parseDate(date, pat, "yyyy-MM-dd HH:mm");
        assert("2012-01-01 21:36".equals(res));
        date = "012年1月1日 21:36";
        pat = "yy年MM月dd日 HH:mm";
        res = DateDetectUtil.getInstance().parseDate(date, pat, "yyyy-MM-dd HH:mm");
        assert("2012-01-01 21:36".equals(res));
        date = "2011年12月31日09:35";
        pat = "yy年MM月dd日";
        res = DateDetectUtil.getInstance().parseDate(date, pat, "yyyy-MM-dd");
        assert("2011-12-31".equals(res));

        res = DateDetectUtil.getInstance().parseDate("2011-11-30 09:25 来源: 中国证券报", "yyyy-MM-dd HH:mm:SS", "yyyy-MM-dd HH:mm:SS");
        
        date = "2012年3月22日 9点40分    ";
        pat = "yyyy年MM月dd日 HH点mm分";
        res = DateDetectUtil.getInstance().parseDate(date, pat, "yyyy-MM-dd HH:mm");
        assert("2012-03-22 09:40".equals(res));
    }

    @Test
    public void testDetectDate() {
        String date = "2011.12.14 09:54 2011.11.14 09:54 2011.10.14 09:54";
        String pat = DateDetectUtil.getInstance().detectPattern(date);
        System.out.println(pat);
        assert("yyyy.MM.dd HH:mm".equals(pat));
        
        date = "2012年1月1日21:36";
        pat = DateDetectUtil.getInstance().detectPattern(date);
        System.out.println(pat);
        assert("yyyy年MM月dd日HH:mm".equals(pat));

        date = "  2012-01-02 09:13     &nbsp;    南方日报    &nbsp;";
        pat = DateDetectUtil.getInstance().detectPattern(date);
        System.out.println(pat);
        assert("yyyy-MM-dd HH:mm".equals(pat));

        date = "2012年1月2日星期一";
        pat = DateDetectUtil.getInstance().detectPattern(date);
        System.out.println(pat);
        assert("yyyy年MM月dd日".equals(pat));
        
        date = "发布时间：2011.12.14 09:54&nbsp;&nbsp;&nbsp;&nbsp;来源：齐鲁晚报&nbsp;&nbsp;&nbsp;&nbsp;作者：齐鲁晚报";
        pat = DateDetectUtil.getInstance().detectPattern(date);
        System.out.println(pat);
        assert("yyyy.MM.dd HH:mm".equals(pat));

        date = "2011.12.14";
        pat = DateDetectUtil.getInstance().detectPattern(date);
        System.out.println(pat);
        assert("yyyy.MM.dd".equals(pat));

        date = "&nbsp;&nbsp;&nbsp;2010-3-12 10:52:02";
        pat = DateDetectUtil.getInstance().detectPattern(date);
        System.out.println(pat);
        assert("yyyy-MM-dd HH:mm:SS".equals(pat));
    }
}
