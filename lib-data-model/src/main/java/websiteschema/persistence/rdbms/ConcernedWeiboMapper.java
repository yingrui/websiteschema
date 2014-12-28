/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.util.List;
import java.util.Map;
import websiteschema.model.domain.weibo.ConcernedWeibo;

/**
 *
 * @author ray
 */
public interface ConcernedWeiboMapper {

    public long getTotalResults(Map params);

    public List<ConcernedWeibo> getResults(Map params);

    public ConcernedWeibo getById(long id);

    public void update(ConcernedWeibo obj);

    public void insert(ConcernedWeibo obj);

    public void delete(ConcernedWeibo obj);

    public void deleteById(long id);
}
