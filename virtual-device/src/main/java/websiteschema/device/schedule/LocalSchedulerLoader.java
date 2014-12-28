/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.device.schedule;

import org.quartz.SchedulerException;
import org.quartz.Trigger;
import java.util.Map;
import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import websiteschema.device.DeviceContext;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.CronScheduleBuilder.*;

/**
 *
 * @author ray
 */
public class LocalSchedulerLoader implements SchedulerLoader {

    Logger l = Logger.getLogger(LocalSchedulerLoader.class);
    private final static java.util.Random random = new java.util.Random();
    private final String group = "group1";

    @Override
    public void load(Scheduler sched) {
        try {
            Map<String, String> all = listJobsAndSchedules();
            addScheduleAndJob(sched, all);
        } catch (Exception ex) {
            l.error(ex.getMessage(), ex);
        }
    }

    private Map<String, String> listJobsAndSchedules() {
        return DeviceContext.getInstance().getConf().getAllPropertiesInField("Jobs");
    }

    private void addScheduleAndJob(Scheduler sched, Map<String, String> all) throws SchedulerException {
        if (null != all) {
            for (String fbName : all.keySet()) {
                String resource = "fb" + "/" + fbName + ".app";
                String schedule = all.get(fbName);
                JobDetail job = createJob(resource, group);
                Trigger trigger = createTrigger(resource, schedule, group);
                if (null != job && null != trigger) {
                    sched.scheduleJob(job, trigger);
                    l.debug("add job: " + job.getKey() + " and next fire time: " + trigger.getNextFireTime());
                }
            }
        }
    }

    private JobDetail createJob(String resource, String group) {
        try {
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("resource", resource);

            // 创建JobDetail
            JobDetail job =
                    newJob(JobApp.class).
                    withIdentity(resource, group).
                    usingJobData(jobDataMap).
                    build();
            return job;
        } catch (Exception ex) {
            l.error(ex);
        }
        return null;
    }

    private Trigger createTrigger(String resource, String sche, String group) {
        try {
            String schedule = getRandomSec() + " " + sche;
            Trigger trigger =
                    newTrigger().
                    withIdentity(resource, group).
                    withSchedule(cronSchedule(schedule)).
                    forJob(resource, group).
                    startNow().
                    build();
            return trigger;
        } catch (Exception ex) {
            l.error(ex.getMessage(), ex);
        }
        return null;
    }

    private int getRandomSec() {
        return random.nextInt(60);
    }
}
