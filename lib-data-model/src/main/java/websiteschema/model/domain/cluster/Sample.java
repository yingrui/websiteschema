/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain.cluster;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import websiteschema.model.domain.HBaseBean;
import websiteschema.persistence.hbase.annotation.ColumnFamily;
import websiteschema.persistence.hbase.annotation.RowKey;

/**
 *
 * @author ray
 */
public class Sample implements HBaseBean {

    @RowKey
    String rowKey;
    @ColumnFamily
    String url;
    @ColumnFamily(family = "c")
    DocUnits content;
    @ColumnFamily
    int httpStatus = 0;
    @ColumnFamily
    Date lastUpdateTime = new Date();
    @ColumnFamily
    Date createTime;
    @ColumnFamily
    String siteId;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    @Override
    public String getRowKey() {
        return rowKey;
    }

    @Override
    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public DocUnits getContent() {
        return content;
    }

    public void setContent(DocUnits content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Unit[] getUnits(String xpath) {
        List<Unit> ret = new ArrayList<Unit>();

        Unit[] units = getContent().getUnits();
        for (Unit unit : units) {
            if (xpath.equals(unit.getXpath())) {
                ret.add(unit);
            }
        }

        return ret.toArray(new Unit[0]);
    }
}
