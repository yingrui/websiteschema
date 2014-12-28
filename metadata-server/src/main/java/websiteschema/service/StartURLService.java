/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import websiteschema.rest.SchedulerController;
import websiteschema.model.domain.Schedule;
import java.util.List;
import websiteschema.persistence.rdbms.ScheduleMapper;
import websiteschema.utils.StringUtil;
import java.util.Map;
import java.util.Date;
import websiteschema.dwr.response.ListRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
public class StartURLService {

    @Autowired
    private StartURLMapper startURLMapper;
    @Autowired
    private ScheduleMapper scheduleMapper;

    public ListRange getResults(Map map) {
        ListRange listRange = new ListRange();
        Map params = buildParamWithInt(map, "start", "limit");
        listRange.setData(startURLMapper.getStartURLs(params).toArray());
        listRange.setTotalSize(startURLMapper.getTotalResults(params));
        return listRange;
    }

    public StartURL getById(long id) {
        return startURLMapper.getById(id);
    }

    @Transactional
    public void insert(StartURL url) {
        validateRecord(url);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        url.setCreateUser(userDetails.getUsername());
        url.setLastUpdateUser(url.getCreateUser());
        startURLMapper.insert(url);
    }

    @Transactional
    public void update(StartURL url) {
        validateRecord(url);
        url.setUpdateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        url.setLastUpdateUser(userDetails.getUsername());
        startURLMapper.update(url);
    }

    @Transactional
    public void deleteRecord(StartURL url) {
        if (null != url && url.getId() > 0) {
            List<Schedule> sches = scheduleMapper.getSchedulesByStartURL(url.getId());
            if (null != sches && !sches.isEmpty()) {
                for (Schedule sche : sches) {
                    scheduleMapper.delete(sche);
                    try {
                        SchedulerController.getScheduler().remove(sche);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            startURLMapper.delete(url);
        }
    }

    private void validateRecord(StartURL url) {
        if (null != url) {
            String jobname = url.getJobname();
            String siteId = url.getSiteId();
            if (StringUtil.isNotEmpty(jobname)) {
                url.setJobname(StringUtil.trim(jobname));
            }
            if (StringUtil.isNotEmpty(siteId)) {
                url.setSiteId(StringUtil.trim(siteId));
            }
        }
    }
}
