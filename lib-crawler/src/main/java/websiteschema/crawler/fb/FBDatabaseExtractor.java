/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import websiteschema.cluster.analyzer.Doc;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.utils.CollectionUtil;
import websiteschema.utils.DbUtil;
import websiteschema.utils.StringUtil;

/**
 *
 * @author ray
 */
@EO(name = {"EO", "EMPTY"})
@EI(name = {"EI:EXT"})
public class FBDatabaseExtractor extends FunctionBlock {

    @DI(name = "SQL")
    public String sql;
    @DI(name = "PK")
    public String pk = "ID";
    @DI(name = "UPDATE_SQL", desc = "当数据获取之后，更新标志位的SQL，ex: update table set status = 1")
    public String updateSQL = "update Results_sina set Flag = 0";
    @DI(name = "JDBC_URL")
    public String jdbcUrl;
    @DI(name = "JDBC_DRIVER", desc = "JDBC Driver")
    public String jdbcDriver = "com.mysql.jdbc.Driver";
    @DI(name = "USR", desc = "User name")
    public String username;
    @DI(name = "PW", desc = "Password")
    public String password;
    @DI(name = "TAG_DATE", desc = "Fetch Date")
    public String tagDreDate = "DREDATE";
    @DO(name = "DOCS", relativeEvents = {"EO"})
    public List<Doc> docs;

    @Algorithm(name = "EXT")
    public void execute() {
        Connection conn = null;
        try {
            Class.forName(jdbcDriver).newInstance();
            conn = DriverManager.getConnection(jdbcUrl, username, password);
            conn.setAutoCommit(false);

            l.debug("Connected to the database");
            Statement stmt = conn.createStatement();

            l.debug(sql);
            ResultSet rs = stmt.executeQuery(sql);
            List<Map<String, String>> res = DbUtil.listFromRS(rs);
            if (null != res && !res.isEmpty()) {
                docs = new ArrayList<Doc>();
                List<String> keys = new ArrayList<String>();
                for (Map<String, String> map : res) {
                    docs.add(convert(map)); //转换Map成Doc，然后添加到DO.DOC
                    String key = map.get(pk);
                    keys.add(key);
                }
                String sql2 = updateSQL + " where " + pk + " in (" + CollectionUtil.toString(keys) + ")";
                l.debug(sql2);
                stmt.executeUpdate(sql2);
                conn.commit();
            }
        } catch (Exception e) {
            l.error(e.getMessage(), e);
            if (null != conn) {
                try {
                    conn.rollback();
                } catch (Exception ex) {
                    l.error(ex.getMessage(), ex);
                }
            }
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (null != conn) {
                    conn.close();
                    l.debug("Disconnected from database");
                }
            } catch (Exception ex) {
                l.error(ex.getMessage(), ex);
            }
        }
        if (null != docs && !docs.isEmpty()) {
            triggerEvent("EO");
        } else {
            triggerEvent("EMPTY");
        }
    }

    private Doc convert(Map<String, String> map) {
        Doc ret = new Doc();
        for (String key : map.keySet()) {
            String value = map.get(key);
            ret.addField(key, value);
        }
        String date = ret.getValue(tagDreDate);
        if (!StringUtil.isNotEmpty(date)) {
            ret.addField(tagDreDate, String.valueOf(System.currentTimeMillis() / 1000));
        }
        return ret;
    }
}
