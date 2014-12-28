/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.weibo;

import org.springframework.social.connect.Connection;
import websiteschema.weibo.sina.connect.SinaConnectionFactory;
import websiteschema.weibo.sina.Sina;
import org.junit.Test;
import static java.lang.System.out;

/**
 *
 * @author ray
 */
public class SinaOAuth2Test {

    @Test
    public void test() {
        String clientId = ClientContext.getConfigure().getProperty("SinaWeibo", "clientID");
        String clientSecret = ClientContext.getConfigure().getProperty("SinaWeibo", "clientSecret");
        SinaConnectionFactory connectionFactory =
                new SinaConnectionFactory(clientId, clientSecret);

        Connection<Sina> connection = connectionFactory.createConnection("websiteschema@gmail.com","websiteschema");
        out.println(connection.getKey());
    }

    
}
