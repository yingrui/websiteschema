/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apc.websiteschema.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ray
 */
public class ImportSiteAndStartURLFromFMS {

    public static void main(String[] args) {
        List<Map<String, String>> sites = getSites("");
        insertSitesIntoWebsiteschema(sites);

        List<Map<String, String>> channels = getChannels("");
        insertChannelsIntoWebsiteschema(channels);
    }

    public static String getSiteDomain(String url) {
        String ret = url.replaceAll("http://", "");
        ret = ret.replaceAll("/.*", "");
        return ret;
    }

    public static void insertSitesIntoWebsiteschema(List<Map<String, String>> sites) {
        System.out.println("MySQL Connect Example.");
        Connection conn = null;
        String jdbc_url = "jdbc:mysql://localhost:3306/websiteschema";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "websiteschema";
        String password = "websiteschema";
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(jdbc_url, userName, password);
            conn.setAutoCommit(false);

            System.out.println("Connected to the database");
            Statement stmt = conn.createStatement();
            for (Map<String, String> map : sites) {
                String url = map.get("url");
                String siteDomain = getSiteDomain(url);
                String siteId = siteDomain.replaceAll("\\.", "_") + "_" + map.get("id");
                String siteName = map.get("sitename");
                String siteType = "portal";
                stmt.executeUpdate("INSERT INTO Site "
                        + "(siteId, siteName, url, siteDomain, siteType, parentId, createTime, createUser)"
                        + "VALUES ('"+siteId+"', '"+siteName+"','"+url+"','"+siteDomain+"','"+siteType+"',0,now(),'system')");
            }
            conn.commit();
            conn.close();
            System.out.println("Disconnected from database");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertChannelsIntoWebsiteschema(List<Map<String, String>> sites) {
        System.out.println("MySQL Connect Example.");
        Connection conn = null;
        String jdbc_url = "jdbc:mysql://localhost:3306/websiteschema";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "websiteschema";
        String password = "websiteschema";
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(jdbc_url, userName, password);
            conn.setAutoCommit(false);

            System.out.println("Connected to the database");
            Statement stmt = conn.createStatement();
            for (Map<String, String> map : sites) {
                String url = map.get("url");
                String siteDomain = getSiteDomain(url);
                String siteId = siteDomain.replaceAll("\\.", "_") + "_" + map.get("id");
                String startURL = map.get("starturl");
                String jobname = map.get("jobname");
                String name = map.get("name");
                stmt.executeUpdate("INSERT INTO StartURL "
                        + "(siteId, startURL, jobname, name, status, createTime, createUser)"
                        + "VALUES ('"+siteId+"', '"+startURL+"','"+jobname+"','"+name+"',0,now(),'system')");
            }
            conn.commit();
            conn.close();
            System.out.println("Disconnected from database");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Map<String, String>> getSites(String id) {
        System.out.println("Oracle Connection");
        Connection conn = null;
        String url = "jdbc:oracle:thin:@10.8.0.160:1521:fms";
        String driver = "oracle.jdbc.driver.OracleDriver";
        String userName = "fcm";
        String password = "fcm";
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, userName, password);
            System.out.println("Connected to the database");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT SOURCE_ID id, SOURCE_NAME siteName, SOURCE_URL url FROM SOURCE_INFO WHERE SOURCE_ID IN (4762,4497,4481,4740,4749,4755,4754,4473,4758,4747,4476,6472,6461,6418,4993,4991,4900,4989,4988,4845,4842,4841,4839,4501,4466,4020,17562,6417,5689,5688,4843,4797,4760,4756,4750,4499,4475,4465,4745,4752,6310,4492,4373,4763,10640,5688,6488,5050,5721,17568,20831)");

            List<Map<String, String>> ret = listFromRS(rs);

            conn.close();
            System.out.println("Disconnected from database");
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Map<String, String>> getChannels(String id) {
        System.out.println("Oracle Connection");
        Connection conn = null;
        String url = "jdbc:oracle:thin:@10.8.0.160:1521:fms";
        String driver = "oracle.jdbc.driver.OracleDriver";
        String userName = "fcm";
        String password = "fcm";
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, userName, password);
            System.out.println("Connected to the database");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT J.BIZ_JOB jobname, J.CHANNEL_NAME name, J.CHANNEL_URL startURL, J.SOURCE_ID id, S.SOURCE_URL url FROM JOB_INFO J left join SOURCE_INFO S on J.SOURCE_ID = S.SOURCE_ID WHERE J.VALID=1 AND J.SOURCE_ID IN (4762,4497,4481,4740,4749,4755,4754,4473,4758,4747,4476,6472,6461,6418,4993,4991,4900,4989,4988,4845,4842,4841,4839,4501,4466,4020,17562,6417,5689,5688,4843,4797,4760,4756,4750,4499,4475,4465,4745,4752,6310,4492,4373,4763,10640,5688,6488,5050,5721,17568,20831)");

            List<Map<String, String>> ret = listFromRS(rs);

            conn.close();
            System.out.println("Disconnected from database");
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Map<String, String>> listFromRS(ResultSet rs) throws SQLException {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        int fieldCount = rs.getMetaData().getColumnCount();
        String fields[][] = new String[fieldCount][2];

        for (int i = 0; i < fieldCount; i++) {
            fields[i][0] = rs.getMetaData().getColumnLabel(i + 1);
            fields[i][1] = rs.getMetaData().getColumnLabel(i + 1).toLowerCase();
        }

        if (rs != null) {
            while (rs.next()) {
                Map<String, String> map = new LinkedHashMap<String, String>();
                for (int i = 0; i < fields.length; i++) {
                    map.put(fields[i][1], rs.getString(fields[i][0]));
                }
                list.add(map);
            }
        }

        return list;
    }
}
