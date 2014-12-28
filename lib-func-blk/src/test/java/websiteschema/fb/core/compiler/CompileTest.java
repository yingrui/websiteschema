/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.core.compiler;

import org.junit.Test;
import websiteschema.conf.Configure;

/**
 *
 * @author ray
 */
public class CompileTest {

    @Test
    public void test() {
        try {
            ApplicationCompiler app = new ApplicationCompiler();
            Configure conf = new Configure("fb/compiler/normal.app");
            app.setConfig(conf);
            app.compile();
        } catch (Exception ex) {
            assert (false);
        }
    }

    @Test
    public void testDataOutputMissDest() {
        try {
            ApplicationCompiler app = new ApplicationCompiler();
            Configure conf = new Configure("fb/compiler/do_miss_dest.app");
            app.setConfig(conf);
            app.compile();
            assert (false);
        } catch (DataOutputMissDestFBException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void testEventOutputMissDest() {
        try {
            ApplicationCompiler app = new ApplicationCompiler();
            Configure conf = new Configure("fb/compiler/eo_miss_dest.app");
            app.setConfig(conf);
            app.compile();
            assert (false);
        } catch (EventOutputMissDestFBException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void testMissStart() {
        try {
            ApplicationCompiler app = new ApplicationCompiler();
            Configure conf = new Configure("fb/compiler/miss_start.app");
            app.setConfig(conf);
            app.compile();
            assert (false);
        } catch (MissStartFBException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            assert (false);
        }
    }
}
