/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.fb.core;

import websiteschema.fb.core.app.Application;
import org.junit.Test;

/**
 *
 * @author ray
 */
public class HelloWorld {

    @Test
    public void test() throws InterruptedException {
        Application app = new Application();
        RuntimeContext context = app.getContext();
        context.loadConfigure("fb/HelloWorld.app");

        Thread t = new Thread(app);
        t.start();
        t.join();
    }

}
