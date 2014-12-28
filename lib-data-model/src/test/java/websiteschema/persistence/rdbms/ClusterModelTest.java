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
import websiteschema.common.wrapper.SQLBeanWrapper;
import websiteschema.model.domain.cluster.Cluster;
import websiteschema.model.domain.cluster.ClusterModel;
import websiteschema.model.domain.cluster.FeatureInfo;
import websiteschema.model.domain.cluster.FeatureStatInfo;

/**
 *
 * @author ray
 */
public class ClusterModelTest {

    ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-beans.xml");
    ClusterModelDBMapper clusterModelDBMapper = ctx.getBean("clusterModelDBMapper", ClusterModelDBMapper.class);
    String name = "test_www_sohu_com_2";

    @Test
    public void test() {
        insert();
        selectAndUpdate();
        delete();
    }

    public void insert() {
        ClusterModel cm = new ClusterModel();
        cm.setRowKey(name);

        cm.setClustererType("clusterer type");
        cm.setTotalSamples(100);
        
        FeatureStatInfo fsi = new FeatureStatInfo();
        FeatureInfo[] list = new FeatureInfo[1];
        list[0] = new FeatureInfo();
        list[0].setName("test");
        fsi.setList(list);
        fsi.setMapDim(new HashMap<String, Integer>());
        cm.setStatInfo(fsi);
        Cluster[] c = new Cluster[1];
        c[0] = new Cluster();
        c[0].setType("type");
        cm.setClusters(c);

        clusterModelDBMapper.insert(SQLBeanWrapper.getMap(cm, ClusterModel.class));
    }

    public void selectAndUpdate() {
        Map map = clusterModelDBMapper.getByRowKey(name);

        System.out.println(map);

        ClusterModel schema = SQLBeanWrapper.getBean(map, ClusterModel.class, false);
        System.out.println(schema.getClusters().length);
        System.out.println(schema.getTotalSamples());
        clusterModelDBMapper.update(map);
    }

    public void delete() {
        Map url = clusterModelDBMapper.getByRowKey(name);
        clusterModelDBMapper.delete(url);
    }
}
