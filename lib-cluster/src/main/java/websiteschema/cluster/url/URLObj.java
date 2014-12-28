/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.cluster.url;

import java.net.URI;

/**
 *
 * @author ray
 */
public class URLObj {

    private URI uri;
    private String path;
    private String query;
    private String schema;
    private String host;

    public URLObj(URI uri) {
        this.uri = uri;
        this.path = uri.getPath();
        this.query = uri.getQuery();
        this.schema = uri.getScheme();
        this.host = uri.getHost();
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }

    public String getQuery() {
        return query;
    }

    public String getSchema() {
        return schema;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }
}
