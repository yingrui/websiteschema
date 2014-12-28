/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.factory;

import org.apache.log4j.Logger;
import websiteschema.fb.core.FBInfo;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.fb.core.RuntimeContext;
import websiteschema.fb.utils.FBUtil;

/**
 *
 * @author ray
 */
public class ClassLoaderFBFactory implements FBFactory {

    Logger l = Logger.getLogger(ClassLoaderFBFactory.class);

    public FunctionBlock create(String fbName, RuntimeContext context) {
        String fbType = context.getConfig().getProperty(fbName, "FBType");
        try {
            Class clazz = Class.forName(fbType);
            FunctionBlock fb = (FunctionBlock) clazz.newInstance();
            fb.setName(fbName);
            fb.setContext(context);

            return fb;
        } catch (ClassNotFoundException ex) {
            l.error(ex);
        } catch (InstantiationException ex) {
            l.error(ex);
        } catch (IllegalAccessException ex) {
            l.error(ex);
        }
        return null;
    }
}
