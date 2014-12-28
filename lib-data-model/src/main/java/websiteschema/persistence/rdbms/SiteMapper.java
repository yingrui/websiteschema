/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.util.List;
import java.util.Map;
import websiteschema.model.domain.Site;

/**
 *
 * @author ray
 */
public interface SiteMapper {

    public long getTotalResults(Map params);

    public List<Site> getSites(Map params);

    public Site getBySiteId(String siteId);

    public Site getById(long id);

    public Site getByName(String siteName);

    public void update(Site site);

    public void insert(Site site);

    public void delete(Site site);

    public void deleteBySiteId(String siteId);
}
