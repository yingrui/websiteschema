/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websiteschema.dwr.response.ListRange;
import websiteschema.model.domain.Scheduler;
import websiteschema.persistence.rdbms.SchedulerMapper;
import static websiteschema.persistence.rdbms.utils.ParameterUtil.buildParamWithInt;

/**
 *
 * @author ray
 */
@Service
public class SchedulerService {
    @Autowired
    private SchedulerMapper schedulerMapper;
    
    public ListRange getResults(Map map){
        ListRange listRange = new ListRange();
        Map params = buildParamWithInt(map, "start", "limit");       
        listRange.setData(schedulerMapper.getSchedulers(params).toArray());
        listRange.setTotalSize(schedulerMapper.getTotalResults());
        
        return listRange;
    }
    
    public Scheduler getById(Long id){
        return schedulerMapper.getById(id);
    }
    
    @Transactional
    public void update(Scheduler scheduler){
        scheduler.setUpdateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        scheduler.setLastUpdateUser(userDetails.getUsername());        
        schedulerMapper.update(scheduler);
    }
    
     @Transactional
    public void insert(Scheduler scheduler){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        scheduler.setCreateUser(userDetails.getUsername());
        scheduler.setLastUpdateUser(scheduler.getCreateUser());      
        schedulerMapper.insert(scheduler);
    }
     
     @Transactional
     public void deleteRecord(Scheduler scheduler){
         System.out.print(scheduler);
         schedulerMapper.delete(scheduler);
     }
    
}
