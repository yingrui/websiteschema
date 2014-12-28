/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.util.List;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import websiteschema.model.domain.Schedule;

/**
 *
 * @author ray
 */
public class SchedulerTest {

    ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-beans.xml");
    ScheduleMapper schedulerMapper = ctx.getBean("scheduleMapper", ScheduleMapper.class);
    long startURLId = 1;

    @Test
    public void test() {
        insert();
        selectAndUpdate();
        delete();
    }

    public void insert() {
        Schedule scheduler = new Schedule();
        scheduler.setJobId(1);
        scheduler.setStartURLId(startURLId);
        scheduler.setSchedule("2 * * * *");
        schedulerMapper.insert(scheduler);

        scheduler = new Schedule();
        scheduler.setJobId(1);
        scheduler.setStartURLId(startURLId);
        scheduler.setSchedule("3 * * * *");
        schedulerMapper.insert(scheduler);
    }

    public void selectAndUpdate() {
        List<Schedule> url = schedulerMapper.getSchedulesByStartURL(startURLId);

        for (Schedule scheduler : url) {
            schedulerMapper.update(scheduler);
        }
    }

    public void delete() {
        List<Schedule> url = schedulerMapper.getSchedulesByStartURL(startURLId);

        for (Schedule scheduler : url) {
            schedulerMapper.delete(scheduler);
        }
    }
}
