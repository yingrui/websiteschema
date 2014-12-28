package websiteschema.example;

import java.util.Properties;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import websiteschema.example.fb.Not;
import websiteschema.fb.core.Application;
import websiteschema.fb.core.ApplicationService;
import websiteschema.fb.core.RuntimeContext;
import websiteschema.fb.osgi.FBFactoryService;

public class Activator implements BundleActivator, ServiceListener {

    ApplicationService service = null;
    BundleContext bundleContext = null;

    public void start(BundleContext bundleContext) throws Exception {
        this.bundleContext = bundleContext;
        ServiceReference[] refs = bundleContext.getServiceReferences(ApplicationService.class.getName(), "(ApplicationService=*)");
        if (null != refs && refs.length > 0) {
            System.out.println("ApplicationManager started already!");
            service = (ApplicationService) bundleContext.getService(refs[0]);
            registerFBFactoryService(bundleContext);
            addApplication(service);
        } else {
            bundleContext.addServiceListener(this, "(objectClass=" + ApplicationService.class.getName() + ")");
            System.out.println("wait for register event of ApplicationService.");
        }
    }

    public void addApplication(ApplicationService service) {
        Application app = new Application();
        RuntimeContext runtimeContext = app.getContext();
        runtimeContext.loadConfigure(Activator.class.getClassLoader().getResourceAsStream("fb/test.app"));

        Application app2 = new Application();
        runtimeContext = app2.getContext();
        runtimeContext.loadConfigure(Activator.class.getClassLoader().getResourceAsStream("fb/crawler.app"));

//        service.startup(app);
        service.startup(app2);
    }

    public void stop(BundleContext context) {
        // NOTE: The service is automatically released.
        System.out.println("Good bye!");
    }

    public FBFactoryService registerFBFactoryService(BundleContext bundleContext) {
        FBFactoryService serv = new BundleFBFactoryService();
        Properties props = new Properties();
        props.put(Not.class.getName(), "1.0-SNAPSHOT");
        bundleContext.registerService(FBFactoryService.class.getName(), serv, props);
        return serv;
    }

    public void serviceChanged(ServiceEvent se) {
        if (se.getType() == ServiceEvent.REGISTERED) {
            if (service == null) {
                // Get a reference to the service object.
                ServiceReference ref = se.getServiceReference();
                service = (ApplicationService) bundleContext.getService(ref);
                if (null != service) {
                    System.out.println("ApplicationManager already started!");
                    registerFBFactoryService(bundleContext);
                    addApplication(service);
                }
            }
        }
    }
}
