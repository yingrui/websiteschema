/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import websiteschema.model.domain.Job;

/**
 *
 * @author ray
 */
public class JobTest {

    ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-beans.xml");
    JobMapper jobMapper = ctx.getBean("jobMapper", JobMapper.class);
    long id = 0;

    @Test
    public void test() {
        insert();
        selectAndUpdate();
        delete();
    }

    public void insert() {
        Job job = new Job();
        job.setConfigure("configure");
        job.setJobType("anc");
        jobMapper.insert(job);
        id = job.getId();
    }

    public void selectAndUpdate() {
        Job job = jobMapper.getById(id);

        job.setLastUpdateUser("admin");

        jobMapper.update(job);
    }

    public void delete() {
        jobMapper.deleteById(id);
    }
}
