/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.core.app;

import java.util.concurrent.Callable;

/**
 *
 * @author ray
 */
public interface IApplication extends Runnable, Callable<AppStatus> {

    public long getTaskId();

    public AppStatus getStatus();
}
