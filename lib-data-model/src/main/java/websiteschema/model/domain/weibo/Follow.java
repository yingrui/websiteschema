/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain.weibo;

import java.util.Date;

/**
 *
 * @author ray
 */
public class Follow implements java.io.Serializable {

    long id;
    long wid;
    String weibo;
    long cwid;
    String concernedWeibo;
    int status;
    Date createTime = new Date();

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getCwid() {
        return cwid;
    }

    public void setCwid(long cwid) {
        this.cwid = cwid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getWid() {
        return wid;
    }

    public void setWid(long wid) {
        this.wid = wid;
    }

    public String getConcernedWeibo() {
        return concernedWeibo;
    }

    public void setConcernedWeibo(String concernedWeibo) {
        this.concernedWeibo = concernedWeibo;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

}
