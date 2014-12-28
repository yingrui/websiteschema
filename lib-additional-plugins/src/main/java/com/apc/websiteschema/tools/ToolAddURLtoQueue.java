/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apc.websiteschema.tools;

import com.rabbitmq.client.Channel;
import java.util.List;
import websiteschema.common.amqp.Message;
import websiteschema.common.amqp.RabbitQueue;
import websiteschema.common.base.Function;
import websiteschema.model.domain.UrlLink;
import websiteschema.model.domain.UrlLog;
import websiteschema.persistence.Mapper;
import websiteschema.persistence.hbase.UrlLinkMapper;
import websiteschema.persistence.hbase.UrlLogMapper;

/**
 * Send a message to queue.
 * {
 *  "taskId":3603908,
 *  "url":"http://guba.eastmoney.com/look,000565,7013462264.html",
 *  "siteId":"guba_eastmoney_com_100",
 *  "jobname":"guba_eastmoney_com_100",
 *  "startURLId":3663,
 *  "jobId":63,
 *  "scheId":0,
 *  "wrapperId":8,
 *  "configure":"CLS=custom",
 *  "createTime":1332892884079,
 *  "depth":1
 * }
 * @author ray
 */
public class ToolAddURLtoQueue {

    RabbitQueue<Message> queue;
    String server;
    String queueName;
    String siteId;
    int wrapperId = 8;
    int startURLId = 3663;
    int jobId = 63;
    int scheId = 0;

    public void scanAndSend(final String jobname, String startDate, String endDate) {
        queue = new RabbitQueue(server, queueName);
        final Mapper<UrlLog> mapper = new UrlLogMapper();
        final Mapper<UrlLink> linkMapper = new UrlLinkMapper();
        mapper.batchScan(jobname + "+" + startDate, jobname + "+" + endDate, 1000, new Function<List<UrlLog>>() {

            @Override
            public void invoke(List<UrlLog> args) {
                Channel chnl = null;
                try {
                    try {
                        chnl = queue.getChannel();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        queue.processException(ex);
                    }
                    if (null != chnl) {
                        for (UrlLog arg : args) {
                            String rowKey = arg.getURLRowKey();
                            UrlLink urlLink = linkMapper.get(rowKey);
                            String url = urlLink.getUrl();
                            Message msg =
                                    new Message(jobId, startURLId, scheId, wrapperId, siteId, jobname, url, "CLS=custom");
                            queue.offer(chnl, msg);
                        }
                    }
                } finally {
                    try {
                        if (null != chnl) {
                            chnl.close();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        queue.close();
    }

    public static void main(String args[]) {
        String start = "2012-03-27 18";
        String end = "2012-03-28 09";
        String jobname = "guba_.sina_com_cn_105";

        ToolAddURLtoQueue tool = new ToolAddURLtoQueue();
        tool.jobId = 153;
        tool.scheId = 158;
        tool.wrapperId = 8;
        tool.startURLId = 5156;
        tool.server = "192.168.4.122";
        tool.queueName = "url_queue_1";
        tool.siteId = "guba_.sina_com_cn_105";
        tool.scanAndSend(jobname, start, end);
    }
}
