package websiteschema.crawler.fb;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import websiteschema.conf.Configure;
import websiteschema.fb.core.RuntimeContext;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TableStorageTest {

    RuntimeContext context = new RuntimeContext();

    public TableStorageTest(){
        File file = new File("test.db");
        file.deleteOnExit();

        context.setConfig(new Configure("configure-site-sqlite.ini"));

        SaveTable();
    }

    @Test
    public void should_create_table_if_not_exists() throws SQLException {
        DriverManagerDataSource dataSource = context.getSpringBean("dataSource", DriverManagerDataSource.class);
        Connection conn = dataSource.getConnection();

        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT NAME FROM sqlite_master WHERE type='table' AND name='Fund'");
        Assert.assertTrue(resultSet.next());
        Assert.assertEquals("Fund", resultSet.getString("NAME"));
        statement.close();
        conn.close();
    }

    @Test
    public void should_save_specified_table() throws SQLException {
        DriverManagerDataSource dataSource = context.getSpringBean("dataSource", DriverManagerDataSource.class);
        Connection conn = dataSource.getConnection();

        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT SHORT_NAME, CODE FROM Fund");

        Assert.assertTrue(resultSet.next());
        Assert.assertEquals("163113", resultSet.getString("CODE"));
        Assert.assertEquals("申万证券行业", resultSet.getString("SHORT_NAME"));

        statement.close();
        conn.close();
    }

    private void SaveTable() {
        HashMap<String, String> columns = new HashMap<String, String>();
        columns.put("基金简称", "SHORT_NAME");
        columns.put("基金代码", "CODE");

        Map<String, String> row = new HashMap<String, String>();
        row.put("基金代码", "163113");
        row.put("基金简称", "申万证券行业");

        ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
        data.add(row);

        FBTableStorage storage = new FBTableStorage();
        storage.setContext(context);
        storage.beanName = "dataSource";
        storage.tableName = "Fund";
        storage.columns = columns;
        storage.table = data;

        storage.save();
    }

}
