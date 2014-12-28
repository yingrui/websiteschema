/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import websiteschema.model.domain.Task;

/**
 *
 * @author ray
 */
public interface TaskMapper {

    public long getTotalResults(Map params);

    public List<Task> getTasks(Map params);

    public List getScheduleStatus();

    public Task getById(long id);

    public void update(Task task);

    public void updateStatus(@Param("status") int status, @Param("list") List<Long> list);

    public void insert(Task task);

    public void delete(Task task);

    public void batchDelete(Date before);

    public void batchDeleteArchive(Date before);

    public void archive(Date before);
}
