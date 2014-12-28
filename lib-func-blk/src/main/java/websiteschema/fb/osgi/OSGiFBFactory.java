/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.osgi;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.fb.core.RuntimeContext;
import websiteschema.fb.factory.FBFactory;

/**
 *
 * @author ray
 */
public class OSGiFBFactory implements FBFactory {

    BundleContext bundleContext;

    public String filter(String expr) {
        return "(" + expr + "=*)";
    }

    public FunctionBlock create(String fbName, RuntimeContext context) {
        String fbType = context.getConfig().getProperty(fbName, "FBType");
        try {
            System.out.println("create : " + fbName);
            String filter = filter(fbType);
            ServiceReference[] refs = bundleContext.getServiceReferences(FBFactoryService.class.getName(), filter);
            if (null != refs && refs.length > 0) {
                FBFactoryService service = (FBFactoryService) bundleContext.getService(refs[0]);
                return service.create(fbName, context);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }
}
