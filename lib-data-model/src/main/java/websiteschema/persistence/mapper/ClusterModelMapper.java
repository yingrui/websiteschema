/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.mapper;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websiteschema.common.base.Function;
import websiteschema.common.wrapper.SQLBeanWrapper;
import websiteschema.model.domain.cluster.ClusterModel;
import websiteschema.persistence.Mapper;
import websiteschema.persistence.rdbms.ClusterModelDBMapper;

/**
 *
 * @author ray
 */
@Service
public class ClusterModelMapper implements Mapper<ClusterModel> {

    @Autowired
    ClusterModelDBMapper clusterModelDBMapper;

    @Override
    public boolean exists(String rowKey) {
        int count = clusterModelDBMapper.exists(rowKey);
        return count > 0;
    }

    @Override
    public ClusterModel get(String rowKey) {
        Map map = clusterModelDBMapper.getByRowKey(rowKey);
        return SQLBeanWrapper.getBean(map, ClusterModel.class, false);
    }

    @Override
    public ClusterModel get(String rowKey, String family) {
        return get(rowKey);
    }

    @Override
    public List<ClusterModel> getList(String start, String end) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ClusterModel> getList(String start, String end, String family) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ClusterModel> getList(String start, String end, String family, int maxResults) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void put(ClusterModel obj) {
        if (null != obj) {
            if (exists(obj.getRowKey())) {
                clusterModelDBMapper.update(SQLBeanWrapper.getMap(obj, ClusterModel.class));
            } else {
                clusterModelDBMapper.insert(SQLBeanWrapper.getMap(obj, ClusterModel.class));
            }
        }
    }

    @Override
    public void put(List<ClusterModel> lst) {
        if (null != lst) {
            for (ClusterModel obj : lst) {
                put(obj);
            }
        }
    }

    @Override
    public void delete(String rowKey) {
        clusterModelDBMapper.deleteByRowKey(rowKey);
    }

    @Override
    public void scan(String start, String end, Function<ClusterModel> func) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void batchScan(String start, String end, int batchSize, Function<List<ClusterModel>> func) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
