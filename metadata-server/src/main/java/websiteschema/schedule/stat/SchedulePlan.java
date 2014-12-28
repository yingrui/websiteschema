/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.schedule.stat;

import java.util.List;
import websiteschema.model.domain.Schedule;

/**
 *
 * @author ray
 */
public class SchedulePlan {

    JobStatistic stat;
    private final static String defaultSchedule = "*/10 * * * ?";
    private final static String mostSlowestSche = " */7 * * ?";
    private final static String slowSche = "*/59 * * * ?";
    private final java.util.Random random = new java.util.Random();

    public SchedulePlan(JobStatistic stat) {
        this.stat = stat;
    }

    public void plan() {
        List<Record> result = stat.getResult();
        for (Record rec : result) {
            if (rec.isScheduleValid() && rec.shouldDecreaseScheduleFreq()) {
                Schedule sche = stat.getSchedule(rec);
                if (null != sche && Schedule.TYPE_CRON == sche.getScheduleType()) {
                    String plan = sche.getSchedule();
                    if (defaultSchedule.equals(plan) || slowSche.equals(plan)) {
                        if (rec.scheTimes > 100) {
                            if (0 == rec.fetchResults) {
                                sche.setSchedule(random.nextInt(60) + mostSlowestSche);
                                stat.saveSchedule(sche);
                            } else if (defaultSchedule.equals(plan)) {
                                sche.setSchedule(slowSche);
                                stat.saveSchedule(sche);
                            }
                        }
                    }
                }
            }
        }
    }
}
