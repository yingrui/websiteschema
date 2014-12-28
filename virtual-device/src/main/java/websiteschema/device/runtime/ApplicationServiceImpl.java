/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.device.runtime;

import websiteschema.fb.core.app.Application;
import websiteschema.fb.core.app.ApplicationService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;
import websiteschema.fb.core.app.AppStatus;
import websiteschema.model.domain.Task;
import websiteschema.persistence.rdbms.TaskMapper;

/**
 *
 * @author ray
 */
@Deprecated
public class ApplicationServiceImpl implements ApplicationService {

    public static final int MaxTaskNumber = 200;
    List<Future<AppStatus>> fList = new ArrayList<Future<AppStatus>>();
    private ExecutorService pool = null;
    private ClassLoader classLoader = ApplicationServiceImpl.class.getClassLoader();
    private TaskMapper taskMapper = null;
    private final ReentrantLock lock = new ReentrantLock();
    java.util.Timer timer;
    java.util.TimerTask timerTask;
    private Logger l = Logger.getLogger(getClass());

    public ApplicationServiceImpl() {
        this(16);
    }

    public ApplicationServiceImpl(int poolSize) {
        init(poolSize);
    }

    private void init(int size) {
        pool = Executors.newFixedThreadPool(size, new ThreadFactory() {

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                t.setContextClassLoader(classLoader);
                return t;
            }
        });
        //初始化检查任务状态的线程。
        initTimer();
    }

    /**
     * 初始化一个线程，每隔30秒检查一次当前有多少任务在运行，并记录已经完成的任务的状态。
     */
    private void initTimer() {
        timer = new java.util.Timer(ApplicationServiceImpl.class.getName(), true);
        timerTask = new java.util.TimerTask() {

            @Override
            public void run() {
                getRunningThreadNumber();
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 30000);
    }

    private boolean addTask(Application task) {
        int Running = getRunningThreadNumber();
        if (Running >= MaxTaskNumber) {
            //当前任务数已经超过了设定的上限，拒绝添加新任务。
            return false;
        } else {
            boolean ret = true;
            Future f = null;
            try {
                f = pool.submit((Callable<AppStatus>) task);
                if (null != f) {
                    fList.add(f);
                    updateTaskStatusWhenStart(task.getStatus());
                }
            } catch (RejectedExecutionException ex) {
                //pool大小已经满了，拒绝添加新任务。
                ret = false;
            }
            return ret;
        }
    }

    public int getRunningThreadNumber() {
        lock.lock();
        try {
            for (Iterator<Future<AppStatus>> it = fList.iterator(); it.hasNext();) {
                Future<AppStatus> f = it.next();
                if (f.isDone()) {
                    it.remove();
                    try {
                        AppStatus res = f.get();
                        updateTaskStatus(res);
                    } catch (Exception ex) {
                    }
                }
            }
        } finally {
            lock.unlock();
        }
        return fList.size();
    }

    /**
     * 声明任务已经开始。
     * @param sta
     */
    private void updateTaskStatusWhenStart(AppStatus sta) {
        long taskId = null != sta ? sta.getTaskId() : 0;
        if (taskId > 0) {
            Task t = taskMapper.getById(taskId);
            if (null != t) {
                t.setStatus(Task.STARTED);
                if (null != sta.getMessage()) {
                    t.setMessage(sta.getMessage());
                }
                taskMapper.update(t);
            }
        }
    }

    /**
     * 如果任务id大于0，则根据功能块网络的执行情况，更新任务的状态。
     * @param sta
     */
    private void updateTaskStatus(AppStatus sta) {
        if (null != sta) {
            long taskId = sta.getTaskId();
            l.debug(" [x] Task " + taskId + " finished: " + sta.getMessage() + " with status: " + sta.getStatus());
            if (taskId > 0) {
                Task t = taskMapper.getById(taskId);
                if (null != t) {
                    if (sta.getStatus() == AppStatus.END) {
                        t.setStatus(Task.FINISHED);
                    } else {
                        t.setStatus(Task.EXCEPTION);
                    }
                    if (null != sta.getMessage()) {
                        t.setMessage(sta.getMessage());
                    }
                    taskMapper.update(t);
                }
            }
        } else {
            l.debug(" [x] Task finished: " + sta);
        }
    }

    public void shutdown() {
        if (null != pool) {
            pool.shutdown();
        }
    }

    public boolean isTerminated() {
        return pool.isTerminated();
    }

    public boolean isShutdown() {
        return pool.isShutdown();
    }

    public void setTaskMapper(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Override
    public boolean startup(Application app) {
        return addTask(app);
    }

    @Override
    public int getTotalTasks() {
        return getRunningThreadNumber();
    }
}
