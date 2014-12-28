/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.common;

import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.Description;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;

/**
 *
 * @author ray
 */
@EI(name = {"EI:SLEEP"})
@EO(name = {"EO"})
public class Sleepy extends FunctionBlock {

    @DI(name = "MS", relativeEvents = "EI", desc = "睡眠时间")
    public int milsec = 0;

    @Algorithm(name = "SLEEP")
    public void cease() {
        sleep(milsec);
        triggerEvent("EO");
    }

    private void sleep(int milsec) {
        try {
            Thread.sleep(milsec);
        } catch (Exception ex) {
            l.error(ex.getMessage(), ex);
        }
    }
}
