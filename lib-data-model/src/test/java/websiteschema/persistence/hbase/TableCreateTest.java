/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.hbase;

import org.junit.Test;
import websiteschema.model.domain.UrlLink;
import websiteschema.model.domain.UrlLog;
import websiteschema.model.domain.Websiteschema;
import websiteschema.model.domain.cluster.ClusterModel;
import websiteschema.model.domain.cluster.Sample;
import websiteschema.persistence.hbase.core.HBaseMapperFactory;

/**
 *
 * @author ray
 */
public class TableCreateTest {

    @Test
    public void test() {
        SampleMapper sm = new SampleMapper();
        HBaseMapperFactory.getInstance().createTableIfNotExists(sm.getTableName(), Sample.class);
        ClusterModelMapper cm = new ClusterModelMapper();
        HBaseMapperFactory.getInstance().createTableIfNotExists(cm.getTableName(), ClusterModel.class);
        WebsiteschemaMapper wm = new WebsiteschemaMapper();
        HBaseMapperFactory.getInstance().createTableIfNotExists(wm.getTableName(), Websiteschema.class);
        UrlLinkMapper um = new UrlLinkMapper();
        HBaseMapperFactory.getInstance().createTableIfNotExists(um.getTableName(), UrlLink.class);
        UrlLogMapper ulm = new UrlLogMapper();
        HBaseMapperFactory.getInstance().createTableIfNotExists(ulm.getTableName(), UrlLog.class);
    }
}
