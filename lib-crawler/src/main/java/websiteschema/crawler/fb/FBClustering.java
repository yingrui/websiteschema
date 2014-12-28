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
import websiteschema.cluster.analyzer.IClusterTypeRecognizer;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.model.domain.Websiteschema;
import websiteschema.model.domain.cluster.Cluster;
import websiteschema.model.domain.cluster.ClusterModel;
import websiteschema.model.domain.cluster.Sample;

/**
 *
 * @author ray
 */
@EO(name = {"DOC", "LINK", "INVALID"})
@EI(name = {"EI:CLS"})
public class FBClustering extends FunctionBlock {

    @DI(name = "IN")
    public Document doc = null;
    @DI(name = "CM", desc = "用来分类的模型")
    public ClusterModel cm = null;
    @DI(name = "WS", desc = "Websiteschema")
    public Websiteschema websiteschema = null;
    @DO(name = "CLS", relativeEvents = {"DOC", "LINK", "INVALID"})
    public String cluster = null;

    @Algorithm(name = "CLS")
    public void clustering() {
        assert (cm != null && websiteschema != null);
        try {
            String siteId = websiteschema.getRowKey();
            String clustererType = cm.getClustererType();
            if (null != clustererType && null != siteId) {
                // 通过反射创建Clusterer。其构造函数需要siteId;
                Class clazz = Class.forName(clustererType);
                Constructor ctor[] = clazz.getDeclaredConstructors();
                Class cx[] = ctor[0].getParameterTypes();
                Clusterer clusterer = (Clusterer) clazz.getConstructor(cx).newInstance(new Object[]{siteId});
                // 初始化Clusterer
                clusterer.init(cm);
                // 转换Document为分类对应的数据
                Sample sample = convert(websiteschema, doc);
                if (null != sample) {
                    // 分类
                    Cluster cls = clusterer.classify(sample);
                    if (null != cls) {
                        cluster = cls.getCustomName();
                        if (IClusterTypeRecognizer.TYPE_DOCUMENT.equals(cls.getType())) {
                            triggerEvent("DOC");
                        } else if (IClusterTypeRecognizer.TYPE_LINK.equals(cls.getType())) {
                            triggerEvent("LINK");
                        } else if (IClusterTypeRecognizer.TYPE_INVALID.equals(cls.getType())) {
                            triggerEvent("INVALID");
                        } else {
                            triggerEvent("INVALID");
                        }
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        triggerEvent("INVALID");
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
