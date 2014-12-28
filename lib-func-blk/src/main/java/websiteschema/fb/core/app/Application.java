/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.core.app;

import java.util.Date;
import org.apache.log4j.Logger;
import websiteschema.conf.Configure;
import websiteschema.fb.core.Event;
import websiteschema.fb.core.FBInfo;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.fb.core.RuntimeContext;
import websiteschema.fb.core.compiler.ApplicationCompiler;

/**
 *
 * @author ray
 */
public class Application implements IApplication {

    Logger l = Logger.getLogger(Application.class);
    RuntimeContext context = new RuntimeContext();
    private boolean running = true;
    private long taskId = 0;
    private AppStatus status = new AppStatus();
    private long startTime = 0;
    private int timeout = -1;

    public Application() {
    }

    public Application(long taskId) {
        this.taskId = taskId;
        status.setTaskId(taskId);
    }

    public void stop() {
        running = false;
    }

    public RuntimeContext getContext() {
        return context;
    }

    public void run() {
        try {
            call();
        } catch (Exception ex) {
            l.error("FATAL ERROR: exit.", ex);
            status.setMessage("ERROR: " + ex.getMessage());
            status.setStatus(AppStatus.ERROR);
            //设置应用结束时间
            status.setEndTime(new Date());
        }
    }

    public long getTaskId() {
        return this.taskId;
    }

    public AppStatus getStatus() {
        return this.status;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public AppStatus call() throws Exception {
        ApplicationCompiler compiler = new ApplicationCompiler();
        compiler.setConfig(context.getConfig());
        try {
            compiler.compile();
        } catch (Exception ex) {
            ex.printStackTrace();
            status.setMessage("ERROR: " + ex.getMessage());
            status.setStatus(AppStatus.ERROR);
            return status;
        }

        startTime = System.currentTimeMillis();
        Configure config = context.getConfig();
        String initEvent = config.getProperty("InitEvent");
        FunctionBlock start = context.getStartFB();
        start.triggerEvent(initEvent);

        while (running) {
            try {
                Event evt = context.getEventQueue().poll();
                if (null != evt) {
                    FunctionBlock fb = evt.fb;
                    String ei = evt.ei;
                    if (null == fb && ei.equals(Event.CEASE_COMMAND)) {
                        stop();
                        status.setStatus(AppStatus.END);
                    } else {
                        if (null != fb) {
                            if (!fb.isWithECC()) {
                                Class clazz = fb.getClass();
                                FBInfo fbInfo = context.getFunctionBlockInfo(clazz);
                                String algorithm = fbInfo.getEIRelatedAlgorithm(ei);
                                fb.execute(algorithm, ei);
                            } else {
                                fb.executeEvent(ei);
                            }
                        } else {
                            stop();
                            status.setMessage("Invalid Event.");
                            status.setStatus(AppStatus.ERROR);
                        }
                    }
                } else {
                    l.trace("no more event to handle, waiting...");
                    Thread.sleep(100);
                }
                //如果设定了超时，则检查是否超时，如果超时则跳出循环
                if (timeout > 0) {
                    long now = System.currentTimeMillis();
                    long elaps = now - startTime;
                    if (timeout < elaps) {
                        status.setMessage("Execution time out: " + elaps);
                        status.setStatus(AppStatus.ERROR);
                        running = false;
                    }
                }
            } catch (Exception ex) {
                running = false;
                ex.printStackTrace();
                l.error("FATAL ERROR: exit.", ex);
                status.setMessage("ERROR: " + ex.getMessage());
                status.setStatus(AppStatus.ERROR);
            }
        }
        //设置应用结束时间
        status.setEndTime(new Date());
        return status;
    }
}
