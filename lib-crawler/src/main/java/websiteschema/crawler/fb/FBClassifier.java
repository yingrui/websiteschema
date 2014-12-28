/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.lang.reflect.Constructor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import websiteschema.cluster.Clusterer;
import websiteschema.cluster.DocumentConvertor;
import websiteschema.fb.annotation.*;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.model.domain.Websiteschema;
import websiteschema.model.domain.cluster.Cluster;
import websiteschema.model.domain.cluster.ClusterModel;
import websiteschema.model.domain.cluster.Sample;
import static websiteschema.cluster.analyzer.IClusterTypeRecognizer.*;

/**
 *
 * @author mgd
 */
@EI(name = {"EI:CLF"})
@EO(name = {"DOC", "LINK", "INV"})
public class FBClassifier extends FunctionBlock {

    @DI(name = "SCHEMA")
    public Websiteschema websiteschema = null;
    @DI(name = "CM")
    public ClusterModel cm = null;
    @DI(name = "DOCS")
    public Document docs[] = null;
    @DO(name = "CLS", relativeEvents = {"DOC", "LINK", "INV"})
    public String cls = null;

    @Algorithm(name = "CLF")
    public void classify() {
        assert (null != cm && null != docs && null != websiteschema);
        String clustererType = cm.getClustererType();
        if (null != clustererType) {
            try {
                Class clazz = Class.forName(clustererType);
                Constructor ctor[] = clazz.getDeclaredConstructors();
                Class cx[] = ctor[0].getParameterTypes();
                // 通过反射创建Clusterer。其构造函数需要siteId;
                String siteId = websiteschema.getRowKey();
                Clusterer clusterer = (Clusterer) clazz.getConstructor(cx).newInstance(new Object[]{siteId});
                clusterer.init(cm);
                Document doc = docs[0];
                Sample sample = convert(websiteschema, doc);
                if (null != sample) {
                    Cluster cluster = clusterer.classify(sample);
                    if (null != cluster) {
                        String page_type = cluster.getType();
                        cls = cluster.getCustomName();
                        if (TYPE_DOCUMENT.equals(page_type)) {
                            triggerEvent("DOC");
                            return;
                        } else if (TYPE_LINK.equals(page_type)) {
                            triggerEvent("LINK");
                            return;
                        }
                    }
                }
                triggerEvent("INV");
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
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
