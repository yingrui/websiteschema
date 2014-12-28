/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.device.schedule;

import org.quartz.Scheduler;

/**
 *
 * @author ray
 */
public interface SchedulerLoader {

    public void load(Scheduler sched);
    
}
