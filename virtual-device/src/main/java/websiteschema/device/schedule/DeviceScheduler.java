/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.device.schedule;

import org.apache.log4j.Logger;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author ray
 */
public class DeviceScheduler {

    public final static int Error = 0;
    public final static int Started = 1;
    public final static int Stopped = 2;
    public final static int Standby = 3;
    private final Logger l = Logger.getLogger(DeviceScheduler.class);
    private Scheduler sched = null;
    // 创建调度者工厂
    private SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    public DeviceScheduler() {
    }

    public void init() throws SchedulerException {
        sched = schedulerFactory.getScheduler();
    }

    public void startup() throws SchedulerException {
        sched.start();
    }

    public Scheduler getSched() {
        return sched;
    }

    public boolean isStarted() throws SchedulerException {
        return Started == status();
    }

    public void remove(JobKey jobKey) throws SchedulerException {
        if (isStarted()) {
            if (null != jobKey) {
                if (null != jobKey) {
                    sched.deleteJob(jobKey);
                    l.debug("delete job: " + jobKey);
                }
            } else {
                l.debug("jobKey is invalid.");
            }
        }
    }

    public int status() throws SchedulerException {
        if (null != sched) {
            if (sched.isShutdown()) {
                return Stopped;
            } else if (sched.isStarted()) {
                return Started;
            } else if (sched.isInStandbyMode()) {
                return Standby;
            }
        }
        return Error;
    }
}
