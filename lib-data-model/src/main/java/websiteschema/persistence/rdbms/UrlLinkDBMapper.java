/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.util.List;
import java.util.Map;

/**
 *
 * @author ray
 */
public interface UrlLinkDBMapper {

    public long getTotalResults(Map params);

    public List<Map> getResults(Map params);

    public Map getById(long id);

    public Map getByRowKey(String rowKey);

    public Map getSimpleResult(String rowKeys);

    public long getId(String rowKey);

    public int exists(String rowKey);

    public void update(Map map);

    public void insert(Map map);

    public void delete(Map map);

    public void deleteById(long id);

    public void deleteByRowKey(String rowKey);
}
