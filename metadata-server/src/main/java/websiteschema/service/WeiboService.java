/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import java.util.Map;
import java.util.Date;
import websiteschema.dwr.response.ListRange;
import websiteschema.model.domain.weibo.Weibo;
import websiteschema.persistence.rdbms.WeiboMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static websiteschema.persistence.rdbms.utils.ParameterUtil.*;

/**
 *
 * @author ray
 */
@Service
public class WeiboService {

    @Autowired
    private WeiboMapper weiboMapper;

    public ListRange getResults(Map map) {
        ListRange listRange = new ListRange();
        Map params = buildParamWithInt(map, "start", "limit");
        listRange.setData(weiboMapper.getResults(params).toArray());
        listRange.setTotalSize(weiboMapper.getTotalResults(params));
        return listRange;
    }

    public Weibo getById(long id) {
        return weiboMapper.getById(id);
    }

    @Transactional
    public void insert(Weibo obj) {
        obj.setCreateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        obj.setCreateUser(userDetails.getUsername());
        obj.setUpdateTime(obj.getCreateTime());
        obj.setLastUpdateUser(obj.getCreateUser());
        weiboMapper.insert(obj);
    }

    @Transactional
    public void update(Weibo obj) {
        obj.setUpdateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        obj.setLastUpdateUser(userDetails.getUsername());
        weiboMapper.update(obj);
    }

    @Transactional
    public void deleteRecord(Weibo obj) {
        weiboMapper.delete(obj);
    }
}
