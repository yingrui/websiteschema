/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.weibo.sina.connect;

import org.apache.commons.httpclient.methods.GetMethod;
import weibo4j.model.MySSLSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import websiteschema.weibo.WeiboConnection;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import websiteschema.weibo.ClientContext;
import websiteschema.weibo.sina.Sina;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

import static websiteschema.utils.JsonHelper.*;

/**
 *
 * @author ray
 */
public class SinaConnectionFactory extends ConnectionFactory<Sina> {

    private static final String accessTokenURL = ClientContext.getConfigure().getProperty("SinaWeibo", "accessTokenURL");
    private static final String authorizeURL = ClientContext.getConfigure().getProperty("SinaWeibo", "authorizeURL");
    private static final String clientId = ClientContext.getConfigure().getProperty("SinaWeibo", "clientID");
    private static final String clientSecret = ClientContext.getConfigure().getProperty("SinaWeibo", "clientSecret");
    String providerUserId = "websiteschema@gmail.com";
    String userPassword = "websiteschema";

    public SinaConnectionFactory(String providerUserId, String userPassword) {
        super(clientId, null, new SinaAdapter());
        this.providerUserId = providerUserId;
        this.userPassword = userPassword;
    }

    private String getAuthorizedCode(HttpClient client, String providerUserId, String userPassword) {
        PostMethod postMethod = new PostMethod(authorizeURL);
        postMethod.addParameter("client_id", clientId);     //appkey
        postMethod.addParameter("redirect_uri", "");        //oauth2 回调地址
        postMethod.addParameter("response_type", "code");
        postMethod.addParameter("userId", providerUserId);          //微博帐号
        postMethod.addParameter("passwd", userPassword);    //帐号密码
        postMethod.addParameter("action", "submit");
        try {
            client.executeMethod(postMethod);
            System.out.println(postMethod.getResponseBodyAsString());
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = postMethod.getResponseHeader("location").getValue();
        String params = url.substring(url.lastIndexOf("?") + 1);
        Map<String, String> paramsMap = new HashMap<String, String>();
        for (String s : params.split("&")) {
            String[] t = s.split("=");
            paramsMap.put(t[0], t[1]);
        }
        String code = paramsMap.get("code");
        return code;
    }

    @Override
    public Connection<Sina> createConnection(ConnectionData data) {
        String passwd = userPassword;
        return createConnection(data.getProviderUserId(), userPassword);
    }

    public Connection<Sina> createConnection(String providerUserId, String userPassword) {
        Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);
        Protocol.registerProtocol("https", myhttps);
        HttpClient client = new HttpClient();
        String code = getAuthorizedCode(client, providerUserId, userPassword);
//        String code = "df48147517e051d06b8dce0bc2dbb15d";
        PostMethod tokenMethod = new PostMethod(accessTokenURL);
        tokenMethod.addParameter("client_id", clientId);                //appkey
        tokenMethod.addParameter("client_secret", clientSecret);        //appsecret
//        tokenMethod.addParameter("grant_type", "password");
        tokenMethod.addParameter("grant_type", "authorization_code");
        tokenMethod.addParameter("code", code);
        tokenMethod.addParameter("redirect_uri", "");                   //回调地址
//        tokenMethod.addParameter("username", providerUserId);           //微博帐号
//        tokenMethod.addParameter("pasword", userPassword);              //帐号密码
        try {
            WeiboConnection<Sina> conn = new WeiboConnection<Sina>();
            client.executeMethod(tokenMethod);
            String result = tokenMethod.getResponseBodyAsString();
            System.out.println(result);
            Map res = toMap(result);
            String accessToken = (String) res.get("access_token");
            String refreshToken = (String) res.get("refreshToken");
            int validSecond = (Integer) res.get("expires_in");
            conn.setAccessToken(accessToken);
            conn.setRefreshToken(refreshToken);
            conn.setExpireTime(System.currentTimeMillis() + validSecond * 1000);
            conn.setPassword(userPassword);
            conn.setProviderUserId(providerUserId);
            conn.setProviderId(clientId);
            
            return conn;
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
