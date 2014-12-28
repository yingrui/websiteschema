/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.schedule.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import websiteschema.model.domain.Task;
import websiteschema.persistence.rdbms.TaskMapper;
import websiteschema.utils.DateUtil;

/**
 *
 * @author ray
 */
public class JobCheckTask implements Job {

    private long jobId;
    private long schedulerId;
    private TaskMapper taskMapper;
    private int before = 2;
    Logger l = Logger.getLogger(JobCheckTask.class);
    private static final int hour = 60 * 60 * 1000;

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        Task task = new Task(schedulerId);
        task.setStatus(Task.STARTED);
        taskMapper.insert(task);
        try {
            long now = System.currentTimeMillis();
            checkTimeoutTask(Task.STARTED, now);
            checkTimeoutTask(Task.SENT, now);
            checkTimeoutTask(Task.UNSENT, now);
            archive();
            task.setStatus(Task.FINISHED);
            taskMapper.update(task);
        } catch (Exception ex) {
            task.setStatus(Task.EXCEPTION);
            task.setMessage(ex.getMessage());
            taskMapper.update(task);
            ex.printStackTrace();
        }
    }

    private void archive() {
        Date now = new Date();
        Date past = DateUtil.addDate(now, -before);
        Date date = DateUtil.parseDate(DateUtil.format(past, "yyyy-MM-dd"), "yyyy-MM-dd");
        taskMapper.archive(date);
        taskMapper.batchDelete(date);
    }

    private void checkTimeoutTask(int status, long now) {
        Map param = new HashMap();
        param.put("status", status);
        Date d = new Date(System.currentTimeMillis() - hour);
        param.put("updateTime", d);
        List<Task> tasks = getTaskMapper().getTasks(param);
        List<Long> timeoutTasks = new ArrayList<Long>();
        for (Task task : tasks) {
            Date date = task.getUpdateTime();
            if (null != date) {
                long time = date.getTime();
                if (now - time > hour) {
                    timeoutTasks.add(task.getId());
                }
            }
        }
        //在数据库中将超时的任务置状态
        setTimeoutStatus(timeoutTasks);
    }

    private void setTimeoutStatus(List<Long> timeoutTasks) {
        if (null != timeoutTasks && !timeoutTasks.isEmpty()) {
            taskMapper.updateStatus(Task.TIMEOUT, timeoutTasks);
        }

    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public long getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(long schedulerId) {
        this.schedulerId = schedulerId;
    }

    public TaskMapper getTaskMapper() {
        return taskMapper;
    }

    public void setTaskMapper(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }
}
