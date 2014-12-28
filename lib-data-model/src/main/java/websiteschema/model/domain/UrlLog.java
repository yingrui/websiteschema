/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import websiteschema.persistence.hbase.annotation.ColumnFamily;
import websiteschema.persistence.hbase.annotation.RowKey;
import websiteschema.utils.UrlLinkUtil;

/**
 *
 * @author ray
 */
public class UrlLog implements HBaseBean {

    private static Pattern pat = Pattern.compile("([A-z0-9_ .]+)\\+([0-9:\\- ]+)\\+(.+)");
    @RowKey(desc = "使用jobname+Date+URL作为RowKey，URL的host部分要倒置")
    private String rowKey;
    @ColumnFamily
    private long createTime = System.currentTimeMillis();

    public UrlLog() {
    }

    public UrlLog(String jobname, String url) throws MalformedURLException {
        URL link = new URL(url);
        rowKey = UrlLinkUtil.getInstance().convertUrlToRowKey(link, jobname);
    }

    public UrlLog(String jobname, URL url) {
        rowKey = UrlLinkUtil.getInstance().convertUrlToRowKey(url, jobname);
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getRowKey() {
        return rowKey;
    }

    @Override
    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public String getJobname() {
        Matcher m = pat.matcher(rowKey);
        if (m.matches()) {
            return m.group(1);
        } else {
            return null;
        }
    }

    public String getURLRowKey() {
        Matcher m = pat.matcher(rowKey);
        if (m.matches()) {
            return m.group(3);
        } else {
            return null;
        }
    }

    public Date getCreateDate() {
        return new Date(createTime);
    }

    public static void main(String args[]) throws Exception {
        UrlLog log = new UrlLog("www_163_com._1", "http://mil.news.sohu.com/s2005/junshiguonei.shtml");
        System.out.println(log.getRowKey());
        System.out.println(log.getJobname());
        System.out.println(log.getURLRowKey());
        System.out.println(log.getCreateDate());
    }
}
