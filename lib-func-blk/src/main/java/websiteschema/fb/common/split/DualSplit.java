/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.common.split;

import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;

/**
 *
 * @author mgd
 */
@EI(name = {"EI:S"})
@EO(name = {"EO1", "EO2"})
public class DualSplit extends FunctionBlock {

    @Algorithm(name = "S")
    public void split2e() {
        triggerEvent("EO1");
        triggerEvent("EO2");
    }
}
