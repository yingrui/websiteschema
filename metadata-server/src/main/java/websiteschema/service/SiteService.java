/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import websiteschema.dwr.response.ListRange;
import websiteschema.model.domain.Site;
import websiteschema.persistence.rdbms.SiteMapper;
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
public class SiteService {

    @Autowired
    private SiteMapper siteMapper;

    public ListRange getResults(Map map) {
        ListRange listRange = new ListRange();
        Map params = buildParamWithInt(map, "start", "limit");
        listRange.setData(siteMapper.getSites(params).toArray());
        listRange.setTotalSize(siteMapper.getTotalResults(params));
        System.out.println(listRange.getTotalSize());
        return listRange;
    }

    public Site getById(long id) {
        return siteMapper.getById(id);
    }

    @Transactional
    public void insert(Site site) {
        site.setCreateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        site.setCreateUser(userDetails.getUsername());
        site.setUpdateTime(site.getCreateTime());
        site.setLastUpdateUser(site.getCreateUser());
        siteMapper.insert(site);
    }

    @Transactional
    public void update(Site site) {
        site.setUpdateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        site.setLastUpdateUser(userDetails.getUsername());
        siteMapper.update(site);
    }

    @Transactional
    public void deleteRecord(Site site) {
        siteMapper.delete(site);
    }
}
