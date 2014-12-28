/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.weka;
import org.junit.Test;

/**
 *
 * @author mgd
 */
public class WekaTest {

    @Test
    public void test1() {
        WekaClassifier wc = new WekaClassifier();
        wc.setModel("websiteschema/model/cluster_type.model");
        double[] vec = { 0.0,0.0,0,0 };
        System.out.println(wc.classify(vec));
    }
}
