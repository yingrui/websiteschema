/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain;

import websiteschema.element.XPathAttributes;
import java.util.Date;
import java.util.Map;
import websiteschema.model.domain.cralwer.CrawlerSettings;
import websiteschema.persistence.hbase.annotation.ColumnFamily;
import websiteschema.persistence.hbase.annotation.RowKey;

/**
 *
 * @author ray
 */
public class Websiteschema implements HBaseBean {

    public final static int Analyzed = 1;
    public final static int New = 0;
    //SiteId
    @RowKey
    String rowKey;
    @ColumnFamily
    Date lastUpdateTime = new Date();
    @ColumnFamily
    Date createTime = null;
    @ColumnFamily
    boolean valid = true;
    @ColumnFamily
    int status = New;
    @ColumnFamily
    Map<String, Integer> dimension;
    @ColumnFamily
    XPathAttributes xpathAttr;
    @ColumnFamily
    CrawlerSettings crawlerSettings;
    @ColumnFamily
    Map<String, String> properties;

    @Override
    public String getRowKey() {
        return rowKey;
    }

    @Override
    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Map<String, Integer> getDimension() {
        return dimension;
    }

    public void setDimension(Map<String, Integer> dimension) {
        this.dimension = dimension;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public XPathAttributes getXpathAttr() {
        return xpathAttr;
    }

    public void setXpathAttr(XPathAttributes xpathAttr) {
        this.xpathAttr = xpathAttr;
    }

    public CrawlerSettings getCrawlerSettings() {
        return crawlerSettings;
    }

    public void setCrawlerSettings(CrawlerSettings crawlerSettings) {
        this.crawlerSettings = crawlerSettings;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
