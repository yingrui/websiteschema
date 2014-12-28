/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.persistence.hbase;

import org.springframework.stereotype.Service;
import websiteschema.model.domain.UrlLog;
import websiteschema.persistence.hbase.core.HBaseMapper;

/**
 *
 * @author ray
 */
@Service
public class UrlLogMapper extends HBaseMapper<UrlLog>{

    public UrlLogMapper() {
        super(UrlLog.class);
    }
}
