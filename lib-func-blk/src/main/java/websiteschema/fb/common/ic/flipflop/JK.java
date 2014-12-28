/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.common.ic.flipflop;

import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.annotation.EVT;
import websiteschema.fb.core.FunctionBlock;

/**
 *
 * @author ray
 */
@EI(name = {"E0:CP", "E1:CP", "E2:CP", "E3:CP", "E4:CP", "E5:CP", "E6:CP", "E7:CP"})
@EO(name = {"EO"})
public class JK extends FunctionBlock {

    @DO(name = "D0", relativeEvents = {"EO"})
    public boolean d0 = false;
    @DO(name = "D1", relativeEvents = {"EO"})
    public boolean d1 = false;
    @DO(name = "D2", relativeEvents = {"EO"})
    public boolean d2 = false;
    @DO(name = "D3", relativeEvents = {"EO"})
    public boolean d3 = false;
    @DO(name = "D4", relativeEvents = {"EO"})
    public boolean d4 = false;
    @DO(name = "D5", relativeEvents = {"EO"})
    public boolean d5 = false;
    @DO(name = "D6", relativeEvents = {"EO"})
    public boolean d6 = false;
    @DO(name = "D7", relativeEvents = {"EO"})
    public boolean d7 = false;

    @Algorithm(name = "CP")
    public void cp(@EVT String ei) {
        //跳变相应的输出位


        triggerEvent("EO");
    }
}
