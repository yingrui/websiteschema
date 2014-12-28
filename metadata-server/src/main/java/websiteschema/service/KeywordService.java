/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import java.util.Map;
import java.util.Date;
import websiteschema.dwr.response.ListRange;
import websiteschema.model.domain.Keyword;
import websiteschema.persistence.rdbms.KeywordMapper;
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
public class KeywordService {

    @Autowired
    private KeywordMapper keywordMapper;

    public ListRange getResults(Map map) {
        ListRange listRange = new ListRange();
        Map params = buildParamWithInt(map, "start", "limit");
        listRange.setData(keywordMapper.getResults(params).toArray());
        listRange.setTotalSize(keywordMapper.getTotalResults(params));
        return listRange;
    }

    public Keyword getById(long id) {
        return keywordMapper.getById(id);
    }

    @Transactional
    public void insert(Keyword obj) {
        obj.setCreateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        obj.setCreateUser(userDetails.getUsername());
        obj.setUpdateTime(obj.getCreateTime());
        obj.setLastUpdateUser(obj.getCreateUser());
        keywordMapper.insert(obj);
    }

    @Transactional
    public void update(Keyword obj) {
        obj.setUpdateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        obj.setLastUpdateUser(userDetails.getUsername());
        keywordMapper.update(obj);
    }

    @Transactional
    public void deleteRecord(Keyword obj) {
        keywordMapper.delete(obj);
    }
}
