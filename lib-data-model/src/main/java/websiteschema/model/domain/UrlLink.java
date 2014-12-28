/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain;

import java.util.Date;
import websiteschema.persistence.hbase.annotation.ColumnFamily;
import websiteschema.persistence.hbase.annotation.RowKey;

/**
 *
 * @author ray
 */
public class UrlLink implements HBaseBean {

    public final static int STATUS_NEW = 0;
    public final static int Redirected = 1;
    @RowKey(desc = "URL的host部分要倒置")
    String rowKey;
    @ColumnFamily(family = "c")
    String content = null; // 采集到了，被抽取之后的结果
    @ColumnFamily
    int status = STATUS_NEW;
    @ColumnFamily
    String url = null;
    @ColumnFamily
    Date lastUpdateTime = new Date();
    @ColumnFamily
    Date createTime = null;
    @ColumnFamily
    String parent = null; //父链接的rowKey
    @ColumnFamily
    int depth = 0;
    @ColumnFamily
    int httpStatus = 0;
    @ColumnFamily
    String jobname = null; //JobName代表了起始URL

    @Override
    public String getRowKey() {
        return rowKey;
    }

    @Override
    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getJobname() {
        return jobname;
    }

    public void setJobname(String jobname) {
        this.jobname = jobname;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
