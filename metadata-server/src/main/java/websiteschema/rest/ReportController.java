/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.rest;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import websiteschema.persistence.rdbms.ScheduleMapper;
import websiteschema.persistence.rdbms.StartURLMapper;
import websiteschema.persistence.rdbms.TaskMapper;
import websiteschema.schedule.stat.JobStatistic;
import websiteschema.schedule.stat.Record;
import websiteschema.schedule.stat.SchedulePlan;

/**
 *
 * @author ray
 */
@Controller
@RequestMapping(value = "/report")
public class ReportController {

    Logger l = Logger.getRootLogger();
    @Autowired
    ScheduleMapper scheduleMapper;
    @Autowired
    StartURLMapper startURLMapper;
    @Autowired
    TaskMapper taskMapper;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void start(HttpServletResponse response) throws IOException {
        JobStatistic stat = new JobStatistic(scheduleMapper, startURLMapper, taskMapper);
        List<Record> records = stat.stat();
        if (null != records && !records.isEmpty()) {
            send(records, response);
        }
    }

    @RequestMapping(value = "/reschedule", method = RequestMethod.GET)
    public void reschedule(HttpServletResponse response) throws IOException {
        JobStatistic stat = new JobStatistic(scheduleMapper, startURLMapper, taskMapper);
        stat.stat();
        SchedulePlan plan = new SchedulePlan(stat);
        plan.plan();
        ServletOutputStream out = response.getOutputStream();
        out.println("{success:true}");
        out.flush();
        out.close();
    }

    private void send(List<Record> records, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition",
                "attachment;filename=websiteschema-report.csv");

        ServletOutputStream out = response.getOutputStream();
        String encoding = "GBK";
        String title = "\"网站\",\"Jobname\",\"栏目名称\",\"是否配置\",\"状态\",\"调度次数\",\"采集数据\",\"执行成功率\",\"建议采集频率\",\"需关注\"";
        out.write((title + "\r\n").getBytes(encoding));
        for (Record r : records) {
            out.write((r.toString() + "\r\n").getBytes(encoding));
        }

        out.flush();
        out.close();
    }
}
