/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.common.bool;

import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;

/**
 *
 * @author ray
 */
@EI(name = {"EI:AND"})
@EO(name = {"T", "F"})
public class And extends FunctionBlock {

    @DI(name = "D0")
    public boolean d0 = false;
    @DI(name = "D1")
    public boolean d1 = false;
//    @DI(name = "D2")
//    public boolean d2 = false;
//    @DI(name = "D3")
//    public boolean d3 = false;
//    @DI(name = "D4")
//    public boolean d4 = false;
//    @DI(name = "D5")
//    public boolean d5 = false;
//    @DI(name = "D6")
//    public boolean d6 = false;
//    @DI(name = "D7")
//    public boolean d7 = false;

    private boolean out = false;

    @Algorithm(name = "AND")
    public void and() {
        out = d0 && d1;
        if (out) {
            triggerEvent("T");
        } else {
            triggerEvent("F");
        }
    }
}
