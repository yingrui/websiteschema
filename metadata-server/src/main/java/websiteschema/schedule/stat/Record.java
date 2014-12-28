/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.schedule.stat;

import java.text.DecimalFormat;
import websiteschema.model.domain.Schedule;

/**
 *
 * @author ray
 */
public class Record {

    String site;
    String jobname;
    String name;
    long scheId = -1;
    int scheStatus = Schedule.STATUS_INVALID;
    int scheTimes = 0;
    int fetchResults = 0;
    int finished = 0;
    int unfinished = 0;

    public boolean isConfig() {
        return scheId >= 0;
    }

    public boolean isScheduleValid() {
        return scheId >= 0 && scheStatus == Schedule.STATUS_VALID;
    }

    public double correctRate() {
        return (double) finished / (double) (scheTimes + fetchResults);
    }

    public boolean shouldDecreaseScheduleFreq() {
        if (isConfig()) {
            if (scheTimes > 0 && finished > 0) {
                double freq = (double) fetchResults / (double) (scheTimes);
                return freq < 0.1;
            }
        }
        return false;
    }

    public boolean shouldAttention() {
        return fetchResults == 0 && isConfig();
    }

    @Override
    public String toString() {
        boolean isConfig = isConfig();
        String ret = "\"" + site + "\",\"" + jobname + "\",\"" + name
                + "\",\"" + (isConfig ? "已配置" : "未配置")
                + "\",\"" + (isScheduleValid() ? "有效" : "无效")
                + "\",\"" + scheTimes + "\",\"" + fetchResults + "\"";
        if (scheTimes > 0 && isConfig) {
            double pre = correctRate();
            DecimalFormat df = new DecimalFormat();
            String pat = "0.00 %";
            df.applyPattern(pat);
            ret += ",\"" + df.format(pre) + "\"";
            double freq = (double) fetchResults / (double) (scheTimes);
            if (freq < 0.1) {
                ret += ",\"降低\"";
            } else {
                ret += ",\"高\"";
            }
        } else {
            ret += ",\"" + 0 + "\"";
            ret += ",\"降低\"";
        }

        if (shouldAttention()) {
            ret += ",\"是\"";
        } else {
            ret += ",\"否\"";
        }
        return ret;
    }
}
