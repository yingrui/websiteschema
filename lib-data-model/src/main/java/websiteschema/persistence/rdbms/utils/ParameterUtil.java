/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author ray
 */
public class ParameterUtil {

    public static Map buildParam(int start, int limit) {
        Map params = new HashMap();
        params.put("start", start);
        params.put("limit", limit);
        return params;
    }

    public static Map buildParam(int start, int limit, String sort) {
        Map params = new HashMap();
        params.put("start", start);
        params.put("limit", limit);
        params.put("sort", sort);
        return params;
    }

    public static Map buildParamWithInt(Map map, String... integer) {
        Map params = new HashMap();
        List integerParams = Arrays.asList(integer);
        Set keySet = map.keySet();
        for (Object key : keySet) {
            if (key instanceof String) {
                Object value = map.get(key);
                if (!"".equals(value)) {
                    if (integerParams.contains(key)) {
                        if (value instanceof String) {
                            int i = Integer.valueOf((String) value);
                            params.put(key, i);
                        }
                    } else {
                        params.put(key, value);
                    }
                }
            }
        }

        return params;
    }

    public static Map buildParam(int start, int limit, String sort, String match) {
        Map params = new HashMap();
        params.put("start", start);
        params.put("limit", limit);
        params.put("sort", sort);
        params.put("match", match);
        return params;
    }

    public static Map with(Map params, String key, Object value) {
        params.put(key, value);
        return params;
    }

    public static Map buildParamWithBean(Object obj) {
        Map params = new HashMap();
        Class clazz = obj.getClass();
        Method[] methods = clazz.getMethods();
        if (null != methods) {
            for (Method method : methods) {
                if (method.isAccessible()) {
                    String name = method.getName();
                    if (name.startsWith("get") && name.length() > 3) {
                        Class[] paramTypes = method.getParameterTypes();
                        if (null == paramTypes || paramTypes.length == 0) {
                            try {
                                Object ret = method.invoke(obj);
                                String fieldname = name.substring(3);
                                fieldname = fieldname.substring(0, 1).toLowerCase() + fieldname.substring(1);
                                params.put(fieldname, ret);
                            } catch (IllegalAccessException ex) {
                                ex.printStackTrace();
                            } catch (IllegalArgumentException ex) {
                                ex.printStackTrace();
                            } catch (InvocationTargetException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return params;
    }
}
