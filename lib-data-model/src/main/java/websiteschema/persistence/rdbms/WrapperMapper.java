/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.util.Date;
import java.util.List;
import java.util.Map;
import websiteschema.model.domain.Wrapper;

/**
 *
 * @author ray
 */
public interface WrapperMapper {

    public long getTotalResults();

    public List<Wrapper> getWrappers(Map params);

    public Wrapper getById(long id);

    public Wrapper getByName(String name);

    public Date getLastUpdateTime(long id);

    public void update(Wrapper wrapper);

    public void insert(Wrapper wrapper);

    public void delete(Wrapper wrapper);

    public void deleteById(long id);
}
