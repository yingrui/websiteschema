/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.device.tools;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import websiteschema.common.base.Function;
import websiteschema.model.domain.Websiteschema;
import websiteschema.model.domain.cluster.ClusterModel;
import websiteschema.persistence.Mapper;

/**
 *
 * @author ray
 */
public class ImportConifgFromHBaseToMysql {

    public static void main(String args[]) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-beans.xml");
        final Mapper<ClusterModel> clusterModelMapper = ctx.getBean("clusterModelMapper", Mapper.class);
        final Mapper<Websiteschema> websiteschemaMapper = ctx.getBean("websiteschemaMapper", Mapper.class);
        Mapper<Websiteschema> mapper1 = new websiteschema.persistence.hbase.WebsiteschemaMapper();
        Mapper<ClusterModel> mapper2 = new websiteschema.persistence.hbase.ClusterModelMapper();
        mapper1.scan("0", "z", new Function<Websiteschema>() {

            @Override
            public void invoke(Websiteschema arg) {
                websiteschemaMapper.put(arg);
            }
        });

        mapper2.scan("0", "z", new Function<ClusterModel>() {

            @Override
            public void invoke(ClusterModel arg) {
                clusterModelMapper.put(arg);
            }
        });
    }
}
