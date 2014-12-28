package websiteschema.device;

import java.util.Properties;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import websiteschema.fb.core.Application;
import websiteschema.fb.core.ApplicationManager;
import websiteschema.fb.core.ApplicationService;
import websiteschema.fb.core.RuntimeContext;
import websiteschema.fb.osgi.OSGiFBFactory;
import websiteschema.fb.utils.FBUtil;

public class Activator implements BundleActivator {

    public void start(BundleContext bundleContext) throws Exception {
        // Query for all service references matching any language.
        ServiceReference[] refs = bundleContext.getServiceReferences(ApplicationService.class.getName(), "(ApplicationService=*)");
        if (null != refs && refs.length > 0) {
            System.out.println("ApplicationManager started already!");
            ApplicationService service = (ApplicationService) bundleContext.getService(refs[0]);
        } else {
            ApplicationService service = registerApplicationService(bundleContext);

            FBUtil util = FBUtil.getInstance();
            OSGiFBFactory factory = new OSGiFBFactory();
            factory.setBundleContext(bundleContext);
            util.registerFBFactory(factory);
            System.out.println("ApplicationManager started!");
        }
    }

    public ApplicationService registerApplicationService(BundleContext bundleContext) {
        Properties props = new Properties();
        props.put("ApplicationService", ApplicationManager.class.getName());
        ApplicationService service = new ApplicationManager();
        bundleContext.registerService(ApplicationService.class.getName(), service, props);
        return service;
    }

    public void add(ApplicationService service) {
        Application app = new Application();
        RuntimeContext runtimeContext = app.getContext();
        runtimeContext.loadConfigure(Activator.class.getClassLoader().getResourceAsStream("fb/test.app"));
        service.startup(app);
    }

    public void stop(BundleContext context) {
        // NOTE: The service is automatically released.
        System.out.println("Good bye!");
    }
}
