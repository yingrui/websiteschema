/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.fb;

import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;

/**
 *
 * @author ray
 */
@EI(name = {"PRINT:PRINT"})
@EO(name = {"EO"})
public class STDOUT extends FunctionBlock {

    @DI(name = "STR")
    public String str = "Hello World";

    @Algorithm(name = "PRINT")
    public void print() {
        System.out.println(str);
        triggerEvent("EO");
    }

}
