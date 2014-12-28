/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb.index;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;

/**
 *
 * @author ray
 */
public class IndexerFactory {

    private static IndexerFactory ins = new IndexerFactory();

    public static IndexerFactory getInstance() {
        return ins;
    }
    private Map<String, HttpClient> repos = new HashMap<String, HttpClient>();

    public HttpClient getHttpClient(String host, int port, String protocol) {
        String key = protocol + "://" + host + ":" + port;
        if (repos.containsKey(key)) {
            return repos.get(key);
        } else {
            return create(key, host, port, protocol);
        }
    }

    public HttpIndexer createIndexer(String host, int port, String protocol) {
        HttpClient httpClient = getHttpClient(host, port, protocol);
        return new HttpIndexer(httpClient);
    }

    private synchronized HttpClient create(String key, String host, int port, String protocol) {
        if (!repos.containsKey(key)) {
            HttpConnectionManager httpConnectionManager = new SimpleHttpConnectionManager();
            HttpClient httpClient = new HttpClient(httpConnectionManager);
            httpClient.getHostConfiguration().setHost(host, port, protocol);
            repos.put(key, httpClient);
        }
        return repos.get(key);
    }
}
