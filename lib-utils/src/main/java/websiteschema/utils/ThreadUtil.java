/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.utils;

import org.apache.log4j.Logger;

/**
 *
 * @author ray
 */
public class ThreadUtil {

    private static Logger l = Logger.getLogger(ThreadUtil.class);

    public static void sleep(long milsec) {
        try {
            Thread.sleep(milsec);
        } catch (InterruptedException ex) {
            l.error(ex.getMessage(), ex);
        }
    }

}
