/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.device.update;

import websiteschema.device.DeviceContext;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import websiteschema.device.schedule.SchedulerLoader;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.CronScheduleBuilder.*;

/**
 *
 * @author ray
 */
public class DailyUpdateSchedulerLoader implements SchedulerLoader {

    Logger l = Logger.getLogger(DailyUpdateSchedulerLoader.class);
    private final static java.util.Random random = new java.util.Random();
    private final String group = "group_daily_update";

    @Override
    public void load(Scheduler sched) {
        try {
            String name = "job";
            String updateTime = DeviceContext.getInstance().getConf().getProperty("Schedule", "UpdateExecuteTime", "50 23 * * ?");
            JobDetail job = createJob(name, group);
            Trigger trigger = createTrigger(name, updateTime, group);
            if (null != job && null != trigger) {
                sched.scheduleJob(job, trigger);
                l.debug("add job: " + job.getKey() + " and next fire time: " + trigger.getNextFireTime());
            }
        } catch (Exception ex) {
            l.error(ex.getMessage(), ex);
        }
    }

    private JobDetail createJob(String name, String group) {
        try {
            JobDataMap jobDataMap = new JobDataMap();

            // 创建JobDetail
            JobDetail job =
                    newJob(UpdateExecuteJob.class).
                    withIdentity(name, group).
                    usingJobData(jobDataMap).
                    build();
            return job;
        } catch (Exception ex) {
            l.error(ex);
        }
        return null;
    }

    private Trigger createTrigger(String name, String sche, String group) {
        try {
            String schedule = random.nextInt(60) + " " + sche;
            Trigger trigger =
                    newTrigger().
                    withIdentity(name, group).
                    withSchedule(cronSchedule(schedule)).
                    forJob(name, group).
                    startNow().
                    build();
            return trigger;
        } catch (Exception ex) {
            l.error(ex.getMessage(), ex);
        }
        return null;
    }
}
