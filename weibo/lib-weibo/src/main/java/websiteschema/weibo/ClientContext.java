/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.weibo;

import websiteschema.conf.Configure;

/**
 *
 * @author ray
 */
public class ClientContext {

    private static final Configure configure = new Configure("configure-site.ini");

    public static Configure getConfigure() {
        return configure;
    }
}
