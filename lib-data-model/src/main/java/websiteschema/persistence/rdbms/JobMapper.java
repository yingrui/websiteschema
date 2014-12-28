/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.persistence.rdbms;

import java.util.List;
import java.util.Map;
import websiteschema.model.domain.Job;

/**
 *
 * @author ray
 */
public interface JobMapper {

    public long getTotalResults();

    public List<Job> getJobs(Map params);

    public Job getById(long id);

    public void update(Job job);

    public void insert(Job job);

    public void delete(Job job);

    public void deleteById(long id);
}
