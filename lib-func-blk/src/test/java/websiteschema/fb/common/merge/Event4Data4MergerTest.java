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
public class Event4Data4MergerTest {
    
    @Test
     public void test() throws Exception{
            Event4Data4Merger evdm = new Event4Data4Merger();
            evdm.d1 = "Hello 1";
            evdm.d2 = "Hello 2";
            evdm.d3 = "Hello 3";
            evdm.d4 = "Hello 4";
//          evdm.DataMerger4("E2");
            evdm.execute("M","E1");
            System.out.println("数据输出为："+ evdm.dataout);
            assert(evdm.d1.equals(evdm.dataout));

     }
}
