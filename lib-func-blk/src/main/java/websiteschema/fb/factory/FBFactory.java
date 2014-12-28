/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.factory;

import websiteschema.fb.core.FunctionBlock;
import websiteschema.fb.core.RuntimeContext;

/**
 *
 * @author ray
 */
public interface FBFactory {

    public FunctionBlock create(String fbName, RuntimeContext context);
}
