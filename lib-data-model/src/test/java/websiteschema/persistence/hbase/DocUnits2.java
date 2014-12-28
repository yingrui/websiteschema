/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.persistence.hbase;

import java.util.ArrayList;
import java.util.List;
import websiteschema.model.domain.cluster.Unit;

/**
 *
 * @author ray
 */
public class DocUnits2 {

    List<Unit> units = new ArrayList<Unit>();

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

}
