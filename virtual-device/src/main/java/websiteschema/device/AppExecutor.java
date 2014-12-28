/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.device;

import websiteschema.fb.core.app.Application;

/**
 *
 * @author ray
 */
public class AppExecutor {

    public static void main(String args[]) throws Exception {
        String arg = args.length > 0 ? args[0] : "fb/weibo_db_extractor.app";
        Application app = new Application();
        app.getContext().loadConfigure(arg);
        app.call();
    }
}
