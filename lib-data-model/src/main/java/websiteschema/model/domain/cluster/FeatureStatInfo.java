/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain.cluster;

import java.util.*;

/**
 *
 * @author ray
 */
public class FeatureStatInfo {

    private Map<String, Integer> mapDim = null;
    private FeatureInfo[] list = null;

    public FeatureInfo[] getList() {
        return list;
    }

    public void setList(FeatureInfo[] list) {
        this.list = list;
    }

    public Map<String, Integer> getMapDim() {
        return mapDim;
    }

    public void setMapDim(Map<String, Integer> mapDim) {
        this.mapDim = mapDim;
    }

    public FeatureInfo getFeatureInfo(String name) {
        return mapDim.containsKey(name) ? list[mapDim.get(name)] : null;
    }
}
