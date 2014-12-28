/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.device.schedule;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import websiteschema.device.DeviceContext;
import websiteschema.fb.core.RuntimeContext;
import websiteschema.fb.core.app.AppStatus;
import websiteschema.fb.core.app.Application;

/**
 *
 * @author ray
 */
public class JobApp implements Job {

    private String resource;
    private Logger l = Logger.getLogger(JobApp.class);
    private int timeout = DeviceContext.getInstance().getConf().
            getIntProperty("Device", "AppTimeout", 5 * 60 * 1000);

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        l.debug("execute" + jec.getJobDetail().getKey());
        Application app = new Application();
        RuntimeContext context = app.getContext();
        try {
            Map<String, String> map = getDefaultConfig();
            context.loadConfigure(resource, map);
            app.setTimeout(timeout);
            AppStatus status = app.call();
            l.debug(resource + " has finished with status: " + status.getStatus());
        } catch (Exception ex) {
            l.error(ex.getMessage(), ex);
        }
    }

    private Map<String, String> getDefaultConfig() {
        return DeviceContext.getInstance().getConf().
                getAllPropertiesInField("FBApp");
    }
}
