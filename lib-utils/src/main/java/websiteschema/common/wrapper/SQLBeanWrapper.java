/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.common.wrapper;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import static websiteschema.utils.PojoMapper.*;

/**
 * Only support few Data Type, such as int
 * @author ray
 */
public class SQLBeanWrapper {

    public static <T> T getBean(Map<String, Object> obj, Class<T> clazz, boolean caseSensitive) {
        Map<String, Object> map = obj;
        if (!caseSensitive && null != obj) {
            map = new HashMap<String, Object>();
            for (String key : obj.keySet()) {
                map.put(key.toLowerCase(), obj.get(key));
            }
        }
        try {
            T ret = null;
            if (null != map && !map.isEmpty()) {
                ret = clazz.newInstance();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    String name = field.getName();
                    if (!caseSensitive) {
                        name = name.toLowerCase();
                    }
                    if (map.containsKey(name)) {
                        Object value = map.get(name);
                        if (null != value) {
                            set(clazz, ret, field, value);
                        }
                    }
                }
            }
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static void set(Class clazz, Object obj, Field field, Object value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        String fieldName = field.getName();
        Class arg = field.getType();
        Class typo = value.getClass();
        String setterName = "set" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
        try {
            if (arg.equals(typo)) {
                Method method = clazz.getMethod(setterName, arg);
                method.invoke(obj, value);
            } else if (double.class.equals(arg)) {
                Method method = clazz.getMethod(setterName, arg);
                if (null != method) {
                    if (value instanceof String) {
                        method.invoke(obj, Double.valueOf((String) value));
                    } else if (value instanceof Double) {
                        method.invoke(obj, value);
                    }
                }
            } else if (float.class.equals(arg)) {
                Method method = clazz.getMethod(setterName, arg);
                if (null != method) {
                    if (value instanceof String) {
                        method.invoke(obj, Float.valueOf((String) value));
                    } else if (value instanceof Float) {
                        method.invoke(obj, value);
                    }
                }
            } else if (int.class.equals(arg)) {
                Method method = clazz.getMethod(setterName, arg);
                if (null != method) {
                    if (value instanceof String) {
                        method.invoke(obj, Integer.valueOf((String) value));
                    } else if (value instanceof Integer) {
                        method.invoke(obj, value);
                    }
                }
            } else if (long.class.equals(arg)) {
                Method method = clazz.getMethod(setterName, arg);
                if (null != method) {
                    if (value instanceof String) {
                        method.invoke(obj, Long.valueOf((String) value));
                    } else if (value instanceof Long) {
                        method.invoke(obj, value);
                    }
                }
            } else if (boolean.class.equals(arg)) {
                Method method = clazz.getMethod(setterName, arg);
                if (null != method) {
                    if (value instanceof String) {
                        method.invoke(obj, Boolean.valueOf((String) value));
                    } else if (value instanceof Boolean) {
                        method.invoke(obj, value);
                    }
                }
            } else if (Date.class.equals(arg) && value instanceof Timestamp) {
                Method method = clazz.getMethod(setterName, arg);
                if (null != method) {
                    method.invoke(obj, new Date(((Timestamp) value).getTime()));
                }
            } else if (value instanceof String) {
                Method method = clazz.getMethod(setterName, arg);
                if (null != method) {
                    try {
                        Object param = fromJson((String) value, arg);
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

    public static <T> Map<String, Object> getMap(T obj, Class<T> clazz) {
        try {
            Map<String, Object> ret = new HashMap<String, Object>();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                {
                    String fieldName = field.getName();
                    Class typo = field.getType();
                    Object value = null;
                    String getterName = "get" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
                    try {
                        if (String.class.equals(typo)) {
                            Method method = clazz.getMethod(getterName);
                            if (null != method) {
                                value = method.invoke(obj);
                            }
                        } else if (long.class.equals(typo)) {
                            Method method = clazz.getMethod(getterName);
                            if (null != method) {
                                value = method.invoke(obj);
                            }
                        } else if (int.class.equals(typo)) {
                            Method method = clazz.getMethod(getterName);
                            if (null != method) {
                                value = method.invoke(obj);
                            }
                        } else if (double.class.equals(typo)) {
                            Method method = clazz.getMethod(getterName);
                            if (null != method) {
                                value = method.invoke(obj);
                            }
                        } else if (float.class.equals(typo)) {
                            Method method = clazz.getMethod(getterName);
                            if (null != method) {
                                value = method.invoke(obj);
                            }
                        } else if (boolean.class.equals(typo)) {
                            getterName = getterName.replaceAll("^get", "is");
                            Method method = clazz.getMethod(getterName);
                            if (null != method) {
                                value = String.valueOf(method.invoke(obj));
                            }
                        } else if (Date.class.equals(typo)) {
                            Method method = clazz.getMethod(getterName);
                            if (null != method) {
                                value = method.invoke(obj);
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
