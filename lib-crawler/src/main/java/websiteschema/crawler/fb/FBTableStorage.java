package websiteschema.crawler.fb;

import websiteschema.fb.annotation.*;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.utils.DateUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;


@EI(name = {"SAVE:SAVE"})
@EO(name = {"EO"})
@Description(desc = "存储表格内容")
public class FBTableStorage extends FunctionBlock {

    @DI(name = "DATA_SOURCE")
    public String beanName = "dataSource";

    @DI(name = "JDBC_URL")
    public String jdbcUrl;
    @DI(name = "JDBC_DRIVER", desc = "JDBC Driver")
    public String jdbcDriver = "com.mysql.jdbc.Driver";
    @DI(name = "USR", desc = "User name")
    public String username;
    @DI(name = "PW", desc = "Password")
    public String password;

    @DI(name = "EMPTY_VALUE")
    public String emptyValue = "---";

    @DI(name = "TABLE_NAME")
    public String tableName;

    @DI(name = "TABLE")
    public List<Map<String, String>> table = null;

    @DI(name = "COLUMNS")
    public Map<String, String> columns = null;

    private javax.sql.DataSource dataSource = null;

    @Algorithm(name = "SAVE")
    public void save() {
        Connection conn = getConnection();
        if (null == conn) {
            this.triggerEvent("EO");
            return;
        }

        try {
            Statement statement = conn.createStatement();
            String createdAt = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
            for(Map<String,String> row: table) {
                String sql = "INSERT INTO " + tableName + " " + getInsertSQL(row, createdAt);
                l.debug(sql);
                statement.execute(sql);
            }
            statement.close();
        } catch (Exception e) {
            l.error(e.getMessage(), e);
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.triggerEvent("EO");
        }
    }

    private Connection getConnection() {
        try {
            Connection connection = null;
            if(beanName != null) {
                dataSource = getContext().getSpringBean(beanName, javax.sql.DataSource.class);
                connection = dataSource.getConnection();
            } else {
                Class.forName(jdbcDriver).newInstance();
                connection = DriverManager.getConnection(jdbcUrl, username, password);
            }
            initiateTable(connection);
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initiateTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        try {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " " +
                                    "(" + getTableFieldDescription() + ")";
            l.debug(createTableSQL);
            statement.execute(createTableSQL);
        } finally {
            statement.close();
        }
    }

    private String getTableFieldDescription() {
        StringBuilder str = new StringBuilder();

        for (String columnName : columns.keySet()) {
            String column = columns.get(columnName);
            str.append(column).append(" VARCHAR(1024) NULL,");
        }

        str.append("CREATED_AT VARCHAR(100) NOT NULL");

        return str.toString();
    }

    private String getInsertSQL(Map<String, String> row, String createdAt) {
        StringBuilder columnNames = new StringBuilder();
        StringBuilder values = new StringBuilder();

        for (String field : row.keySet()) {
            String columnName = columns.get(field);
            if (columnName != null) {
                columnNames.append(columnName).append(",");
                values.append("'").append(getValue(row, field)).append("',");
            }
        }
        columnNames.append("CREATED_AT");
        values.append("'").append(createdAt).append("'");

        return "(" + columnNames.toString() + ") VALUES (" + values + ")";
    }

    private String getValue(Map<String, String> row, String field) {
        String value = row.get(field);
        return value.equals(emptyValue) ? "" : value;
    }
}
