/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.persistence.rdbms;

import java.util.List;
import java.util.Map;
import websiteschema.model.domain.weibo.Weibo;

/**
 *
 * @author ray
 */
public interface WeiboMapper {

    public long getTotalResults(Map params);

    public List<Weibo> getResults(Map params);

    public Weibo getById(long id);

    public void update(Weibo obj);

    public void insert(Weibo obj);

    public void delete(Weibo obj);

    public void deleteById(long id);
}
