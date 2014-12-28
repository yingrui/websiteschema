/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apc.websiteschema.fms;


/**
 *
 * @author mgd
 */
public class FMSSite {

    private long id;
    private String name;
    private long type_id1;
    private String bigKind;
    private long type_id2;

    public byte getCore() {
        return core;
    }

    public void setCore(byte core) {
        this.core = core;
    }

    public long getType_id2() {
        return type_id2;
    }

    public void setType_id2(long type_id2) {
        this.type_id2 = type_id2;
    }
    private byte core;
    private String url;
    private String smallKind;

    public String getBigKind() {
        return bigKind;
    }

    public void setBigKind(String bigKind) {
        this.bigKind = bigKind;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSmallKind() {
        return smallKind;
    }

    public void setSmallKind(String smallKind) {
        this.smallKind = smallKind;
    }


    public long getType_id1() {
        return type_id1;
    }

    public void setType_id1(long type_id1) {
        this.type_id1 = type_id1;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
