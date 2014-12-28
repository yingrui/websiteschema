/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.metadata.utils;

import org.apache.log4j.Logger;
import websiteschema.conf.Configure;
import websiteschema.persistence.rdbms.SysConfMapper;

/**
 *
 * @author ray
 */
public class MetadataServerContext {

    static private MetadataServerContext ins = new MetadataServerContext();

    public static MetadataServerContext getInstance() {
        return ins;
    }
    private Configure conf;
    private SysConfMapper propLoader = null;
    private Logger l = Logger.getLogger(MetadataServerContext.class);

    public MetadataServerContext() {
        load();
    }

    public void setPropLoader(SysConfMapper sysConfMapper) {
        this.propLoader = sysConfMapper;
    }

    public Configure getConf() {
        return conf;
    }

    public void reload() {
        load();
    }

    private void load() {
        try {
            conf = new Configure("configure-site.ini");
            if (null == conf) {
                l.error("Can not load configuration file: configure-site.ini");
            }
            conf.setPropLoader(propLoader);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
