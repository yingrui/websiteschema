/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;

/**
 *
 * @author ray
 */
public class FBInfo {

    Map<String, String> mapEiAlgorithm = new HashMap<String, String>();
    Map<String, List<String>> mapEvtRelatedData = new HashMap<String, List<String>>();
    List<String> eventOutputs = new ArrayList<String>();
    List<String> eventInputs = new ArrayList<String>();
    Class clazz;

    public FBInfo(Class clazz) {
        this.clazz = clazz;
        parseEI(clazz);
        parseEO(clazz);
    }

    private void parseEI(Class clazz) {
        if (clazz.isAnnotationPresent(EI.class)) {
            EI ei = (EI) clazz.getAnnotation(EI.class);
            String[] array = ei.name();
            for (String str : array) {
                String[] config = str.split(":");
                if (2 == config.length) {
                    eventInputs.add(config[0]);
                    mapEiAlgorithm.put(config[0], config[1]);
                }
            }
        }
    }

    private void parseEO(Class clazz) {
        if (clazz.isAnnotationPresent(EO.class)) {
            EO _eo = (EO) clazz.getAnnotation(EO.class);
            String[] array = _eo.name();
            for (String eo : array) {
                eventOutputs.add(eo);
                List<String> relatedData = getEventRelativeData(eo, clazz);
                mapEvtRelatedData.put(eo, relatedData);
            }
        }
    }

    public String getEIRelatedAlgorithm(String ei) {
        return mapEiAlgorithm.get(ei);
    }

    public List<String> eventRelativeData(String eo) {
        return mapEvtRelatedData.get(eo);
    }

    public Class getClazz() {
        return clazz;
    }

    public List<String> getEventInputs() {
        return eventInputs;
    }

    public List<String> getEventOutputs() {
        return eventOutputs;
    }

    private List<String> getEventRelativeData(String eo, Class clazz) {
        List<String> ret = new ArrayList<String>();
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(DO.class)) {
                DO _do = field.getAnnotation(DO.class);
                String[] relativeEvents = _do.relativeEvents();
                for (String relativeEvent : relativeEvents) {
                    if (relativeEvent.equals(eo)) {
                        ret.add(_do.name());
                    }
                }
            }
        }
        return ret;
    }
}
