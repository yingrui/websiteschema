/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb;

import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;

/**
 *
 * @author ray
 */
@EO(name = {"EO"})
@EI(name = {"INIT:INIT"})
public class Timer extends FunctionBlock {

    @DI(name = "interval")
    public int interval = 500;
    @DO(name = "times", relativeEvents = {"EO"})
    public int times = 0;
    java.util.Timer t;
    java.util.TimerTask task;

    public Timer() {
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
        t.cancel();
    }

    @Algorithm(name = "INIT")
    public void init() {
        t = new java.util.Timer(getName(), true);
        task = new java.util.TimerTask() {

            @Override
            public void run() {
                ++times;
                triggerEvent("EO");
            }
        };
        t.scheduleAtFixedRate(task, 0, interval);
    }
}
