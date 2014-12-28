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
public class ConcernedWeibo implements java.io.Serializable {
    long id;
    String name;
    int objectType = 0;
    String title;
    String siteId;
    String weiboURL;
    String org;
    String notes;
    String certification;
    int fans = 0;
    int follow = 0;
    int weibo = 0;
    Date createTime = new Date();
    String createUser = "system";
    Date updateTime = new Date();
    String lastUpdateUser = "system";

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public int getFollow() {
        return follow;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getWeibo() {
        return weibo;
    }

    public void setWeibo(int weibo) {
        this.weibo = weibo;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getWeiboURL() {
        return weiboURL;
    }

    public void setWeiboURL(String weiboURL) {
        this.weiboURL = weiboURL;
    }
    
}
