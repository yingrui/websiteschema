/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.hbase.core;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.log4j.Logger;

/**
 *
 * @author ray
 */
public class HBaseConf {

    Configuration conf;
    Logger l = Logger.getLogger(HBaseConf.class);

    public HBaseConf() {
        conf = HBaseConfiguration.create();
        l.info("hbase.master: " + conf.get("hbase.master"));
        l.info("hbase.zookeeper.quorum: " + conf.get("hbase.zookeeper.quorum"));
    }
    private static HBaseConf ins = new HBaseConf();

    public static Configuration getHBaseConfiguration() {
        return ins.conf;
    }
}
