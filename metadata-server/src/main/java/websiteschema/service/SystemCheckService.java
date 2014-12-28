/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import com.rabbitmq.client.Channel;
import java.net.URL;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websiteschema.common.amqp.Message;
import websiteschema.common.amqp.RabbitQueue;
import websiteschema.metadata.utils.MetadataServerContext;
import websiteschema.model.domain.SysConf;
import websiteschema.model.domain.cluster.Sample;
import websiteschema.persistence.hbase.core.HBaseMapperFactory;
import websiteschema.persistence.rdbms.SysConfMapper;

/**
 *
 * @author ray
 */
@Service
public class SystemCheckService {

    @Autowired
    private SysConfMapper sysConfMapper;

    public String checkStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("<p>");
        sb.append("<h2>检查网络情况：</h2>");
        sb.append(checkNetwork());
        sb.append("<h2>检查数据库：</h2>");
        sb.append(checkDatabase());
        sb.append("<h2>检查HBase数据库：</h2>");
        sb.append(checkNosqlDatabase());
        sb.append("<h2>检查消息队列服务器：</h2>");
        sb.append(checkRabbitMQ());
        sb.append("<h2>检查下属采集服务器：</h2>");
        sb.append(checkVirtualDevices());
        sb.append("</p>");
        return sb.toString();
    }

    private String checkVirtualDevices() {
        StringBuilder sb = new StringBuilder();
        List<String> devices = MetadataServerContext.getInstance().
                getConf().getListProperty("VirtualDevices");
        sb.append("<span>");
        for (String device : devices) {
            try {
                URL url = new URL("http://" + device + "/action=getstatus");
                url.getContent();
                sb.append("<h3>").append(device).append("：状态正常</h3>");
            } catch (Exception ex) {
                sb.append("<h3><font color='red'>").append(device).append("：状态异常</font></h3>");
            }
        }
        sb.append("</span>");
        return sb.toString();
    }

    private String checkNetwork() {
        StringBuilder sb = new StringBuilder();
        String u = MetadataServerContext.getInstance().
                getConf().getProperty("NetworkTestURL");
        try {
            URL url = new URL(u);
            url.getContent();
            sb.append("<h3>打开").append(u).append("正常，网络连接正常</h3>");
        } catch (Exception ex) {
            sb.append("<h3><font color='red'>网络状态异常，无法打开：").append(u).append("</font></h3>");
        }
        return sb.toString();
    }

    private String checkDatabase() {
        StringBuilder sb = new StringBuilder();
        try {
            SysConf row = sysConfMapper.getById(1);
            sb.append("<h3>数据库状态正常</h3>");
        } catch (Exception ex) {
            sb.append("<h3><font color='red'>数据库状态异常</h3>");
        }
        return sb.toString();
    }

    private String checkNosqlDatabase() {
        StringBuilder sb = new StringBuilder();
        boolean b = false;
        try {
            b = HBaseMapperFactory.getInstance().checkTableWhetherExists("sample", Sample.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        } catch (NoClassDefFoundError ex) {
        }
        if (b) {
            sb.append("<h3>Hbase数据库状态正常</h3>");
        } else {
            sb.append("<h3><font color='red'>Hbase数据库状态异常</font></h3>");
        }
        return sb.toString();
    }

    private String checkRabbitMQ() {
        StringBuilder sb = new StringBuilder();
        String host = MetadataServerContext.getInstance().
                getConf().getProperty("URLQueue", "ServerHost", "localhost");
        try {
            RabbitQueue<Message> queue = new RabbitQueue<Message>(host, "testing");
            Channel chnl = queue.getChannel();
            chnl.close();
            sb.append("<h3>RabbitMQ: ").append(host).append(" 状态正常</h3>");
        } catch (Exception ex) {
            ex.printStackTrace();
            sb.append("<h3><font color='red'>RabbitMQ: ").append(host).append(" 状态异常</font></h3>");
        }
        return sb.toString();
    }
}
