/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.persistence.rdbms;

import java.util.List;
import java.util.Map;
import websiteschema.model.domain.Schedule;

/**
 *
 * @author ray
 */
public interface ScheduleMapper {

    public long getTotalResults(Map params);

    public List<Schedule> getSchedules(Map params);

    public List<Schedule> getAll(); 

    public List<Schedule> getSchedulesByStartURL(long startURL);
    
    public List<Schedule> getSchedulesByLocationId(long locationId);
    
    public Schedule getById(long id);

    public void update(Schedule schedule);

    public void insert(Schedule schedule);

    public void delete(Schedule schedule);

    public void deleteById(long id);

}
