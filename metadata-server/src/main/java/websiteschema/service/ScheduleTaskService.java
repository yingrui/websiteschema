/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import websiteschema.model.domain.Task;
import websiteschema.persistence.rdbms.TaskMapper;
import java.util.Map;
import websiteschema.dwr.response.ListRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static websiteschema.persistence.rdbms.utils.ParameterUtil.*;

/**
 *
 * @author ray
 */
@Service
public class ScheduleTaskService {

    @Autowired
    private TaskMapper taskMapper;

    public ListRange getResults(Map map) {
        ListRange listRange = new ListRange();
        Map params = buildParamWithInt(map, "start", "limit");
        listRange.setData(taskMapper.getTasks(params).toArray());
        listRange.setTotalSize(taskMapper.getTotalResults(params));
        return listRange;
    }

    public Task getById(long id) {
        return taskMapper.getById(id);
    }
}
