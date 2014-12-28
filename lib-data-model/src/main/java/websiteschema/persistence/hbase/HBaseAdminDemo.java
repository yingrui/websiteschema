/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author ray
 */
public class HBaseAdminDemo {

    Configuration conf;

    HBaseAdminDemo() {
        conf = HBaseConfiguration.create();
    }

    /**
     * 创建表操作
     * @throws IOException
     */
    public void createTable(String tablename, String[] cfs) throws IOException {
        HBaseAdmin admin = new HBaseAdmin(conf);
        if (admin.tableExists(tablename)) {
            System.out.println("表已经存在！");
        } else {
            HTableDescriptor tableDesc = new HTableDescriptor(tablename);
            for (int i = 0; i < cfs.length; i++) {
                tableDesc.addFamily(new HColumnDescriptor(cfs[i]));
            }
            admin.createTable(tableDesc);
            System.out.println("表创建成功！");
        }
    }

    /**
     * 删除表操作
     * @param tablename
     * @throws IOException
     */
    public void deleteTable(String tablename) throws IOException {
        try {
            HBaseAdmin admin = new HBaseAdmin(conf);
            admin.disableTable(tablename);
            admin.deleteTable(tablename);
            System.out.println("表删除成功！");
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入一行记录
     * @param tablename
     * @param cfs
     */
    public void writeRow(String tablename, String rowKey, Map<String, String> record) {
        try {
            HTable table = new HTable(conf, tablename);
            Put put = new Put(Bytes.toBytes(rowKey));
            for (String key : record.keySet()) {
                put.add(Bytes.toBytes(key),
                        Bytes.toBytes(String.valueOf(1)),
                        Bytes.toBytes(record.get(key)));
                table.put(put);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一行记录
     * @param tablename
     * @param rowkey
     * @throws IOException
     */
    public void deleteRow(String tablename, String rowkey) throws IOException {
        HTable table = new HTable(conf, tablename);
        List list = new ArrayList();
        Delete d1 = new Delete(rowkey.getBytes());
        list.add(d1);
        table.delete(list);
        System.out.println("删除行成功！");
    }

    /**
     * 查找一行记录
     * @param tablename
     * @param rowkey
     */
    public void selectRow(String tablename, String rowKey)
            throws IOException {
        HTable table = new HTable(conf, tablename);
        Get g = new Get(rowKey.getBytes());
        Result rs = table.get(g);
        if (!rs.isEmpty()) {
            for (KeyValue kv : rs.raw()) {
                System.out.print(new String(kv.getRow()) + "  ");
                System.out.print(new String(kv.getFamily()) + ":");
                System.out.print(new String(kv.getQualifier()) + "  ");
                System.out.print(kv.getTimestamp() + "  ");
                System.out.println(new String(kv.getValue()));
            }
        } else {
            System.out.println("这是空行: " + rowKey);
        }
    }

    /**
     * 查询表中所有行
     * @param tablename
     */
    public void scaner(String tablename) {
        try {
            HTable table = new HTable(conf, tablename);
            Scan s = new Scan();
            ResultScanner rs = table.getScanner(s);
            for (Result r : rs) {
                KeyValue[] kv = r.raw();
                for (int i = 0; i < kv.length; i++) {
                    System.out.print(new String(kv[i].getRow()) + "  ");
                    System.out.print(new String(kv[i].getFamily()) + ":");
                    System.out.print(new String(kv[i].getQualifier()) + "  ");
                    System.out.print(kv[i].getTimestamp() + "  ");
                    System.out.println(new String(kv[i].getValue()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询表中所有行
     * @param tablename
     */
    public void scaner(String tablename, Filter filter) {
        try {
            HTable table = new HTable(conf, tablename);
            Scan s = new Scan();
            s.setFilter(filter);
            ResultScanner rs = table.getScanner(s);
            for (Result r : rs) {
                KeyValue[] kv = r.raw();
                for (int i = 0; i < kv.length; i++) {
                    System.out.print(new String(kv[i].getRow()) + "  ");
                    System.out.print(new String(kv[i].getFamily()) + ":");
                    System.out.print(new String(kv[i].getQualifier()) + "  ");
                    System.out.print(kv[i].getTimestamp() + "  ");
                    System.out.println(new String(kv[i].getValue()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        HBaseAdminDemo admin = new HBaseAdminDemo();
//        admin.deleteTable("test");
//
//        String cfs[] = {"cf", "cf2", "cf3"};
//        admin.createTable("test", cfs);

        Map<String, String> record = new HashMap<String, String>();
        record.put("cf", "a");
        record.put("cf", "b");
        record.put("cf2", "c");
        record.put("cf3", "d");
        admin.writeRow("test", "row2", record);

        admin.scaner("test");
//        admin.scaner("test");

        admin.selectRow("test", "row2");
        admin.deleteRow("test", "row2");
        admin.selectRow("test", "row2");
    }
}
