/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.url;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author ray
 */
public class URLClustererTest {

    @Test
    public void test() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(URLClustererTest.class.getClassLoader().getResourceAsStream("url.txt")));
            String line = br.readLine();
            List<URI> setURL = new ArrayList<URI>();
            while (null != line) {
                URI uri = new URI(line);
                setURL.add(uri);
                line = br.readLine();
            }
            br.close();

            URLClusterer clusterer = new URLClusterer();
            clusterer.setClusterSize(25);
            List<URLCluster> result = clusterer.clustering(setURL);
            if (null != result) {
                int i = 0;
                for (URLCluster cluster : result) {
                    List<URLObj> list = cluster.getMembers();
                    for (URLObj obj : list) {
                        System.out.println("cluster " + i + " : " + obj.getUri());
                    }
                    i++;
                    System.out.println();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
