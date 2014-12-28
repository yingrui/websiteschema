/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.analyzer.browser.utils;

import java.lang.reflect.Constructor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import websiteschema.analyzer.context.BrowserContext;
import websiteschema.cluster.Clusterer;
import websiteschema.cluster.DocumentConvertor;
import websiteschema.model.domain.Websiteschema;
import websiteschema.model.domain.cluster.Cluster;
import websiteschema.model.domain.cluster.ClusterModel;
import websiteschema.model.domain.cluster.Sample;
import websiteschema.persistence.Mapper;

/**
 *
 * @author ray
 */
public class ClustererUtil {

    private static ClustererUtil ins = new ClustererUtil();

    public static ClustererUtil getInstance() {
        return ins;
    }

    public String classify(String siteId, Document doc) {
        Mapper<ClusterModel> cmMapper = BrowserContext.getSpringContext().getBean("clusterModelMapper", Mapper.class);
        ClusterModel cm = cmMapper.get(siteId);
        Mapper<Websiteschema> mapper = BrowserContext.getSpringContext().getBean("websiteschemaMapper", Mapper.class);
        Websiteschema websiteschema = mapper.get(siteId);
        if (null != cm) {
            String clustererType = cm.getClustererType();
            if (null != clustererType) {
                try {
                    Class clazz = Class.forName(clustererType);
                    Constructor ctor[] = clazz.getDeclaredConstructors();
                    Class cx[] = ctor[0].getParameterTypes();
                    // 通过反射创建Clusterer。其构造函数需要siteId;
                    Clusterer clusterer = (Clusterer) clazz.getConstructor(cx).newInstance(new Object[]{siteId});
                    clusterer.init(cm);
                    Sample sample = convert(websiteschema, doc);
                    if (null != sample) {
                        Cluster cluster = clusterer.classify(sample);
                        if (null != cluster) {
                            return cluster.getCustomName();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    private Sample convert(Websiteschema schema, Document doc) {
        Sample sample = null;

        if (null != doc && doc.getNodeType() == Node.DOCUMENT_NODE) {
            DocumentConvertor dc = new DocumentConvertor();
            dc.setXpathAttr(schema.getXpathAttr());
            sample = new Sample();
            sample.setContent(dc.convertDocument((Document) doc));
        }

        return sample;
    }
}
