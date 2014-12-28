/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster;

import java.util.*;
import org.apache.log4j.Logger;
import websiteschema.model.domain.cluster.Dimension;
import websiteschema.model.domain.cluster.DocUnits;
import websiteschema.model.domain.cluster.DocVector;
import websiteschema.model.domain.cluster.FeatureInfo;
import websiteschema.model.domain.cluster.FeatureStatInfo;
import websiteschema.model.domain.cluster.Sample;
import websiteschema.model.domain.cluster.Unit;

/**
 *
 * @author ray
 */
public class DocVectorConvertor {

    Logger l = Logger.getLogger(DocVectorConvertor.class);

    private Dimension getDim(String dimName, FeatureStatInfo statInfo) {
        FeatureInfo f = statInfo.getFeatureInfo(dimName);
        if (null != f) {
            int dim = statInfo.getMapDim().get(dimName);
            Dimension d = new Dimension();
            d.setId(dim);
            d.setValue(f.getWeight());
            return d;
        }
        return null;
    }

    public DocVector convert(Sample sample, FeatureStatInfo statInfo) {
        DocVector vect = new DocVector();
        vect.setName(sample.getRowKey());// 设置向量的row key

        DocUnits units = sample.getContent();
        if (null != units) {
            Unit array[] = units.getUnits();
            if (null != array) {
                List<Dimension> dims = new ArrayList<Dimension>();
                Set<String> set = new HashSet<String>();
                for (Unit u : array) {
                    if (!set.contains(u.getXpath())) {
                        Dimension d = getDim(u.getXpath(), statInfo);
                        if (null != d && d.getValue() > 0) {
                            dims.add(d);
                        }
                        set.add(u.getXpath());
                    }
                }
                set = null;
                vect.append(dims);
            }
        } else {
            l.error("sample is invalid: " + sample.getUrl());
            return null;
        }

        return vect;
    }
}
