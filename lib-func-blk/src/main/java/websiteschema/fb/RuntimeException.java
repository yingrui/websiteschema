/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb;

import websiteschema.fb.annotation.Description;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.core.FunctionBlock;

/**
 *
 * @author ray
 */
@EI(name = {"EI:THROW"})
@Description(desc = "内置的异常功能块，会抛出异常，并终止功能块网络的执行，同时会抛出异常信息。")
public class RuntimeException extends FunctionBlock {

    @DI(name = "MSG")
    String msg;

    @Algorithm(name = "THROW")
    public void throwException() {
        throw new java.lang.RuntimeException(msg);
    }
}
