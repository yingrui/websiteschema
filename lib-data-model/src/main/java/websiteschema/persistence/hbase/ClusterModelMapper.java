/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.hbase;

import org.springframework.stereotype.Service;
import websiteschema.model.domain.cluster.ClusterModel;
import websiteschema.persistence.hbase.core.HBaseMapper;

/**
 *
 * @author ray
 */
@Service
public class ClusterModelMapper extends HBaseMapper<ClusterModel> {

    public ClusterModelMapper() {
        super(ClusterModel.class);
    }
}
