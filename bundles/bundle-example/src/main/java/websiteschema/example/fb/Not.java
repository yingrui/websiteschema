/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.example.fb;

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
@EO(name = {"EO"})
@EI(name = {"EI:NOT"})
public class Not extends FunctionBlock {

    @DI(name = "IN")
    public int in = 0;
    @DO(name = "OUT", relativeEvents = {"EO"})
    public int out = 0;

    @Algorithm(name = "NOT")
    public void algorithm() {
        System.out.println("in: " + in);
        out = ~in;
        triggerEvent("EO");
    }
}
