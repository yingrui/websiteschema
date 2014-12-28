/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apc.websiteschema.fms.service;

import com.apc.websiteschema.fms.FMSSite;
import com.apc.websiteschema.fms.mapper.FMSSiteMapper;
import java.util.Map;
import websiteschema.dwr.response.ListRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websiteschema.model.domain.Site;
import websiteschema.persistence.rdbms.SiteMapper;
import static websiteschema.persistence.rdbms.utils.ParameterUtil.*;

/**
 *
 * @author ray
 */
@Service
public class FMSSiteService {

    @Autowired
    private FMSSiteMapper fmsSiteMapper;
    @Autowired
    private SiteMapper siteMapper;

    public ListRange getResults(Map map) {
        ListRange listRange = new ListRange();
        Map params = buildParamWithInt(map, "start", "limit");
        listRange.setData(fmsSiteMapper.getFMSSites(params).toArray());
        listRange.setTotalSize(fmsSiteMapper.getTotalResults(params));
        System.out.println(listRange.getTotalSize());
        return listRange;
    }

    public FMSSite getById(long id) {
        return fmsSiteMapper.getById(id);
    }

    @Transactional
    public void addSite(FMSSite fmsSite) {
        Site site = new Site();
        String url = fmsSite.getUrl();
        String domain = FMSJobService.getSiteDomain(url);
        site.setSiteId(domain.replace('.', '_') + '_' + fmsSite.getId());
        site.setUrl(url);
        site.setSiteName(fmsSite.getName());
        site.setSiteDomain(domain);
        site.setSiteType("news");// 建议值
        siteMapper.insert(site);
    }
}
