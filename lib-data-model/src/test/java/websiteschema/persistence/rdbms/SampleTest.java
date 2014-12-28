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
import websiteschema.common.wrapper.SQLBeanWrapper;
import websiteschema.model.domain.cluster.DocUnits;
import websiteschema.model.domain.cluster.Sample;
import websiteschema.model.domain.cluster.Unit;
import websiteschema.persistence.mapper.SampleMapper;
import websiteschema.utils.DateUtil;

/**
 *
 * @author ray
 */
public class SampleTest {

    ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-beans.xml");
    SampleDBMapper sampleDBMapper = ctx.getBean("sampleDBMapper", SampleDBMapper.class);
    SampleMapper sampleMapper = ctx.getBean("sampleMapper", SampleMapper.class);
    String name = "test_www_sohu_com_2";

    @Test
    public void test() {
//        insert();
        selectAndUpdate();
//        delete();
    }

    public void insert() {
        Sample s1 = new Sample();
        s1.setRowKey(name + "1");
        s1.setSiteId(name);
        s1.setHttpStatus(200);
        s1.setLastUpdateTime(new Date());
        s1.setContent(create());
        sampleDBMapper.insert(SQLBeanWrapper.getMap(s1, Sample.class));

        Sample s2 = new Sample();
        s2.setRowKey(name + "2");
        s2.setSiteId(name);
        s2.setHttpStatus(200);
        s2.setLastUpdateTime(new Date());
        s2.setContent(create());
        sampleDBMapper.insert(SQLBeanWrapper.getMap(s2, Sample.class));
    }

    private DocUnits create() {
        DocUnits ret = new DocUnits();
        Unit u[] = new Unit[1];
        u[0] = new Unit();
        u[0].setText("abc");
        u[0].setXpath("xpath");
        ret.setUnits(u);
        return ret;
    }

    public void selectAndUpdate() {
        List<Sample> lst = sampleMapper.getList(name, name + "+" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm"));
        System.out.println(lst);
        for (Sample s : lst) {
            System.out.println(s.getContent().getUnits().length);
        }
    }

    public void delete() {
        Sample s1 = sampleMapper.get(name + "1");
        sampleMapper.delete(s1.getRowKey());
        Sample s2 = sampleMapper.get(name + "2");
        sampleMapper.delete(s2.getRowKey());
    }
}
