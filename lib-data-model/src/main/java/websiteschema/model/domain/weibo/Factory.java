/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain.weibo;

/**
 *
 * @author ray
 */
public class Factory {

    public static Weibo Weibo(String userId, String siteId, String passwd) {
        Weibo ret = new Weibo();
        ret.setUserId(userId);
        ret.setSiteId(siteId);
        ret.setPasswd(passwd);
        return ret;
    }

    public static ConcernedWeibo ConcernedWeibo(String name, int objectType, String weiboURL) {
        ConcernedWeibo ret = new ConcernedWeibo();
        ret.setName(name);
        ret.setObjectType(objectType);
        ret.setWeiboURL(weiboURL);
        return ret;
    }

    public static Follow Follow(long wid, long cwid, int status) {
        Follow ret = new Follow();
        ret.setWid(wid);
        ret.setCwid(cwid);
        ret.setStatus(status);
        return ret;
    }
}
