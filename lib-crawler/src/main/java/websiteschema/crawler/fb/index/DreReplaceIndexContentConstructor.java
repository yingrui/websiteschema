/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb.index;

import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import websiteschema.element.DocumentUtil;
import websiteschema.utils.Escape;

/**
 * 
 * @author ray
 */
public class DreReplaceIndexContentConstructor implements IndexContentConstructor {

//    #DREDOCREF http://www.autonomy.com/autonomy/dynamic/autopage442.shtml\n
//    #DREFIELDNAME Country\n
//    #DREFIELDVALUE UK\n
//    #DREFIELDNAME Region\n
//    #DREFIELDVALUE South East\n
//    #DREFIELDNAME OnSale\n
//    #DREDELETEFIELDVALUE Yes\n
    String docRef = "href";

    public DreReplaceIndexContentConstructor() {
    }

    /**
     * 
     * @param unit
     * @return
     */
    @Override
    public String buildUnitContent(Map<String, String> unit) {
        if (null != unit && !unit.isEmpty()) {
            StringBuilder sb = new StringBuilder();

            for (String key : unit.keySet()) {
                String value = unit.get(key);
                if (key.equalsIgnoreCase(docRef)) {
                    value = Escape.escape(Escape.escape(value, "UTF-8"), "UTF-8");
                    sb.insert(0, "#DREDOCREF " + value + "\n");
                } else {
                    sb.append("#DREFIELDNAME ").append(key).append("\n");
                    sb.append("#DREFIELDVALUE ").append(value).append("\n");
                }
            }

            return sb.toString();
        }
        return null;
    }

    @Override
    public void buildUnitContent(Map<String, String> unit, Document doc, Element root) {
    }
}
