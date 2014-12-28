/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.core.app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;

/**
 *
 * @author ray
 */
public class ApplicationManager implements ApplicationService {

    public static final long MaxTaskNumber = 100;
    List<Future<AppStatus>> fList = new ArrayList<Future<AppStatus>>();
    private int poolSize = 5;
    private ExecutorService pool = null;
    private ClassLoader classLoader = ApplicationManager.class.getClassLoader();

    public ApplicationManager() {
        init(poolSize);
    }

    private void init(int size) {
        pool = Executors.newFixedThreadPool(size, new ThreadFactory() {

            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                t.setContextClassLoader(classLoader);
                return t;
            }
        });
    }

    public boolean addTask(Callable<AppStatus> task) {
        int Running = getRunningThreadNumber();
        if (Running > MaxTaskNumber) {
            return false;
        } else {
            boolean ret = true;
            Future f = null;
            try {
                f = pool.submit((Callable<AppStatus>) task);
                if (null != f) {
                    fList.add(f);
                }
            } catch (RejectedExecutionException ex) {
                ret = false;
            }
            return ret;
        }
    }

    public int getTotalTasks() {
        return getRunningThreadNumber();
    }

    public int getRunningThreadNumber() {
        for (Iterator<Future<AppStatus>> it = fList.iterator(); it.hasNext();) {
            Future<AppStatus> f = it.next();
            if (f.isDone()) {
                it.remove();
                try {
                    AppStatus res = f.get();
                } catch (Exception ex) {
                }
            }
        }
        return fList.size();
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

    public boolean startup(Application app) {
        return addTask(app);
    }
}
