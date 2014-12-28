/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.utils;

import java.io.IOException;
import java.util.Map;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 *
 * @author ray
 */
public class HttpClientUtil {

    private static HttpClientUtil ins = new HttpClientUtil();

    public static HttpClientUtil getInstance() {
        return ins;
    }

    public String getURLContent(String url) {
        GetMethod getMethod = new GetMethod(url);
        return executeQuery(getMethod);
    }

    private String executeQuery(HttpMethod method) {
        //设置content-type
        method.setRequestHeader("content-type", "text/html;charset=utf-8");
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());
        //关闭httpclient自动重定向
        method.setFollowRedirects(false);
        try {
            HttpClient httpClient = new HttpClient();
            int statusCode = httpClient.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            String response = method.getResponseBodyAsString();
            return response;
        } catch (HttpException e) {
            //发生致命的异常，可能是协议不对或者返回的内容有问题
            return null;
        } catch (IOException e) {
            //发生网络异常
            return null;
        } finally {
            //释放连接
            method.releaseConnection();
        }
    }
}
