/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.util.Date;
import java.util.List;
import java.util.Map;
import websiteschema.model.domain.Scheduler;
import websiteschema.model.domain.Wrapper;

/**
 *
 * @author ray
 */
public interface SchedulerMapper {

    public long getTotalResults();

    public List<Scheduler> getSchedulers(Map params);

    public Scheduler getById(long id);

    public Scheduler getByName(String name);

    public Date getLastUpdateTime(long id);

    public void update(Scheduler scheduler);

    public void insert(Scheduler scheduler);

    public void delete(Scheduler scheduler);

    public void deleteById(long id);
}
