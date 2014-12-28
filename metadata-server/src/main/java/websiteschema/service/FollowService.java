/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import java.util.Map;
import java.util.Date;
import websiteschema.dwr.response.ListRange;
import websiteschema.model.domain.weibo.Follow;
import websiteschema.persistence.rdbms.FollowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static websiteschema.persistence.rdbms.utils.ParameterUtil.*;

/**
 *
 * @author ray
 */
@Service
public class FollowService {

    @Autowired
    private FollowMapper followMapper;

    public ListRange getResults(Map map) {
        ListRange listRange = new ListRange();
        Map params = buildParamWithInt(map, "start", "limit");
        listRange.setData(followMapper.getResults(params).toArray());
        listRange.setTotalSize(followMapper.getTotalResults(params));
        return listRange;
    }

    public Follow getById(long id) {
        return followMapper.getById(id);
    }

    @Transactional
    public void insert(Follow obj) {
        obj.setCreateTime(new Date());
        followMapper.insert(obj);
    }

    @Transactional
    public void update(Follow obj) {
        followMapper.update(obj);
    }

    @Transactional
    public void deleteRecord(Follow obj) {
        followMapper.delete(obj);
    }
}
