/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.util.List;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import websiteschema.model.domain.weibo.*;
import static websiteschema.model.domain.weibo.Factory.*;

/**
 *
 * @author ray
 */
public class WeiboTest {

    ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-beans.xml");
    WeiboMapper weiboMapper = ctx.getBean("weiboMapper", WeiboMapper.class);
    FollowMapper followMapper = ctx.getBean("followMapper", FollowMapper.class);
    ConcernedWeiboMapper concernedWeiboMapper = ctx.getBean("concernedWeiboMapper", ConcernedWeiboMapper.class);
    long id = 0;
    Weibo w = Weibo("test", "www_weibo_com_7", "test");
    ConcernedWeibo cw = ConcernedWeibo("tt", 0, "http://weibo.com/test");

    @Test
    public void test() {
        Follow f = insert();
        selectAndUpdate(f);
        delete(f);
    }

    public Follow insert() {
        weiboMapper.insert(w);
        System.out.println("insert a weibo and wid: " + w.getId());
        concernedWeiboMapper.insert(cw);
        System.out.println("insert a concerned weibo and cwid: " + cw.getId());
        Follow f = Follow(w.getId(), cw.getId(), 0);
        followMapper.insert(f);
        System.out.println("wid: " + w.getId() + " follows cwid:" + cw.getId() + " fid: " + f.getId());
        return f;
    }

    public void selectAndUpdate(Follow f) {
        List<ConcernedWeibo> list = followMapper.getConcernedWeibo(f);
        for(ConcernedWeibo cw : list) {
            System.out.println(cw.getName());
        }
    }

    public void delete(Follow f) {
        weiboMapper.deleteById(f.getWid());
        concernedWeiboMapper.deleteById(f.getCwid());
        followMapper.delete(f);
        followMapper.deleteByWeibo(f.getWid());
    }
}
