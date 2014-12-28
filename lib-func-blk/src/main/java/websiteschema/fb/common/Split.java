/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.common;

import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.Description;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;

/**
 *
 * @author ray
 */
@EI(name = {"EI:SP"})
@EO(name = {"EO1", "EO2"})
@Description(desc = "将一个事件分成两个事件")
public class Split extends FunctionBlock {

    @Algorithm(name = "SP", desc="触发事件EO1和EO2")
    public void split() {
        triggerEvent("EO1");
        triggerEvent("EO2");
    }
}
