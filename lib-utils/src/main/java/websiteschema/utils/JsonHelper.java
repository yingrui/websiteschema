/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.utils;

import java.util.Map;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 *
 * @author ray
 */
public class JsonHelper {

    public static Map toMap(String json) {
        if (null != json) {
            JSONObject jmap = JSONObject.fromObject(json);
            return jmap;
        } else {
            return null;
        }
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        String jsonText = json;
        if (null != jsonText) {
            try {
                return (T) JSONObject.toBean(JSONObject.fromObject(jsonText), clazz);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
}
