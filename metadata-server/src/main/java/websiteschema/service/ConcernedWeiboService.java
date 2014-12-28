/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import websiteschema.model.domain.Site;
import java.util.List;
import websiteschema.persistence.rdbms.SiteMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import websiteschema.dwr.response.ListRange;
import websiteschema.model.domain.weibo.ConcernedWeibo;
import websiteschema.persistence.rdbms.ConcernedWeiboMapper;
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
public class ConcernedWeiboService {

    @Autowired
    private ConcernedWeiboMapper concernedWeiboMapper;
    @Autowired
    private SiteMapper siteMapper;

    public ListRange getResults(Map map) {
        ListRange listRange = new ListRange();
        Map params = buildParamWithInt(map, "start", "limit");
        listRange.setData(concernedWeiboMapper.getResults(params).toArray());
        listRange.setTotalSize(concernedWeiboMapper.getTotalResults(params));
        return listRange;
    }

    public ConcernedWeibo getById(long id) {
        return concernedWeiboMapper.getById(id);
    }

    @Transactional
    public void insert(ConcernedWeibo obj) {
        System.out.println(obj.getName());
        obj.setCreateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        obj.setCreateUser(userDetails.getUsername());
        obj.setUpdateTime(obj.getCreateTime());
        obj.setLastUpdateUser(obj.getCreateUser());

        check(obj);
        concernedWeiboMapper.insert(obj);
    }

    private void check(ConcernedWeibo obj) {
        String url = obj.getWeiboURL();
        String siteId = obj.getSiteId();
        if (null == siteId || "".equals(siteId)) {
            if (null != url) {
                try {
                    URI uri = new URI(url);
                    String siteDomain = uri.getHost();
                    if (null != siteDomain) {
                        Map param = with(with(buildParam(0, 10), "siteDomain", siteDomain), "siteType", "weibo");
                        List<Site> sites = siteMapper.getSites(param);
                        if (null != sites && sites.size() > 0) {
                            siteId = sites.get(0).getSiteId();
                            obj.setSiteId(siteId);
                        }
                    }
                } catch (URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Transactional
    public void update(ConcernedWeibo obj) {
        obj.setUpdateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        obj.setLastUpdateUser(userDetails.getUsername());
        check(obj);
        concernedWeiboMapper.update(obj);
    }

    @Transactional
    public void deleteRecord(ConcernedWeibo obj) {
        concernedWeiboMapper.delete(obj);
    }
}
