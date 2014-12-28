/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.common.wrapper;

import java.util.HashMap;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import static websiteschema.utils.PojoMapper.*;

/**
 *
 * @author ray
 */
public class BeanWrapper {

    public static <T> T getBean(Map<String, String> obj, Class<T> clazz, boolean caseSensitive) {
        Map<String, String> map = obj;
        if (!caseSensitive && null != obj) {
            map = new HashMap<String, String>();
            for (String key : obj.keySet()) {
                map.put(key.toLowerCase(), obj.get(key));
            }
        }
        try {
            T ret = null;
            if (!map.isEmpty()) {
                ret = clazz.newInstance();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    String name = field.getName();
                    if (!caseSensitive) {
                        name = name.toLowerCase();
                    }
                    if (map.containsKey(name)) {
                        String value = map.get(name);
                        set(clazz, ret, field, value);
                    }
                }
            }
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static <T> T getBean(Map<String, String> obj, Class<T> clazz) {
        try {
            T ret = null;
            if (!obj.isEmpty()) {
                ret = clazz.newInstance();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    String name = field.getName();
                    if (obj.containsKey(name)) {
                        String value = obj.get(name);
                        set(clazz, ret, field, value);
                    }
                }
            }
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static void set(Class clazz, Object obj, Field field, String value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        String fieldName = field.getName();
        Class arg = field.getType();
        String setterName = "set" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
        try {
            if (String.class.equals(arg)) {
                Method method = clazz.getMethod(setterName, String.class);
                if (null != method) {
                    method.invoke(obj, value);
                }
            } else if (double.class.equals(arg)) {
                Method method = clazz.getMethod(setterName, double.class);
                if (null != method) {
                    method.invoke(obj, Double.valueOf(value));
                }
            } else if (float.class.equals(arg)) {
                Method method = clazz.getMethod(setterName, float.class);
                if (null != method) {
                    method.invoke(obj, Float.valueOf(value));
                }
            } else if (int.class.equals(arg)) {
                Method method = clazz.getMethod(setterName, int.class);
                if (null != method) {
                    method.invoke(obj, Integer.valueOf(value));
                }
            } else if (long.class.equals(arg)) {
                Method method = clazz.getMethod(setterName, long.class);
                if (null != method) {
                    method.invoke(obj, Long.valueOf(value));
                }
            } else if (boolean.class.equals(arg)) {
                Method method = clazz.getMethod(setterName, boolean.class);
                if (null != method) {
                    method.invoke(obj, Boolean.valueOf(value));
                }
            } else {
                Method method = clazz.getMethod(setterName, arg);
                if (null != method) {
                    try {
                        Object param = fromJson(value, arg);
                        method.invoke(obj, param);
                    } catch (Exception ex) {
                        System.out.println("jackson fromJSON error: " + ex);
                    }
                }
            }
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    public static <T> Map<String, String> getMap(T obj, Class<T> clazz) {
        try {
            Map<String, String> ret = new HashMap<String, String>();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                {
                    String fieldName = field.getName();
                    Class typo = field.getType();
                    String value = "";
                    String getterName = "get" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
                    try {
                        if (String.class.equals(typo)) {
                            Method method = clazz.getMethod(getterName);
                            if (null != method) {
                                value = (String) method.invoke(obj);
                            }
                        } else if (long.class.equals(typo)) {
                            Method method = clazz.getMethod(getterName);
                            if (null != method) {
                                value = String.valueOf(method.invoke(obj));
                            }
                        } else if (int.class.equals(typo)) {
                            Method method = clazz.getMethod(getterName);
                            if (null != method) {
                                value = String.valueOf(method.invoke(obj));
                            }
                        } else if (double.class.equals(typo)) {
                            Method method = clazz.getMethod(getterName);
                            if (null != method) {
                                value = String.valueOf(method.invoke(obj));
                            }
                        } else if (float.class.equals(typo)) {
                            Method method = clazz.getMethod(getterName);
                            if (null != method) {
                                value = String.valueOf(method.invoke(obj));
                            }
                        } else if (boolean.class.equals(typo)) {
                            getterName = getterName.replaceAll("^get", "is");
                            Method method = clazz.getMethod(getterName);
                            if (null != method) {
                                value = String.valueOf(method.invoke(obj));
                            }
                        } else {
                            //获取其他类型数据
                            Method method = clazz.getMethod(getterName);
                            if (null != method) {
                                try {
                                    Object getter = method.invoke(obj);
                                    if (null != getter) {
                                        value = toJson(getter);
                                    } else {
                                        value = null;
                                    }
                                } catch (Exception ex) {
                                    System.out.println("jackson toJSON error: " + ex);
                                }
                            }
                        }
                    } catch (NoSuchMethodException ex) {
                        ex.printStackTrace();
                    }
                    if (null != value) {
                        ret.put(fieldName, value);
                    }
                }
            }
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
