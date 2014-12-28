/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.device.update;

import websiteschema.common.update.IUpdateModule;
import java.util.*;

/**
 *
 * @author ray
 */
public class UpdateExecutor implements IUpdateExecutor {

    class UpdateModuleInfo {

        IUpdateModule module;
        long interval = day;
        long lastUpdateTime = System.currentTimeMillis();

        UpdateModuleInfo(IUpdateModule mod) {
            module = mod;
        }
    }
    private Map<String, UpdateModuleInfo> modules = new HashMap<String, UpdateModuleInfo>();
    private final int second = 1000;
    private final int min = 60 * second;
    private final int hour = 60 * min;
    private final int day = 24 * hour;

    @Override
    public boolean registryUpdateModule(IUpdateModule module) {
        try {
            String moduleName = module.getClass().getName();
            if (!modules.containsKey(moduleName)) {
                modules.put(moduleName, new UpdateModuleInfo(module));
            }
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    @Override
    public boolean unregistryUpdateModule(IUpdateModule module) {
        try {
            String moduleName = module.getClass().getName();
            if (modules.containsKey(moduleName)) {
                modules.remove(moduleName);
            }
        } catch (Exception ex) {
            return false;
        }
        return true;
    }


    @Override
    public void execute() {
        Set<String> setModuleName = modules.keySet();
        for (String moduleName : setModuleName) {
            UpdateModuleInfo modInfo = modules.get(moduleName);
            long now = System.currentTimeMillis();
            if (now - modInfo.lastUpdateTime >= modInfo.interval) {
                try {
                    modInfo.module.update();
                } catch (Exception ex) {
                } finally {
                    modInfo.lastUpdateTime = System.currentTimeMillis();
                }
            }
        }
    }
}
