package websiteschema.device;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.management.ManagementFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import websiteschema.common.amqp.QueueFactory;
import websiteschema.device.job.JobWorker;
import websiteschema.device.schedule.DeviceScheduler;
import websiteschema.device.schedule.LocalSchedulerLoader;
import websiteschema.device.schedule.SchedulerLoader;
import websiteschema.device.update.DailyUpdateSchedulerLoader;
import websiteschema.fb.core.app.AppStatus;
import websiteschema.fb.core.app.Application;

public class VirtualDevice {

    private Server server = null;
    private DeviceScheduler sched = new DeviceScheduler();
    private boolean stop = false;
    private int poolSize = DeviceContext.getInstance().getConf().getIntProperty("Device", "PoolSize", 16);
    private Logger l = Logger.getLogger(getClass());
    private JobWorker[] jobWorkers = null;
    private Thread[] threads = null;
    private java.util.Timer timer;
    private java.util.TimerTask timerTask;

    public static void main(String[] args) throws Exception {
        VirtualDevice device = new VirtualDevice();
        device.writePID();
        device.startJetty();
        device.startWorker();
        device.startScheduler();
    }

    /**
     * 启动本地的任务调度器
     */
    public void startScheduler() {
        try {
            sched.init();
            SchedulerLoader loader1 = new LocalSchedulerLoader();
            loader1.load(sched.getSched());
            SchedulerLoader loader2 = new DailyUpdateSchedulerLoader();
            loader2.load(sched.getSched());
            sched.startup();
        } catch (Exception ex) {
            l.error(ex.getMessage(), ex);
        }
    }

    /**
     * 多线程接收和处理任务
     */
    public void startWorker() {
        InitRabbitQueue();

        jobWorkers = new JobWorker[poolSize];
        threads = new Thread[poolSize];
        for (int i = 0; i < poolSize; i++) {
            JobWorker worker = new JobWorker();
            jobWorkers[i] = worker;
            Thread t = new Thread(worker);
            t.setDaemon(true);
            t.setName("JobWorker-" + i);
            t.start();
            threads[i] = t;
        }
        initTimer();
    }

    private void InitRabbitQueue() {
        QueueFactory.getInstance().setConfig(DeviceContext.getInstance().getConf());
    }

    /**
     * 初始化一个线程，每隔30秒检查一次当前有多少任务在等待，并处理超时等待的线程。
     */
    private void initTimer() {
        timer = new java.util.Timer(getClass().getName(), true);
        timerTask = new java.util.TimerTask() {

            @Override
            public void run() {
                if (null != jobWorkers) {
                    long current = System.currentTimeMillis();
                    for (int i = 0; i < poolSize; i++) {
                        Thread thread = threads[i];
                        if (null != thread && thread.getState() == Thread.State.WAITING) {
                            //处理
                            JobWorker worker = jobWorkers[i];
                            Application app = worker.getApp();
                            if (null != app) {
                                AppStatus status = app.getStatus();
                                if (null != status) {
                                    long startTime = app.getStartTime();
                                    if (current - startTime > 600000) {
                                        // 如果线程已经开始10分钟了，并且还处于“等待”的状态
                                        // 将线程中断一下。
                                        l.info("Interrupt thread " + thread.getName());
                                        thread.interrupt();
                                    }
                                }
                            }
                        } else if (null == thread || !thread.isAlive()) {
                            if (null != thread) {
                                l.info(thread.getName() + " already dead.");
                            }
                            Thread t = new Thread(jobWorkers[i]);
                            t.setName("JobWorker-" + i);
                            t.start();
                            threads[i] = t;
                        }
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 30000);
    }

    public boolean isStop() {
        return stop;
    }

    public void stopDevice() {
        try {
            stopJetty();
        } catch (Exception ex) {
        }
        System.exit(0);
    }

    private String getPID() {
        String processName = ManagementFactory.getRuntimeMXBean().getName();
        return processName.substring(0, processName.indexOf("@"));
    }

    public void writePID() throws IOException {
        File f = new File("device.pid");
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(f));
        writer.write(String.valueOf(getPID()));
        writer.flush();
        writer.close();
    }

    public void startJetty() throws Exception {
        Handler handler = new AbstractHandler() {

            @Override
            public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch)
                    throws IOException, ServletException {
                String path = request.getPathInfo();
                System.out.println(path);
                if ("/action=reload".equalsIgnoreCase(path)) {
                    DeviceContext.getInstance().load();
                    response.setContentType("text/plain");
                    response.getWriter().println("{\"success\":\"true\"}");
                    response.setStatus(HttpServletResponse.SC_OK);
                    ((Request) request).setHandled(true);
                } else if ("/action=getpid".equalsIgnoreCase(path)) {
                    DeviceContext.getInstance().load();
                    response.setContentType("text/plain");
                    response.getWriter().println(getPID());
                    response.setStatus(HttpServletResponse.SC_OK);
                    ((Request) request).setHandled(true);
                } else if ("/action=getstatus".equalsIgnoreCase(path)) {
                    response.setContentType("text/xml");
                    response.getWriter().println(getStatus());
                    response.setStatus(HttpServletResponse.SC_OK);
                    ((Request) request).setHandled(true);
                } else if ("/action=stop".equalsIgnoreCase(path)) {
                    stopDevice();
                } else {
                    response.setContentType("text/xml");
                    response.getWriter().println(getUnknownAction(path));
                    response.setStatus(HttpServletResponse.SC_OK);
                    ((Request) request).setHandled(true);
                }
            }
        };

        server = new Server(DeviceContext.getInstance().getConf().getIntProperty("Device", "port", 12207));
        server.setHandler(handler);
        server.start();
    }

    public String getStatus() {
        StringBuilder sb = new StringBuilder();

        sb.append("<?xml version=\"1.0\"?>").
                append("<response>").
                append("<action>GETSTATUS</action>").
                append("<response>SUCCESS</response>").
                append("<responsedata>").
                append("<product>VIRTUAL-DEVICE</product>").
                append("<serviceport>").
                append(DeviceContext.getInstance().getConf().getProperty("Device", "port", "12207")).
                append("</serviceport>").
                append("<tasks>").
                append(getStartedTaskNumber()).
                append("</tasks>").
                append("</responsedata>").
                append("</response>");
        return sb.toString();
    }

    public int getStartedTaskNumber() {
        int c = 0;
        if (null != threads) {
            for (int i = 0; i < threads.length; i++) {
                Thread t = threads[i];
                if (t.isAlive()) {
                    c++;
                }
            }
        }
        return c;
    }

    public String getUnknownAction(String action) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\"?>").
                append("<response>").
                append("<action>").
                append(action).
                append("</action>").
                append("<response>ERROR</response>").
                append("<responsedata>").
                append("<error>").
                append("<errorstring>The action you attempted is not recognized</errorstring>").
                append("<errorcode>UNKNOWN</errorcode>").
                append("</error>").
                append("</responsedata>").
                append("</response>");
        return sb.toString();
    }

    public void stopJetty() throws Exception {
        server.stop();
    }
}
