/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.util.List;
import java.util.Map;
import websiteschema.model.domain.StartURL;

/**
 *
 * @author ray
 */
public interface StartURLMapper {

    public long getTotalResults(Map params);

    public List<StartURL> getStartURLs(Map params);

    public List getSitesAndJobnames();

    public StartURL getById(long id);

    public StartURL getByJobname(String jobname);

    public void update(StartURL startURL);

    public void updateStatus(StartURL startURL);

    public void insert(StartURL startURL);

    public void delete(StartURL startURL);

    public void deleteById(long id);
}
