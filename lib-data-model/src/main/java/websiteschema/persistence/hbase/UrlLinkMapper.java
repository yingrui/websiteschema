/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.persistence.hbase;

import org.springframework.stereotype.Service;
import websiteschema.model.domain.UrlLink;
import websiteschema.persistence.hbase.core.HBaseMapper;

/**
 *
 * @author ray
 */
@Service
public class UrlLinkMapper extends HBaseMapper<UrlLink>{

    public UrlLinkMapper() {
        super(UrlLink.class);
    }
}
