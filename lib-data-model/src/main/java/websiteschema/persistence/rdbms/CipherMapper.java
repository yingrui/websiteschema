/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.util.List;
import java.util.Map;
import websiteschema.model.domain.Cipher;
import websiteschema.model.domain.Site;

/**
 *
 * @author ray
 */
public interface CipherMapper {

    public long getTotalResults(Map params);

    public List<Cipher> getCiphers(Map params);

    public Cipher getBySiteId(String siteId);

    public Cipher getById(long id);

    public void update(Cipher cipher);

    public void insert(Cipher cipher);

    public void delete(Cipher cipher);

    public void deleteBySiteId(String siteId);
}
