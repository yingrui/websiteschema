/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb.index;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import websiteschema.common.amqp.QueueFactory;
import websiteschema.common.amqp.RabbitQueue;
import websiteschema.common.base.Function;
import websiteschema.fb.annotation.Description;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.model.domain.UrlLink;
import websiteschema.persistence.Mapper;
import websiteschema.utils.StringUtil;

/**
 *
 * @author ray
 */
@EI(name = {"ADD:ADD", "SEND:RECEIVE_AND_SEND"})
@EO(name = {"EO"})
@Description(desc = "将URL添加至URL Queue")
public class FBIndexQueue extends FunctionBlock {

    @DI(name = "HOST", desc = "RabbitMQ server host", relativeEvents = {"ADD", "SEND"})
    public String host;
    @DI(name = "PORT", desc = "RabbitMQ server port", relativeEvents = {"ADD", "SEND"})
    public int port;
    @DI(name = "QUEUE", desc = "RabbitMQ server queue name", relativeEvents = {"ADD", "SEND"})
    public String queueName = "index_queue";
    @DI(name = "KEY", desc = "需要发送至HBASE的rowKey", relativeEvents = {"ADD"})
    public String urlKey;
    //
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
    final static ReentrantLock lock = new ReentrantLock();

    @Algorithm(name = "ADD", desc = "将添加链接发送至Queue，以便后来批量发送至其他服务")
    public void addOne() {
        RabbitQueue<String> queue = QueueFactory.getInstance().getQueue(host, port, queueName);
        try {
            queue.offer(urlKey);
            triggerEvent("EO");
        } catch (Exception ex) {
            l.error(ex.getMessage(), ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Algorithm(name = "RECEIVE_AND_SEND", desc = "")
    public void receive() {
        RabbitQueue<String> queue = QueueFactory.getInstance().getQueue(host, port, queueName);
        final IndexQueueHelper handler = new IndexQueueHelper(host + "-" + queueName, tempDir);
        Mapper<UrlLink> urlLinkMapper = getContext().getSpringBean("urlLinkMapper", Mapper.class);
        handler.setUrlLinkMapper(urlLinkMapper);
        lock.lock();
        try {
            int count = 0;
            int maxSize = 1000;
            while (count++ < maxSize) {
                String rowKey = queue.poll(String.class, 1000, new Function<String>() {

                    @Override
                    public void invoke(String arg) {
                        handler.saveMessageInFileSystem(arg);
                    }
                });
                if (null == rowKey) {
                    break;
                }
            }

            List<String> list = handler.listMessages();
            if (null != list && !list.isEmpty()) {
                String content = handler.compositeIndexFile(list);

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
