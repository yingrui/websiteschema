/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.example;

import websiteschema.fb.core.FunctionBlock;
import websiteschema.fb.core.RuntimeContext;
import websiteschema.fb.osgi.FBFactoryService;

/**
 *
 * @author ray
 */
public class BundleFBFactoryService implements FBFactoryService {

    public FunctionBlock create(String fbName, RuntimeContext context) {
        String fbType = context.getConfig().getProperty(fbName, "FBType");
        try {
            Class clazz = Class.forName(fbType);
            FunctionBlock fb = (FunctionBlock) clazz.newInstance();
            fb.setName(fbName);
            fb.setContext(context);
            return fb;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
