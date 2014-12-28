/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.weibo;

import org.springframework.social.ServiceProvider;
import org.springframework.social.twitter.api.Twitter;

/**
 *
 * @author ray
 */
public interface WeiboServiceProvider<A extends Twitter> extends ServiceProvider<A> {

    public A getApi(String accessToken);

}
