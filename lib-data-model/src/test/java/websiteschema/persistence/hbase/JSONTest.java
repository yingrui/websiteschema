/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.hbase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;
import org.junit.Test;
import websiteschema.model.domain.cluster.DocUnits;
import websiteschema.model.domain.cluster.Unit;
import static websiteschema.utils.PojoMapper.*;

/**
 *
 * @author ray
 */
public class JSONTest {

    @Test
    public void testJSONLib1() {
        Unit unit = new Unit("HTML/HEAD/META", "[]");
        String json = JSONObject.fromObject(unit).toString();
        System.out.println(json);
        Class clazz = Unit.class;
        Unit obj = (Unit) JSONObject.toBean(JSONObject.fromObject(json), clazz);
        JSONObject jobj = JSONObject.fromObject(obj);
        System.out.println(jobj.toString());
    }

    @Test
    public void testJSONLib() {
        try {
            Unit unit = new Unit("HTML/HEAD/META", "[]");
            Unit[] units = new Unit[1];
            units[0] = unit;
            DocUnits docUnits = new DocUnits();
            docUnits.setUnits(units);
//            String json = "{\"units\":[{\"text\":\"[]\",\"xpath\":\"HTML/HEAD/META\"}]}";
            String json = JSONObject.fromObject(docUnits).toString();
            System.out.println(json);
            Class clazz = DocUnits.class;
            Map<String, Class> classMap = new HashMap<String, Class>();
            classMap.put("units", Unit.class);
            DocUnits obj = (DocUnits) JSONObject.toBean(JSONObject.fromObject(json), clazz, classMap);
            JSONObject jobj = JSONObject.fromObject(obj);
            System.out.println(jobj.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("json-lib.jar has bugs! aha.");
        }
    }

    @Test
    public void testJackson() throws IOException {
        Unit unit = new Unit("HTML/HEAD/META", "[]");
        Unit unit2 = new Unit("HTML/HEAD", "[]");
        Unit[] units = new Unit[2];
        units[0] = unit;
        units[1] = unit2;
        DocUnits docUnits = new DocUnits();
        docUnits.setUnits(units);
//            String json = "{\"units\":[{\"text\":\"[]\",\"xpath\":\"HTML/HEAD/META\"}]}";
        String json = toJson(docUnits);
        System.out.println("testJackson: " + json);
        Class clazz = DocUnits.class;
        Map<String, Class> classMap = new HashMap<String, Class>();
        classMap.put("units", Unit.class);

        DocUnits obj = (DocUnits) fromJson(json, clazz);
        System.out.println(obj.getUnits()[0].getText());
    }

    @Test
    public void testJacksonArray() throws IOException {
        Unit unit = new Unit("HTML/HEAD/META", "[]");
        Unit unit2 = new Unit("HTML/HEAD", "[]");
        Unit[] units = new Unit[2];
        units[0] = unit;
        units[1] = unit2;
        String json = toJson(units);
        System.out.println("testJacksonArray: " + json);

        Class clazz = Unit.class;
        Unit[] array = (Unit[]) fromJson(json, units.getClass());
        System.out.println(array[0].getText());
    }
}
