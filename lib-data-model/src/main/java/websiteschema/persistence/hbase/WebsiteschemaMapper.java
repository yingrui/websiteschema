/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.persistence.hbase;

import org.springframework.stereotype.Service;
import websiteschema.model.domain.Websiteschema;
import websiteschema.persistence.hbase.core.HBaseMapper;

/**
 *
 * @author ray
 */
@Service
public class WebsiteschemaMapper extends HBaseMapper<Websiteschema> {

    public WebsiteschemaMapper() {
        super(Websiteschema.class);
    }

}
