/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb.index;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import websiteschema.common.amqp.QueueFactory;
import websiteschema.common.amqp.RabbitQueue;
import websiteschema.common.base.Function;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.utils.FileUtil;
import websiteschema.utils.PojoMapper;
import websiteschema.utils.StringUtil;

/**
 *
 * @author ray
 */
@EI(name = {"ADD:ADD", "SEND:RECEIVE_AND_SEND"})
@EO(name = {"EO"})
public class FBUnitIndexer extends FunctionBlock {

    @DI(name = "HOST", desc = "RabbitMQ server host", relativeEvents = {"ADD", "SEND"})
    public String host;
    @DI(name = "PORT", desc = "RabbitMQ server port", relativeEvents = {"ADD", "SEND"})
    public int port;
    @DI(name = "QUEUE", desc = "RabbitMQ server queue name", relativeEvents = {"ADD", "SEND"})
    public String queueName = "index_queue";
    @DI(name = "UNITS", desc = "需要发送至消息队列的数据", relativeEvents = {"ADD"})
    public List<Map<String, String>> units;
    // SEND Event
    @DI(name = "INDEX_CONTENT_CONSTRUCTOR", desc = "用来生成索引文件的组件", relativeEvents = {"SEND"})
    public String iccType = "websiteschema.crawler.fb.index.SimpleIndexContentConstructor";
    @DI(name = "TEMPDIR", desc = "存放临时urlKey的目录", relativeEvents = {"SEND"})
    public String tempDir = "temp";
    @DI(name = "INDEX_HOST", desc = "接受数据的服务器地址", relativeEvents = {"SEND"})
    public String indexHost = "dih.nyapc";
    @DI(name = "INDEX_PORT", desc = "接受数据的服务器地址", relativeEvents = {"SEND"})
    public int indexPort = 3001;
    @DI(name = "INDEX_CMD", desc = "接受数据的服务器地址", relativeEvents = {"SEND"})
    public String indexCommand = "/DREADDDATA";
    @DI(name = "INDEX_SUFFIX", desc = "接受数据的服务器地址", relativeEvents = {"SEND"})
    public String indexSuffix = "#DREENDDATANOOP";
    @DI(name = "INDEX_SUCCESS", desc = "接受数据的服务器地址", relativeEvents = {"SEND"})
    public String indexSuccessChars = "INDEXID";
    @DI(name = "IGNORE", desc = "在Unit中需要被忽略的标签", relativeEvents = {"SEND"})
    public List<String> ignoreTags = null;
    @DI(name = "MAP", desc = "在Unit中需要被重新命名的标签", relativeEvents = {"SEND"})
    public Map<String, String> map = null;
    final static ReentrantLock lock = new ReentrantLock();

    @Algorithm(name = "ADD", desc = "将添加Units发送至Queue，以便后来批量发送至其他服务")
    public void add() {
        RabbitQueue<List<Map<String, String>>> queue = QueueFactory.getInstance().getQueue(host, port, queueName);
        try {
            queue.offer(units);
            triggerEvent("EO");
        } catch (Exception ex) {
            l.error(ex.getMessage(), ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Algorithm(name = "RECEIVE_AND_SEND", desc = "")
    public void receive() {
        RabbitQueue<List> queue = QueueFactory.getInstance().getQueue(host, port, queueName);
        final IndexQueueHelper handler = new IndexQueueHelper(host + "-" + queueName, tempDir);
        lock.lock();
        try {
            int count = 0;
            int maxSize = 10000;
            while (count++ < maxSize) {
                List msg = queue.poll(List.class, 1000, new Function<List>() {

                    @Override
                    public void invoke(List arg) {
                        try {
                            handler.saveMessageInFileSystem(PojoMapper.toJson(arg));
                        } catch (Exception ex) {
                            l.error(ex.getMessage(), ex);
                        }
                    }
                });
                if (null == msg) {
                    break;
                }
            }

            List<String> list = handler.listMessages();
            if (null != list && !list.isEmpty()) {
                String content = buildIndexContent(list);
                boolean suc = sendContent(content);
                if (suc) {
                    handler.removeMessageInFileSystem(list);
                }
            }

            triggerEvent("EO");
        } catch (Exception ex) {
            l.error(ex.getMessage(), ex);
            throw new RuntimeException(ex.getMessage());
        } finally {
            lock.unlock();
        }
    }

    private IndexContentConstructor createIndexContentConstructor() {
        try {
            Class clazz = Class.forName(iccType);
            return (IndexContentConstructor) clazz.newInstance();
        } catch (Exception ex) {
            l.error(ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * 读取文件，并将文件的内容转成需要发送的数据。
     * @param list
     * @return
     */
    private String buildIndexContent(List<String> list) {
        IndexContentConstructor icc = createIndexContentConstructor();
        if (null != icc) {
            StringBuilder sb = new StringBuilder();
            for (String msg : list) {
                String json = FileUtil.read(msg);
                if (null != json) {
                    json = StringUtil.trim(json);
                    try {
                        List<Map<String, String>> listUnits = PojoMapper.fromJson(json, List.class);
                        if (null != listUnits && !listUnits.isEmpty()) {
                            for (Map<String, String> unit : listUnits) {
                                sb.append(icc.buildUnitContent(refineUnit(unit)));
                            }
                        }
                    } catch (IOException ex) {
                        l.error(ex.getMessage(), ex);
                    }
                }
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * 根据配置修改需要发送的数据。
     * @param unit
     * @return
     */
    private Map<String, String> refineUnit(Map<String, String> unit) {
        Map<String, String> ret = new HashMap<String, String>();
        if (null != unit && !unit.isEmpty()) {
            for (String key : unit.keySet()) {
                String value = unit.get(key);
                if (null == ignoreTags || !ignoreTags.contains(key)) {
                    if (null != map && map.containsKey(key)) {
                        ret.put(map.get(key), value);
                    } else {
                        ret.put(key, value);
                    }
                }
            }
        }
        return ret;
    }

    private boolean sendContent(String content) {
        boolean suc = false;
        if (StringUtil.isNotEmpty(content)) {
            try {
                HttpIndexer indexer = IndexerFactory.getInstance().createIndexer(indexHost, indexPort, "http");
                indexer.setCommand(indexCommand);
                indexer.setSuffix(indexSuffix);
                String response = indexer.post(null, content);
                if (StringUtil.isNotEmpty(indexSuccessChars)) {
                    if (response.contains(indexSuccessChars)) {
                        suc = true;
                    }
                } else {
                    suc = true;
                }
            } catch (Exception ex) {
                l.error(ex.getMessage(), ex);
                suc = false;
            }
        }
        return suc;
    }
}
