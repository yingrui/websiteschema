/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.util.Date;
import java.util.List;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import websiteschema.common.base.Function;
import websiteschema.model.domain.UrlLog;
import websiteschema.persistence.mapper.UrlLogMapper;
import websiteschema.utils.DateUtil;

/**
 *
 * @author ray
 */
public class UrlLogTest {

    ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-beans.xml");
    UrlLogMapper urlLogMapper = ctx.getBean("urlLogMapper", UrlLogMapper.class);
    String name = "stock_eastmoney_com_60736";

    @Test
    public void test() {
//        insert();
        selectAndUpdate();
        delete();
    }

    public void insert() {
        UrlLog s1 = new UrlLog();
        s1.setRowKey("insurance_eastmoney_com_63447+2012-04-12 11:50+http://moc.yenomtsae.ecnarusni/news/1224,20120412200403874.html");
        s1.setCreateTime(1334202638419L);
        urlLogMapper.put(s1);

        UrlLog s2 = new UrlLog();
        s2.setRowKey("insurance_eastmoney_com_63447+2012-04-06 10:53+http://moc.yenomtsae.ecnarusni/news/1235,20120406199573438.html");
        s2.setCreateTime(1333680813312L);
        urlLogMapper.put(s2);
    }

    public void selectAndUpdate() {
        List<UrlLog> lst = urlLogMapper.getList(name, name + "+" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm"));
        System.out.println(lst);
        for (UrlLog s : lst) {
            System.out.println(s.getURLRowKey());
        }

        urlLogMapper.scan(name, name + "+" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm"), new Function<UrlLog>() {

            @Override
            public void invoke(UrlLog arg) {
                System.out.println(arg.getJobname());
            }
        });
    }

    public void delete() {
        urlLogMapper.scan(name, name + "+" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm"), new Function<UrlLog>() {

            @Override
            public void invoke(UrlLog arg) {
                urlLogMapper.delete(arg.getRowKey());
            }
        });
    }
}
