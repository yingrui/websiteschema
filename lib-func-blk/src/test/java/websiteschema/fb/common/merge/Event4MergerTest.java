/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.common.merge;
import org.junit.Test;


/**
 *
 * @author st
 */
public class Event4MergerTest{
    @Test
    public void test() throws Exception{
        QuadMerge evm = new QuadMerge();
        evm.executeEvent("EO");
    }
}
