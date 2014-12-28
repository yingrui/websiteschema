/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.util.List;
import java.util.Map;
import websiteschema.model.domain.weibo.ConcernedWeibo;
import websiteschema.model.domain.weibo.Follow;
import websiteschema.model.domain.weibo.Weibo;

/**
 *
 * @author ray
 */
public interface FollowMapper {

    public List<ConcernedWeibo> getConcernedWeibo(Follow follow);

    public List<Weibo> getWeibo(Follow follow);

    public long getTotalResults(Map params);

    public List<Follow> getResults(Map params);

    public Follow getById(long id);

    public void insert(Follow follow);

    public void update(Follow follow);

    public void delete(Follow follow);

    public void deleteByWeibo(long wid);

    public void deleteByConcernedWeibo(long cwid);
}
