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
import websiteschema.model.domain.Websiteschema;
import websiteschema.persistence.Mapper;
import websiteschema.persistence.rdbms.WebsiteschemaDBMapper;

/**
 *
 * @author ray
 */
@Service
public class WebsiteschemaMapper implements Mapper<Websiteschema> {

    @Autowired
    WebsiteschemaDBMapper websiteschemaDBMapper;

    @Override
    public boolean exists(String rowKey) {
        int count = websiteschemaDBMapper.exists(rowKey);
        return count > 0;
    }

    @Override
    public Websiteschema get(String rowKey) {
        Map map = websiteschemaDBMapper.getByRowKey(rowKey);
        return SQLBeanWrapper.getBean(map, Websiteschema.class, false);
    }

    @Override
    public Websiteschema get(String rowKey, String family) {
        return get(rowKey);
    }

    @Override
    public List<Websiteschema> getList(String start, String end) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Websiteschema> getList(String start, String end, String family) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Websiteschema> getList(String start, String end, String family, int maxResults) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void put(Websiteschema obj) {
        if (null != obj) {
            if (exists(obj.getRowKey())) {
                websiteschemaDBMapper.update(SQLBeanWrapper.getMap(obj, Websiteschema.class));
            } else {
                websiteschemaDBMapper.insert(SQLBeanWrapper.getMap(obj, Websiteschema.class));
            }
        }
    }

    @Override
    public void put(List<Websiteschema> lst) {
        if (null != lst) {
            for (Websiteschema obj : lst) {
                put(obj);
            }
        }
    }

    @Override
    public void delete(String rowKey) {
        websiteschemaDBMapper.deleteByRowKey(rowKey);
    }

    @Override
    public void scan(String start, String end, Function<Websiteschema> func) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void batchScan(String start, String end, int batchSize, Function<List<Websiteschema>> func) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
