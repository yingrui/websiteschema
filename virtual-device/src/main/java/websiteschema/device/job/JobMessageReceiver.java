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
import websiteschema.model.domain.Wrapper;
import websiteschema.utils.ThreadUtil;

/**
 *
 * @author ray
 */
@Deprecated
public class JobMessageReceiver implements Runnable {

    private RabbitQueue<Message> priorQueue;
    private RabbitQueue<Message> queue;
    private boolean isStop = false;
    private Logger l = Logger.getLogger(JobMessageReceiver.class);
    private String host = null;
    private int port = -1;
    private String queueName = null;
    private String priorQueueName = null;
    private boolean threadPoolNotFull = false;

    public JobMessageReceiver() {
        host = DeviceContext.getInstance().getConf().
                getProperty("URLQueue", "ServerHost", "localhost");
        port = DeviceContext.getInstance().getConf().
                getIntProperty("URLQueue", "ServerPort", -1);
        priorQueueName = DeviceContext.getInstance().getConf().
                getProperty("URLQueue", "PriorQueueName", "url_queue");
        queueName = DeviceContext.getInstance().getConf().
                getProperty("URLQueue", "QueueName", "url_queue_1");
        priorQueue = QueueFactory.getInstance().getQueue(host, port, priorQueueName);
        queue = QueueFactory.getInstance().getQueue(host, port, queueName);
    }

    public void stop() {
        isStop = true;
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
                int restSize = 100;
                message = queue.poll(Message.class, 10000, handler);
                while (null != message && ++count < restSize && threadPoolNotFull) {
                    message = queue.poll(Message.class, 10000, handler);
                }
            }
        }
    }

    private void createApplication(Message msg, Wrapper wrapper) {
        if (Wrapper.TYPE_FB.equals(wrapper.getWrapperType())) {
            Application app = new Application(msg.getTaskId());
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
                boolean inserted = DeviceContext.getInstance().getAppRuntime().startup(app);
                threadPoolNotFull = inserted;
                while (!inserted) {
                    l.debug("thread pool is full.");
                    ThreadUtil.sleep(1000);
                    inserted = DeviceContext.getInstance().getAppRuntime().startup(app);
                }
            }
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
