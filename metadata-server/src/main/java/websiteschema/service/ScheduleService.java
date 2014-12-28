/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import org.quartz.SchedulerException;
import websiteschema.rest.SchedulerController;
import java.util.Map;
import websiteschema.dwr.response.ListRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websiteschema.model.domain.Schedule;
import websiteschema.persistence.rdbms.ScheduleMapper;
import static websiteschema.persistence.rdbms.utils.ParameterUtil.*;

/**
 *
 * @author ray
 */
@Service
public class ScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private SchedulerController schedulerController;

    public ListRange getResults(Map map) {
        ListRange listRange = new ListRange();
        Map params = buildParamWithInt(map, "start", "limit");
        listRange.setData(scheduleMapper.getSchedules(params).toArray());
        listRange.setTotalSize(scheduleMapper.getTotalResults(params));
        return listRange;
    }

    public Schedule getById(long id) {
        return scheduleMapper.getById(id);
    }

    @Transactional
    public void insert(Schedule sche) {         
        scheduleMapper.insert(sche);
    }

    @Transactional
    public void update(Schedule sche) {
        scheduleMapper.update(sche);
        Schedule old = scheduleMapper.getById(sche.getId());
        try {
            if (old.getStatus() == Schedule.STATUS_VALID
                    && sche.getStatus() == Schedule.STATUS_INVALID) {
                //原来的状态是有效，现在变成无效，需要删除调度器。
                SchedulerController.getScheduler().remove(sche);
            } else if (old.getStatus() == Schedule.STATUS_INVALID
                    && sche.getStatus() == Schedule.STATUS_VALID) {
                //原来的状态是无效，现在变成无效，需要增加Job至调度器
                SchedulerController.getScheduler().add(sche);
            } else if (sche.getStatus() == Schedule.STATUS_VALID) {
                //如果状态不变，则需要重新加载Job
                SchedulerController.getScheduler().reload(sche);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Transactional
    public void deleteRecord(Schedule sche) {
        System.out.println("delete " + sche.getId());
        scheduleMapper.delete(sche);
        try {
            SchedulerController.getScheduler().remove(sche);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean launchScheduler() throws IOException {
//        schedulerController.setScheduleMapper(scheduleMapper);
        return schedulerController.start();
    }

    public boolean shutdownScheduler() throws IOException {
//        schedulerController.setScheduleMapper(scheduleMapper);
        return schedulerController.stop();
    }

    public int getStatusOfScheduler() throws SchedulerException {
        return SchedulerController.getScheduler().status();
    }

    public boolean createTempJob(Schedule sche) {
        return SchedulerController.getScheduler().createTempJob(sche);
    }
    private static final Pattern pat = Pattern.compile("(.*)\\((0+)\\)(.*)");

    public String createBatchTempJob(long scheId, String url, int start, int end) {
        System.out.println("sche id: " + scheId + " url: " + url + " start: " + start + " end: " + end);
        Schedule sche = scheduleMapper.getById(scheId);
        if (null != sche) {
            Matcher m = pat.matcher(url);
            if (m.matches()) {
                String prefix = m.group(1);
                String suffix = m.group(3);
                String p = m.group(2);
                DecimalFormat df = new DecimalFormat();
                df.applyPattern(p);
                int count = 0;
                while (start <= end) {
                    String middle = df.format(start);
                    String u = prefix + middle + suffix;
                    System.out.println(u);
                    if (!SchedulerController.getScheduler().createTempJob(sche, u)) {
                        return "无法正确的加入调度: " + u;
                    }
                    start++;
                    count ++;
                }
                return "成功将" + count +"个任务加入调度";
            } else {
                return "输入的URL模板不正确";
            }
        }
        return "批量添加任务失败";
    }
}
