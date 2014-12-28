/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.hbase;

import java.util.List;
import websiteschema.persistence.hbase.core.HBaseMapperFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import websiteschema.common.base.Function;
import websiteschema.model.domain.cluster.DocUnits;
import websiteschema.model.domain.cluster.Sample;
import websiteschema.model.domain.cluster.Unit;

/**
 *
 * @author ray
 */
public class SampleTest {

    ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-hbase-beans.xml");
    SampleMapper mapper = ctx.getBean("sampleMapper", SampleMapper.class);
    String rowKey = "test_sohu_com_2+http://www.sohu.com/";

    @Test
    public void test() {
        HBaseMapperFactory.getInstance().createTableIfNotExists(mapper.getTableName(), Sample.class);
        put();

        get();

        scan();

        getList();

        delete();

//        deleteTable();
    }

    public void deleteTable() {
        HBaseMapperFactory.getInstance().deleteTable(mapper);
    }

    public void put() {
        Sample record = new Sample();
        record.setRowKey(rowKey);
        record.setUrl("http://www.sohu.com/");
        {
            DocUnits doc = new DocUnits();
            Unit[] units = new Unit[1];
            Unit u = new Unit();
            u.setText("test");
            u.setXpath("/html/body/text()");
            units[0] = u;
            doc.setUnits(units);
            record.setContent(doc);
        }
        mapper.put(record);
    }

    public void scan() {
        final String siteId = "test_sohu_com_2+";
        mapper.scan("test_sohu_com_2+", new Function<Sample>() {

            @Override
            public void invoke(Sample arg) {
                String rowKey = arg.getRowKey();
                System.out.println(siteId + " " + rowKey);
            }
        });
    }

    public void getList() {
        final String siteId = "test_sohu_com_2";
        List<Sample> samples = mapper.getList(siteId, "test_sohu_com_3", "cf");
        for (Sample sample : samples) {
            assert (null == sample.getContent());
        }
    }

    public void get() {
        Sample record = mapper.get(rowKey);
        System.out.println("    " + record.getRowKey());
        System.out.println("    " + record.getUrl());
        System.out.println("    " + record.getContent().get(0).getText());
        System.out.println("    " + record.getLastUpdateTime());
    }

    public void delete() {
        mapper.delete(rowKey);
    }
}
