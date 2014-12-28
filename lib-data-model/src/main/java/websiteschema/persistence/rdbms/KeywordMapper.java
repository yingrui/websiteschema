/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.util.List;
import java.util.Map;
import websiteschema.model.domain.Keyword;

/**
 *
 * @author ray
 */
public interface KeywordMapper {

    public long getTotalResults(Map params);

    public List<Keyword> getResults(Map params);

    public Keyword getById(long id);

    public void update(Keyword obj);

    public void insert(Keyword obj);

    public void delete(Keyword obj);

    public void deleteById(long id);
}
