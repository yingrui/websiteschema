/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.device.update;

import java.util.List;
import org.apache.log4j.Logger;
import websiteschema.common.update.IUpdateModule;
import websiteschema.device.DeviceContext;

/**
 *
 * @author ray
 */
public class UpdateExecutorFactory {

    private final static IUpdateExecutor ins = new UpdateExecutor();
    private final static Logger l = Logger.getLogger(UpdateExecutorFactory.class);

    static {
        // regist update modules.
        List<String> modules = DeviceContext.getInstance().getConf().getListProperty("Schedule", "UpdateModules");
        if (null != modules) {
            for (String className : modules) {
                try {
                    Class clazz = Class.forName(className);
                    IUpdateModule module = (IUpdateModule) clazz.newInstance();
                    boolean suc = ins.registryUpdateModule(module);
                    if (suc) {
                        l.info("Successfully registed UpdateModule: " + className);
                    } else {
                        l.info("Register update module " + className + " failed!");
                    }
                } catch (Exception ex) {
                    l.error(ex.getMessage(), ex);
                }
            }
        }
    }

    public static IUpdateExecutor createUpdateExecutor() {
        return ins;
    }
}
