/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.weibo;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.twitter.api.Twitter;

/**
 *
 * @author ray
 */
public class WeiboConnection<A extends Twitter> implements Connection<A>, ConnectionValues {

    private String displayName;
    private String profileUrl;
    private String imageUrl;
    private String providerId;
    private String secret;
    private String providerUserId;
    private String password;
    private String accessToken;
    private String refreshToken;
    private Long expireTime;
    private WeiboServiceProvider<A> serviceProvider;

    @Override
    public ConnectionKey getKey() {
        return new ConnectionKey(providerId, providerUserId);
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getProfileUrl() {
        return profileUrl;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public void sync() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean test() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasExpired() {
        return expireTime != null && System.currentTimeMillis() >= expireTime;
    }

    @Override
    public void refresh() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UserProfile fetchUserProfile() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateStatus(String message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public A getApi() {
        return serviceProvider.getApi(accessToken);
    }

    @Override
    public ConnectionData createData() {
        return new ConnectionData(getKey().getProviderId(), getKey().getProviderUserId(), getDisplayName(), getProfileUrl(), getImageUrl(), accessToken, secret, refreshToken, expireTime);
    }

    @Override
    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    @Override
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
