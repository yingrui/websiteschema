/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.weibo.sina.connect;

import org.springframework.social.ServiceProvider;
import websiteschema.weibo.sina.Sina;

/**
 *
 * @author ray
 */
public class SinaServiceProvider implements ServiceProvider<Sina> {

    public SinaServiceProvider(String clientId, String clientSecret) {
    }

    public Sina getApi(String accessToken) {
        return null;
    }
}
