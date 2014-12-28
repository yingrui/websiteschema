/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.hbase.core;

import websiteschema.persistence.hbase.annotation.ColumnFamily;
import java.io.IOException;
import java.lang.reflect.Field;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import websiteschema.model.domain.HBaseBean;

/**
 *
 * @author ray
 */
public class HBaseMapperFactory {

    private static final HBaseMapperFactory instance = new HBaseMapperFactory();
    private Logger l = Logger.getLogger(HBaseMapperFactory.class);
    private HBaseAdmin admin;

    public static HBaseMapperFactory getInstance() {
        return instance;
    }

    HBaseMapperFactory() {
        Configuration conf = HBaseConfiguration.create();
        try {
            admin = new HBaseAdmin(conf);
        } catch (MasterNotRunningException ex) {
            l.error("MasterNotRunningException", ex);
        } catch (ZooKeeperConnectionException ex) {
            l.error("ZooKeeperConnectionException", ex);
        }
    }

    public boolean checkTableWhetherExists(String tableName, Class clazz) {
        try {
            if (!admin.tableExists(tableName)) {
                l.debug("表 " + tableName + " 不存在！");
                return false;
            } else {
                l.debug("表 " + tableName + " 已经存在！");
                return true;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void createTableIfNotExists(String tableName, Class clazz) {
        try {
            if (!admin.tableExists(tableName)) {

                HTableDescriptor tableDesc = new HTableDescriptor(tableName);
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(ColumnFamily.class)) {
                        ColumnFamily cf = field.getAnnotation(ColumnFamily.class);
                        String familyName = null != cf.family() && !"".equals(cf.family())
                                ? cf.family() : field.getName();
                        System.out.println("-------- Family name: " + familyName);
                        tableDesc.addFamily(new HColumnDescriptor(familyName));
                    }
                }
                admin.createTable(tableDesc);
                l.debug("表 " + tableName + " 创建成功！");
            } else {
                l.debug("表 " + tableName + " 已经存在！");
                Field[] fields = clazz.getDeclaredFields();
                admin.disableTable(tableName);
                HTableDescriptor tableDesc = admin.getTableDescriptor(Bytes.toBytes(tableName));
                for (Field field : fields) {
                    if (field.isAnnotationPresent(ColumnFamily.class)) {
                        ColumnFamily cf = field.getAnnotation(ColumnFamily.class);
                        String familyName = null != cf.family() && !"".equals(cf.family())
                                ? cf.family() : field.getName();
                        if (!tableDesc.hasFamily(Bytes.toBytes(familyName))) {
                            admin.addColumn(tableName, new HColumnDescriptor(field.getName()));
                            l.debug("表 " + tableName + " 添加了列： " + field.getName());
                        }
                    }
                }
                admin.enableTable(tableName);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteTable(HBaseMapper mapper) {
        try {
            admin.disableTable(mapper.getTableName());
            admin.deleteTable(mapper.getTableName());
            l.debug("删除表 " + mapper.getTableName() + " 成功！");
        } catch (IOException ex) {
            l.error("Table " + mapper.getTableName(), ex);
        }
    }
}
