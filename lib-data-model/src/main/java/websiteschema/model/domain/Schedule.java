/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain;

import java.util.Date;

/**
 *
 * @author ray
 */
public class Schedule implements java.io.Serializable {

    public final static int TYPE_CRON = 0;
    public final static int TYPE_START_END_TIMES = 1;
    public final static int STATUS_INVALID = 0;
    public final static int STATUS_VALID = 1;
    long id;
    long startURLId;
    String channelName;
    String startURL;
    String jobname;
    long jobId;
    long priority = 0;
    String schedule;
    int locationId = 4;
    int scheduleType = TYPE_CRON;
    int status = STATUS_INVALID;
    Date createTime = new Date();

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public long getStartURLId() {
        return startURLId;
    }

    public void setStartURLId(long startURLId) {
        this.startURLId = startURLId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(int scheduleType) {
        this.scheduleType = scheduleType;
    }

    public long getPriority() {
        return priority;
    }

    public void setPriority(long priority) {
        this.priority = priority;
    }

    public String getStartURL() {
        return startURL;
    }

    public void setStartURL(String startURL) {
        this.startURL = startURL;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getJobname() {
        return jobname;
    }

    public void setJobname(String jobname) {
        this.jobname = jobname;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }
}
