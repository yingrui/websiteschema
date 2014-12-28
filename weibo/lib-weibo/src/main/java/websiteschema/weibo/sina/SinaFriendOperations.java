/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.weibo.sina;

import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.FriendOperations;
import org.springframework.social.twitter.api.TwitterProfile;
import weibo4j.Weibo;

/**
 *
 * @author ray
 */
public class SinaFriendOperations implements FriendOperations {

    Weibo weibo;

    public SinaFriendOperations(Weibo weibo) {
        this.weibo = weibo;
    }

    @Override
    public CursoredList<TwitterProfile> getFriends() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<TwitterProfile> getFriendsInCursor(long cursor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<TwitterProfile> getFriends(long userId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<TwitterProfile> getFriendsInCursor(long userId, long cursor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<TwitterProfile> getFriends(String screenName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<TwitterProfile> getFriendsInCursor(String screenName, long cursor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<Long> getFriendIds() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<Long> getFriendIdsInCursor(long cursor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<Long> getFriendIds(long userId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<Long> getFriendIdsInCursor(long userId, long cursor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<Long> getFriendIds(String screenName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<Long> getFriendIdsInCursor(String screenName, long cursor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<TwitterProfile> getFollowers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<TwitterProfile> getFollowersInCursor(long cursor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<TwitterProfile> getFollowers(long userId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<TwitterProfile> getFollowersInCursor(long userId, long cursor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<TwitterProfile> getFollowers(String screenName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<TwitterProfile> getFollowersInCursor(String screenName, long cursor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<Long> getFollowerIds() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<Long> getFollowerIdsInCursor(long cursor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<Long> getFollowerIds(long userId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<Long> getFollowerIdsInCursor(long userId, long cursor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<Long> getFollowerIds(String screenName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<Long> getFollowerIdsInCursor(String screenName, long cursor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String follow(long userId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String follow(String screenName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String unfollow(long userId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String unfollow(String screenName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TwitterProfile enableNotifications(long userId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TwitterProfile enableNotifications(String screenName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TwitterProfile disableNotifications(long userId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TwitterProfile disableNotifications(String screenName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean friendshipExists(String userA, String userB) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<Long> getIncomingFriendships() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<Long> getIncomingFriendships(long cursor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<Long> getOutgoingFriendships() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CursoredList<Long> getOutgoingFriendships(long cursor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
