/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.weka;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author ray
 */
public class WekaClassifier {

    String model_str = null;

    public int classify(double[] vector) {
        Classifier c = WekaUtil.getWekaUtil().getClassifier(model_str);
        Instances dataSet = WekaUtil.getWekaUtil().getDataSet(model_str);
        FeatureWrapFactory fwf = new FeatureWrapFactory();
        fwf.setDataSet(dataSet);

        try {
            Instance v = fwf.wrap(vector);
            if (null != v) {
                double res = c.classifyInstance(v);
                return (int) res;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return -1;
    }

    public String getModel() {
        return model_str;
    }

    public void setModel(String model) {
        this.model_str = model;
    }
}
