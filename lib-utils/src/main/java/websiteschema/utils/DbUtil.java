/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ray
 */
public class DbUtil {

    public static List<Map<String, String>> listFromRS(ResultSet rs) throws SQLException {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        int fieldCount = rs.getMetaData().getColumnCount();
        String fields[][] = new String[fieldCount][2];

        for (int i = 0; i < fieldCount; i++) {
            fields[i][0] = rs.getMetaData().getColumnLabel(i + 1);
            fields[i][1] = rs.getMetaData().getColumnLabel(i + 1);
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
