/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.crawler.fb.index;

import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author ray
 */
public interface IndexContentConstructor {

    /**
     * 将Unit构建成即将索引的文本内容。
     * @param unit
     * @return
     */
    public String buildUnitContent(Map<String, String> unit);

    /**
     * 将Unit构建成即将索引的文本内容。
     * @param unit
     * @return
     */
    public void buildUnitContent(Map<String, String> unit, Document doc, Element root);

}
