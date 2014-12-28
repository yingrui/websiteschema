/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb.index;

import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import websiteschema.element.DocumentUtil;

/**
 * 
 * @author ray
 */
public class SimpleIndexContentConstructor implements IndexContentConstructor {

    @Override
    public String buildUnitContent(Map<String, String> unit) {
        if (null != unit && !unit.isEmpty()) {
            Document doc = DocumentUtil.createEmptyDocument();
            Element eleDoc = doc.createElement("DOCUMENT");
            doc.appendChild(eleDoc);

            for (String key : unit.keySet()) {
                String value = unit.get(key);
                Element ele = doc.createElement(key);
                ele.setTextContent(value);
                eleDoc.appendChild(ele);
            }
            return DocumentUtil.getXMLString(doc);
        }
        return null;
    }

    @Override
    public void buildUnitContent(Map<String, String> unit, Document doc, Element root) {
        if (null != unit && !unit.isEmpty()) {
            Element eleDoc = doc.createElement("DOCUMENT");
            root.appendChild(eleDoc);

            for (String key : unit.keySet()) {
                String value = unit.get(key);
                Element ele = doc.createElement(key);
                ele.setTextContent(value);
                eleDoc.appendChild(ele);
            }
        }
    }
}
