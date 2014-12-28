/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.weibo.sina.connect;

import org.springframework.social.ApiException;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;
import org.springframework.social.twitter.api.TwitterProfile;
import websiteschema.weibo.sina.Sina;

/**
 *
 * @author ray
 */
public class SinaAdapter implements ApiAdapter<Sina> {

    @Override
    public boolean test(Sina weibo) {
        try {
            weibo.userOperations().getUserProfile();
            return true;
        } catch (ApiException e) {
            return false;
        }
    }

    @Override
    public void setConnectionValues(Sina weibo, ConnectionValues values) {
        TwitterProfile profile = weibo.userOperations().getUserProfile();
        values.setProviderUserId(Long.toString(profile.getId()));
        values.setDisplayName("@" + profile.getScreenName());
        values.setProfileUrl(profile.getProfileUrl());
        values.setImageUrl(profile.getProfileImageUrl());
    }

    @Override
    public UserProfile fetchUserProfile(Sina weibo) {
        TwitterProfile profile = weibo.userOperations().getUserProfile();
        return new UserProfileBuilder().setName(profile.getName()).setUsername(profile.getScreenName()).build();
    }

    @Override
    public void updateStatus(Sina weibo, String message) {
        weibo.timelineOperations().updateStatus(message);
    }
}
