/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain.cluster;

import java.util.List;

/**
 *
 * @author ray
 */
public class DocUnits {

    Unit[] units = new Unit[0];

    public Unit[] getUnits() {
        return units;
    }

    public void setUnits(Unit[] units) {
        this.units = units;
    }

    public void append(List<Unit> array) {
        int pos = this.units.length;
        Unit[] tmp = new Unit[pos + array.size()];
        System.arraycopy(this.units, 0, tmp, 0, pos);
        for (int i = 0; i < array.size(); i++) {
            tmp[pos + i] = array.get(i);
        }
        this.units = tmp;
    }

    public Unit get(int index) {
        return units[index];
    }
}
