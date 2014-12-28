/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.rest;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import websiteschema.conf.Configure;
import websiteschema.metadata.utils.MetadataServerContext;
import websiteschema.persistence.rdbms.ChannelMapper;
import websiteschema.persistence.rdbms.JobMapper;
import websiteschema.persistence.rdbms.ScheduleMapper;
import websiteschema.persistence.rdbms.StartURLMapper;
import websiteschema.persistence.rdbms.TaskMapper;
import websiteschema.persistence.rdbms.WrapperMapper;
import websiteschema.schedule.JobScheduler;

/**
 *
 * @author ray
 */
@Controller
@RequestMapping(value = "/scheduler")
public class SchedulerController {

    Logger l = Logger.getRootLogger();
    private static final JobScheduler scheduler = new JobScheduler();
    @Autowired
    ScheduleMapper scheduleMapper;
    @Autowired
    JobMapper jobMapper;
    @Autowired
    WrapperMapper wrapperMapper;
    @Autowired
    StartURLMapper startURLMapper;
    @Autowired
    TaskMapper taskMapper;
    @Autowired
    ChannelMapper channelMapper;

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public void start(HttpServletResponse response) throws IOException {
        boolean ok = start();
        if (ok) {
            response.getWriter().print("{success:true}");
        } else {
            response.getWriter().print("{success:false}");
        }
    }

    @PostConstruct
    public boolean start() throws IOException {
        try {  
            scheduler.setScheduleMapper(scheduleMapper);
            scheduler.setJobMapper(jobMapper);
            scheduler.setStartURLMapper(startURLMapper);
            scheduler.setWrapperMapper(wrapperMapper);
            scheduler.setTaskMapper(taskMapper);
            scheduler.setChannelMapper(channelMapper);   
           
            scheduler.setLocationId(MetadataServerContext.getInstance().getConf().getIntProperty("LocationId"));            
            int status = scheduler.status();
            if (JobScheduler.Stopped == status
                    || JobScheduler.Error == status) {
                l.info("load");                  
                scheduler.load();                 
                l.info("start");
                scheduler.startup();
            } else if (JobScheduler.Started == status) {
                // do nothing
                l.info("already started");
            } else if (JobScheduler.Standby == status) {
                l.info("start");
                scheduler.load();
                scheduler.startup();
            }
            return true;
        } catch (SchedulerException e) {
            l.error(e.getMessage(), e);
            return false;
        }
    }

    @RequestMapping(value = "/stop", method = RequestMethod.GET)
    public void stop(HttpServletResponse response) throws IOException {
        l.info("stop");
        boolean ok = stop();
        if (ok) {
            response.getWriter().print("{success:true}");
        } else {
            response.getWriter().print("{success:false}");
        }
    }

    public boolean stop() throws IOException {
        try {
            scheduler.shutdown();
            return true;
        } catch (SchedulerException e) {
            l.error(e.getMessage(), e);
            return false;
        }
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public void status(HttpServletRequest request, HttpServletResponse response) throws IOException {
        l.debug("status");
        try {
            int status = scheduler.status();
            response.getWriter().print("{success:true,status:" + status + "}");
        } catch (SchedulerException e) {
            l.error(e.getMessage(), e);
            response.getWriter().print("{success:false}");
        }
    }

    public static JobScheduler getScheduler() {
        return scheduler;
    }
}
