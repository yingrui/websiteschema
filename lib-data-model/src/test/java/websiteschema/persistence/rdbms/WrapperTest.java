/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.util.Date;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import websiteschema.model.domain.Wrapper;

/**
 *
 * @author ray
 */
public class WrapperTest {

    ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-beans.xml");
    WrapperMapper wrapperMapper = ctx.getBean("wrapperMapper", WrapperMapper.class);
    String name = "test_www_sohu_com_2";

    @Test
    public void test() {
        insert();
        selectAndUpdate();
        delete();
    }

    public void insert() {
        Wrapper wrapper = new Wrapper();
        wrapper.setApplication("abcd");
        wrapper.setWrapperType("abc");
        wrapper.setName(name);
        wrapperMapper.insert(wrapper);
    }

    public void selectAndUpdate() {
        Wrapper wrapper = wrapperMapper.getByName(name);

        wrapper.setWrapperType("bcd");

        wrapperMapper.update(wrapper);

        Date updateTime = wrapperMapper.getLastUpdateTime(wrapper.getId());
        System.out.println(updateTime);
    }

    public void delete() {
        Wrapper url = wrapperMapper.getByName(name);
        wrapperMapper.delete(url);
    }
}
