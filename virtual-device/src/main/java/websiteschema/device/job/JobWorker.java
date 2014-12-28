/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.device.job;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import websiteschema.common.amqp.Message;
import websiteschema.common.amqp.QueueFactory;
import websiteschema.common.amqp.RabbitQueue;
import websiteschema.common.base.Function;
import websiteschema.device.DeviceContext;
import websiteschema.device.handler.WrapperHandler;
import websiteschema.fb.core.app.Application;
import websiteschema.fb.core.RuntimeContext;
import websiteschema.fb.core.app.AppStatus;
import websiteschema.model.domain.Task;
import websiteschema.model.domain.Wrapper;
import websiteschema.persistence.rdbms.TaskMapper;
import websiteschema.utils.StringUtil;
import websiteschema.utils.ThreadUtil;

/**
 *
 * @author ray
 */
public class JobWorker implements Runnable {

    private RabbitQueue<Message> priorQueue;
    private RabbitQueue<Message> queue;
    private boolean isStop = false;
    private Logger l = Logger.getLogger(JobWorker.class);
    private String host = null;
    private int port = -1;
    private String queueName = null;
    private String priorQueueName = null;
    private TaskMapper taskMapper = null;
    private Application app = null;

    public JobWorker() {
        host = DeviceContext.getInstance().getConf().
                getProperty("URLQueue", "ServerHost", "localhost");
        port = DeviceContext.getInstance().getConf().
                getIntProperty("URLQueue", "ServerPort", -1);
        priorQueueName = DeviceContext.getInstance().getConf().
                getProperty("URLQueue", "PriorQueueName", "url_queue");
        queueName = DeviceContext.getInstance().getConf().
                getProperty("URLQueue", "QueueName", "");
        priorQueue = QueueFactory.getInstance().getQueue(host, port, priorQueueName);
        if (StringUtil.isNotEmpty(queueName)) {
            queue = QueueFactory.getInstance().getQueue(host, port, queueName);
        }
        taskMapper = DeviceContext.getBean("taskMapper", TaskMapper.class);
    }

    public void stop() {
        isStop = true;
    }

    public Application getApp() {
        return app;
    }

    @Override
    public void run() {

        Function<Message> handler = new Function<Message>() {

            /**
             * 保存消息，然后将消息添加到AppRuntime中运行。
             */
            @Override
            public void invoke(Message msg) {
                // 获取Wrapper
                long wrapperId = msg.getWrapperId();
                Wrapper wrapper = WrapperHandler.getInstance().getWrapper(wrapperId);
                // 创建任务并执行
                createApplication(msg, wrapper);
            }
        };

        while (!isStop) {
            Message message = null;
            // 首先等待优先队列中的消息
            if (null != priorQueue) {
                message = priorQueue.poll(Message.class, 1000, handler);
                if (null != message) {
                    continue;
                }
            }
            // 如果优先队列中没有等到消息，继续等待普通队列中的消息。
            if (null != queue) {
                int count = 0;
                message = queue.poll(Message.class, 10000, handler);
                while (null != message && ++count < 3) {
                    message = queue.poll(Message.class, 10000, handler);
                }
            }

            // 如果所有的Queue中都没有消息，则等待30秒
            if (null == message) {
                ThreadUtil.sleep(30000);
            }
        }
    }

    private void createApplication(Message msg, Wrapper wrapper) {
        if (Wrapper.TYPE_FB.equals(wrapper.getWrapperType())) {
            l.debug("create application and execute it.");
            app = new Application(msg.getTaskId());
            // 功能块网络运行5分钟超时
            int timeout = DeviceContext.getInstance().getConf().
                    getIntProperty("Device", "AppTimeout", 5 * 60 * 1000);
            app.setTimeout(timeout);
            RuntimeContext runtimeContext = app.getContext();
            String appConfig = wrapper.getApplication();
            InputStream is = convertToInputStream(appConfig);
            if (null != is) {
                //加载Wrapper，将Job的配置转为Map，并设置为Filter。
                runtimeContext.loadConfigure(is, convertToMap(msg));
                try {
                    AppStatus status = app.getStatus();
                    updateTaskStatusWhenStart(status);
                    status = app.call();
                    updateTaskStatus(status);
                } catch (Exception ex) {
                    l.error(ex.getMessage(), ex);
                }
            }
            app = null;
        }
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

    public InputStream convertToInputStream(String content) {
        try {
            return new ByteArrayInputStream(content.getBytes("UTF-8"));
        } catch (Exception ex) {
            l.error(ex.getMessage(), ex);
            return null;
        }
    }

    private Map<String, String> getDefaultConfig() {
        return DeviceContext.getInstance().getConf().
                getAllPropertiesInField("FBApp");
    }

    public Map<String, String> convertToMap(Message msg) {
        try {
            Map<String, String> def = getDefaultConfig();
            Map<String, String> ret = null;
            // 首先添加配置文件中的默认配置
            if (null == def) {
                ret = new HashMap<String, String>();
            } else {
                ret = new HashMap<String, String>(def);
            }
            // 添加根据jobId和起始URL等参数配置
            String url = msg.getUrl();
            String siteId = msg.getSiteId();
            String jobname = msg.getJobname();
            if (null != url) {
                ret.put("URL", url);
            }
            if (null != siteId) {
                ret.put("SITEID", siteId);
            }
            if (null != jobname) {
                ret.put("JOBNAME", jobname);
            }
            ret.put("STARTURLID", String.valueOf(msg.getStartURLId()));
            ret.put("JOBID", String.valueOf(msg.getJobId()));
            ret.put("SCHEID", String.valueOf(msg.getScheId()));
            ret.put("WRAPPERID", String.valueOf(msg.getWrapperId()));
            // 添加默认的RabbitQueue服务器和队列名称
            ret.put("QUEUE_SERVER", host);
            ret.put("QUEUE_NAME", priorQueueName);
            // 添加由msg中接收到的参数
            String properties = msg.getConfigure();
            InputStream is = convertToInputStream(properties);
            Properties prop = new Properties();
            prop.load(is);
            for (String key : prop.stringPropertyNames()) {
                ret.put(key, prop.getProperty(key));
            }

            return ret;
        } catch (Exception ex) {
            l.error(ex.getMessage(), ex);
        }
        return null;
    }
}
