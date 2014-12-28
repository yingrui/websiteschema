/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.common.base;

import java.util.Collection;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author ray
 */
public class Traversal {

    public static void traversal(Node ele, Function<Node> func) {
        if (null != ele) {
            func.invoke(ele);
            NodeList children = ele.getChildNodes();
            if (null != children) {
                for (int i = 0; i < children.getLength(); i++) {
                    Node child = children.item(i);
                    traversal(child, func);
                }
            }
        }
    }

    public static <T> void traversal(Collection<T> list, Function<T> func) {
        for(T t : list) {
            func.invoke(t);
        }
    }

}
