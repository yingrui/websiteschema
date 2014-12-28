/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.device.update;

import websiteschema.common.update.IUpdateModule;

/**
 *
 * @author ray
 */
public interface IUpdateExecutor {

    /**
     * Registry Update Module.
     * @param module
     * @return
     */
    public boolean registryUpdateModule(IUpdateModule module);

    /**
     * Unregistry Update Module.
     * @param module
     * @return
     */
    public boolean unregistryUpdateModule(IUpdateModule module);

    /**
     * Execute update process.
     */
    public void execute();

}
