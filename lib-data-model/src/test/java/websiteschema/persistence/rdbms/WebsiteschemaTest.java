/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import websiteschema.model.domain.Websiteschema;
import websiteschema.model.domain.cralwer.CrawlerSettings;
import websiteschema.common.wrapper.SQLBeanWrapper;
import websiteschema.utils.PojoMapper;

/**
 *
 * @author ray
 */
public class WebsiteschemaTest {

    ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-beans.xml");
    WebsiteschemaDBMapper websiteschemaDBMapper = ctx.getBean("websiteschemaDBMapper", WebsiteschemaDBMapper.class);
    String name = "test_www_sohu_com_2";

    @Test
    public void test() {
        insert();
        selectAndUpdate();
        delete();
    }

    public void insert() {
        Websiteschema schema = new Websiteschema();
        schema.setRowKey(name);
        schema.setDimension(new HashMap<String, Integer>());
        schema.setCreateTime(new Date());
        schema.setProperties(new HashMap<String, String>());
        schema.setCrawlerSettings(new CrawlerSettings());
        websiteschemaDBMapper.insert(SQLBeanWrapper.getMap(schema, Websiteschema.class));
    }

    public void selectAndUpdate() {
        Map map = websiteschemaDBMapper.getByRowKey(name);

        System.out.println(map);

        Websiteschema schema = SQLBeanWrapper.getBean(map, Websiteschema.class, false);
        try {
            System.out.println(PojoMapper.toJson(schema.getCrawlerSettings()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        websiteschemaDBMapper.update(map);
    }

    public void delete() {
        Map url = websiteschemaDBMapper.getByRowKey(name);
        websiteschemaDBMapper.delete(url);
    }
}
