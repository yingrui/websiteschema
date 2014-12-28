/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.schedule.stat;

import java.util.*;
import websiteschema.model.domain.Schedule;
import websiteschema.model.domain.Task;
import websiteschema.persistence.rdbms.ScheduleMapper;
import websiteschema.persistence.rdbms.StartURLMapper;
import websiteschema.persistence.rdbms.TaskMapper;

/**
 *
 * @author ray
 */
public class JobStatistic {

    ScheduleMapper scheduleMapper;
    StartURLMapper startURLMapper;
    TaskMapper taskMapper;
    List<Record> result;

    public JobStatistic(ScheduleMapper scheduleMapper, StartURLMapper startURLMapper, TaskMapper taskMapper) {
        this.scheduleMapper = scheduleMapper;
        this.startURLMapper = startURLMapper;
        this.taskMapper = taskMapper;
    }

    public List<Record> getResult() {
        return result;
    }

    public Schedule getSchedule(Record rec) {
        return scheduleMapper.getById(rec.scheId);
    }

    public void saveSchedule(Schedule sche) {
        scheduleMapper.update(sche);
    }

    public List<Record> stat() {
        List scheStatus = taskMapper.getScheduleStatus();
        List sitesAndJobnames = startURLMapper.getSitesAndJobnames();
        result = stat(scheStatus, sitesAndJobnames);
        return result;
    }

    private List<Record> stat(List<Map> scheStatus, List<Map> sitesAndJobnames) {
        Map<String, Record> res = new HashMap<String, Record>();
        for (Map map : sitesAndJobnames) {
            String jobname = (String) map.get("jobname");
            String site = (String) map.get("siteId");
            String name = (String) map.get("name");
            Long scheId = (Long) map.get("id");
            Integer status = (Integer) map.get("status");
            Record r = new Record();
            r.jobname = jobname;
            r.site = site;
            r.name = name;
            r.scheId = null != scheId ? scheId : -1;
            r.scheStatus = null != status ? status : Schedule.STATUS_INVALID;
            String key = scheId != null ? scheId.toString() : jobname;
            res.put(key, r);
        }

        for (Map map : scheStatus) {
            Long scheId = (Long) map.get("scheId");
            Integer taskType = (Integer) map.get("taskType");
            Integer status = (Integer) map.get("status");
            Long count = (Long) map.get("count");
            Record r = res.get(scheId.toString());
            if (null != r) {
                if (taskType == Task.TYPE_LINK) {
                    r.scheTimes += count;
                } else if (status == Task.FINISHED) {
                    r.fetchResults += count;
                }
                if (status != Task.FINISHED) {
                    r.unfinished += count;
                } else {
                    r.finished += count;
                }
            }
        }
        List<Record> ret = new ArrayList<Record>(res.size());
        for (String key : res.keySet()) {
            ret.add(res.get(key));
        }
        return ret;
    }
}
