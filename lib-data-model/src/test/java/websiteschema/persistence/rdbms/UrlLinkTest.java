/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.util.Date;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import websiteschema.model.domain.UrlLink;
import websiteschema.persistence.Mapper;
import websiteschema.utils.FileUtil;

/**
 *
 * @author ray
 */
public class UrlLinkTest {

    ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-beans.xml");
    Mapper<UrlLink> urlLinkMapper = ctx.getBean("urlLinkMapper", Mapper.class);
    String name = "stock_eastmoney_com_60736";

    @Test
    public void test() {
        insert();
        selectAndUpdate();
//        delete();
    }

    public void insert() {
        UrlLink s1 = new UrlLink();
        s1.setRowKey("http://moc.yenomtsae.kcots/news/1423,20120412200412818.html");
        s1.setCreateTime(new Date());
        s1.setLastUpdateTime(new Date());
        s1.setDepth(1);
        s1.setJobname(name);
        s1.setParent(null);
        s1.setHttpStatus(200);
        s1.setUrl("http://stock.eastmoney.com/news/1423,20120412200412818.html");
        s1.setContent(read("websiteschema/persistence/mapper/doc1.xml"));
        urlLinkMapper.put(s1);

        UrlLink s2 = new UrlLink();
        s2.setRowKey("http://moc.yenomtsae.kcots/news/1423,20120412200466967.html");
        s2.setCreateTime(new Date());
        s2.setLastUpdateTime(new Date());
        s2.setDepth(1);
        s2.setJobname(name);
        s2.setParent(null);
        s2.setHttpStatus(200);
        s2.setUrl("http://stock.eastmoney.com/news/1423,20120412200466967.html");
        s2.setContent(read("websiteschema/persistence/mapper/doc2.xml"));
        urlLinkMapper.put(s2);
    }

    private String read(String file) {
        return FileUtil.readResource(file);
    }

    public void selectAndUpdate() {
        System.out.println(urlLinkMapper.exists("http://moc.yenomtsae.kcots/news/1423,20120412200466967.html"));

        UrlLink obj = urlLinkMapper.get("http://moc.yenomtsae.kcots/news/1423,20120412200412818.html");
        System.out.println(obj.getContent());
    }

    public void delete() {
        urlLinkMapper.delete("http://moc.yenomtsae.kcots/news/1423,20120412200466967.html");
        urlLinkMapper.delete("http://moc.yenomtsae.kcots/news/1423,20120412200412818.html");
    }
}
