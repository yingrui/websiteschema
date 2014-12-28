/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.login;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import websiteschema.utils.EncryptUtil;
import websiteschema.utils.PojoMapper;

/**
 *
 * @author ray
 */
public class SinaLogin {

    HttpClient httpclient = new HttpClient();
    int servertime;
    String nonce;
    String pcid;
    String username = "websiteschema@gmail.com";
    String password = "websiteschema";
    EncryptUtil te = new EncryptUtil();

    public SinaLogin() {
        // 设置代理服务器地址和端口
        httpclient.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
    }

    public void accessIndex() throws IOException {
        HttpMethod method = new GetMethod("http://weibo.com");
        method.addRequestHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1");
        httpclient.executeMethod(method);
        System.out.println(method.getStatusLine());
        method.releaseConnection();
    }

    private String encodeUserName() {
        try {
            return te.base64(URLEncoder.encode(username, "UTF-8"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String encodePassword() {
        servertime = (int) (System.currentTimeMillis() / 1000);
        return te.Encrypt(te.Encrypt(te.Encrypt(password, "SHA-1"), "SHA-1") + servertime + nonce, "SHA-1");
    }

    public void preLogin() throws IOException {
        ///sso/prelogin.php?entry=weibo&callback=sinaSSOController.preloginCallBack&su=dW5kZWZpbmVk&client=ssologin.js(v1.3.19)&_=1333178409333
        String url = "http://login.sina.com.cn/sso/prelogin.php?entry=weibo&callback=sinaSSOController.preloginCallBack&su=dW5kZWZpbmVk&client=ssologin.js(v1.3.19)&_=" + System.currentTimeMillis();
        HttpMethod method = new GetMethod(url);
        method.addRequestHeader("Referer", "http://weibo.com/");
        method.addRequestHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1");
        httpclient.executeMethod(method);
        System.out.println(method.getStatusLine());
        String ret = method.getResponseBodyAsString();
        ret = ret.replace("sinaSSOController.preloginCallBack(", "");
        ret = ret.replaceAll("\\)$", "");
        System.out.println(ret);
        Map map = PojoMapper.fromJson(ret, Map.class);
        servertime = (Integer) map.get("servertime");
        nonce = (String) map.get("nonce");
        pcid = (String) map.get("pcid");
        method.releaseConnection();
    }

    public void login() throws IOException {
        ///sso/prelogin.php?entry=weibo&callback=sinaSSOController.preloginCallBack&su=dW5kZWZpbmVk&client=ssologin.js(v1.3.19)&_=1333178409333
        String url = "http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.3.19)";

//        Cookie mycookie = new Cookie("login.sina.com.cn", "Apache", "0000000a.2ca72b5f.4f76afe5.f2583d14",
//                "/", null, false);
//        Cookie mycookie2 = new Cookie("login.sina.com.cn", "SINAGLOBAL", "0000000a.2cae2b5f.4f76afe5.2a13e027",
//                "/", null, false);
//        httpclient.getState().addCookie(mycookie);
//        httpclient.getState().addCookie(mycookie2);

        PostMethod method = new PostMethod(url);
        method.addRequestHeader("Referer", "http://weibo.com/");
        method.addRequestHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1");
        NameValuePair[] data = {
            new NameValuePair("entry", "weibo"),
            new NameValuePair("gateway", "1"),
            new NameValuePair("from", ""),
            new NameValuePair("savestate", "7"),
            new NameValuePair("useticket", "1"),
            new NameValuePair("ssosimplelogin", "1"),
            new NameValuePair("vsnf", "1"),
            new NameValuePair("vsnval", ""),
            new NameValuePair("pcid", pcid),
            new NameValuePair("su", encodeUserName()),
            new NameValuePair("service", "miniblog"),
            new NameValuePair("servertime", "" + servertime),
            new NameValuePair("nonce", nonce),
            new NameValuePair("pwencode", "wsse"),
            new NameValuePair("sp", encodePassword()),
            new NameValuePair("encoding", "UTF-8"),
            new NameValuePair("url", "http%3A%2F%2Fweibo.com%2Fajaxlogin.php%3Fframelogin%3D1%26callback%3Dparent.sinaSSOController.feedBackUrlCallBack"),
            new NameValuePair("returntype", "META")
        };
        method.setRequestBody(data);
        httpclient.executeMethod(method);
        System.out.println(method.getStatusLine());
        System.out.println(method.getResponseBodyAsString());
        method.releaseConnection();
    }

    public void printCookie() {
        // Get all the cookies
        Cookie[] cookies = httpclient.getState().getCookies();

        // Display the cookies
        System.out.println("Present cookies: ");
        for (int i = 0; i < cookies.length; i++) {
            System.out.println(" - " + cookies[i].toExternalForm());
        }
    }

    public static void main(String args[]) throws IOException {
        SinaLogin login = new SinaLogin();
        login.accessIndex();
        login.printCookie();
        login.preLogin();
        login.printCookie();
        login.login();
        login.printCookie();
    }
}
