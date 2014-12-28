/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.device.update;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author ray
 */
public class UpdateExecuteJob implements Job {

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        UpdateExecutorFactory.createUpdateExecutor().execute();
    }
}
