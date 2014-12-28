/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.hbase;

import websiteschema.persistence.hbase.core.HBaseMapperFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import websiteschema.common.base.Function;
import websiteschema.model.domain.Websiteschema;

/**
 *
 * @author ray
 */
public class WebsiteschemaTest {

//    ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-hbase-beans.xml");
//    WebsiteschemaMapper mapper = ctx.getBean("websiteschemaMapper", WebsiteschemaMapper.class);
    WebsiteschemaMapper mapper = new WebsiteschemaMapper();
    String rowKey = "test_sohu_com_2";

    @Test
    public void test() {
        put();

        get();

        scan();

        delete();

//        deleteTable();
    }

    public void deleteTable() {
        HBaseMapperFactory.getInstance().deleteTable(mapper);
    }

    public void put() {
        Websiteschema record = new Websiteschema();
        record.setRowKey(rowKey);
        record.setValid(false);
        mapper.put(record);
    }

    public void scan() {
        final String siteId = "sda";
        mapper.scan(new Function<Websiteschema>() {

            @Override
            public void invoke(Websiteschema arg) {
                String rowKey = arg.getRowKey();
                System.out.println(siteId + " " + rowKey);
            }
        });
    }

    public void get() {
        Websiteschema record = mapper.get(rowKey);
        System.out.println("    " + record.getRowKey());
        System.out.println("    " + record.isValid());
        System.out.println("    " + record.getLastUpdateTime());
        assert (!record.isValid());
    }

    public void delete() {
        mapper.delete(rowKey);
    }
}
