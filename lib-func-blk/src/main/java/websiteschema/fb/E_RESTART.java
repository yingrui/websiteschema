/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb;

import websiteschema.fb.annotation.Description;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;
import static websiteschema.fb.core.Event.*;

/**
 *
 * @author ray
 */
@EI(name = {"STOP:CEASE"})
@EO(name = {"COLD", "WARM", "STOP"})
@Description(desc="内置的启动和停止功能块，当接收到STOP事件，会向应用发送停止的命令，最终终止应用。")
public final class E_RESTART extends FunctionBlock {

    @Algorithm(name = "CEASE")
    public void cease() {
        getContext().addEvent(CeaseEvent());
    }
}
