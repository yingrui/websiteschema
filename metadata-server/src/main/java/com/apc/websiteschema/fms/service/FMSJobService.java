/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apc.websiteschema.fms.service;

import com.apc.websiteschema.fms.FMSJob;
import com.apc.websiteschema.fms.mapper.FMSJobMapper;
import java.util.Map;
import websiteschema.dwr.response.ListRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websiteschema.model.domain.StartURL;
import websiteschema.persistence.rdbms.StartURLMapper;
import static websiteschema.persistence.rdbms.utils.ParameterUtil.*;

/**
 *
 * @author ray
 */
@Service
public class FMSJobService {

    @Autowired
    private FMSJobMapper fmsJobMapper;
    @Autowired
    private StartURLMapper startURLMapper;

    public ListRange getResults(Map map) {
        ListRange listRange = new ListRange();
        Map params = buildParamWithInt(map, "start", "limit");
        listRange.setData(fmsJobMapper.getFMSJobs(params).toArray());
        listRange.setTotalSize(fmsJobMapper.getTotalResults(params));
        System.out.println(listRange.getTotalSize());
        return listRange;
    }

    public FMSJob getById(long id) {
        return fmsJobMapper.getById(id);
    }

    public static String getSiteDomain(String url) {
        String ret = url.replaceAll("http://", "");
        ret = ret.replaceAll("/.*", "");
        return ret;
    }

    @Transactional
    public void addStartURL(FMSJob job) {
        String jobname = job.getJobname();
        StartURL startURL = startURLMapper.getByJobname(jobname);
        if (null == startURL) {
            startURL = new StartURL();
            startURL.setCreateTime(job.getCreateTime());
            startURL.setCreateUser(job.getCreateUser());
            String siteurl = job.getSite_url();
            String siteId = getSiteDomain(siteurl).replace('.', '_') + "_" + job.getSourceId();
            startURL.setSiteId(siteId);
            startURL.setName(job.getName());
            startURL.setJobname(jobname);
            startURL.setStatus(0);
            startURL.setStartURL(job.getUrl());
            startURLMapper.insert(startURL);
        } else {
//            System.err.println("-------------->" + startURL.getJobname());
        }
    }
}
