/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.weibo.sina;

import org.springframework.social.twitter.api.DirectMessageOperations;
import org.springframework.social.twitter.api.FriendOperations;
import org.springframework.social.twitter.api.GeoOperations;
import org.springframework.social.twitter.api.ListOperations;
import org.springframework.social.twitter.api.SearchOperations;
import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.UserOperations;
import weibo4j.Weibo;

/**
 *
 * @author ray
 */
public class Sina implements Twitter {

    String accessToken;
    Weibo weibo;

    public Sina(String accessToken) {
        this.accessToken = accessToken;
        weibo = new Weibo();
        weibo.setToken(accessToken);
    }

    @Override
    public DirectMessageOperations directMessageOperations() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FriendOperations friendOperations() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GeoOperations geoOperations() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ListOperations listOperations() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SearchOperations searchOperations() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TimelineOperations timelineOperations() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UserOperations userOperations() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isAuthorized() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
