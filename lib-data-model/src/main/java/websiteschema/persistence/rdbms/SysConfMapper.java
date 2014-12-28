/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.util.List;
import java.util.Map;
import websiteschema.conf.other.PropLoader;
import websiteschema.model.domain.SysConf;

/**
 *
 * @author ray
 */
public interface SysConfMapper extends PropLoader {

    public long getTotalResults(Map map);

    public List<SysConf> getRows(Map map);

    public void insert(SysConf conf);

    public void update(SysConf conf);

    public SysConf getById(long id);

    public void delete(SysConf conf);

    public void deleteById(long id);
}
