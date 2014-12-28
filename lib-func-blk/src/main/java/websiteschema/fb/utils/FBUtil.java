/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.DataLink;
import websiteschema.fb.core.EventLink;
import websiteschema.fb.core.FBInfo;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.fb.core.RuntimeContext;
import websiteschema.fb.factory.ClassLoaderFBFactory;
import websiteschema.fb.factory.FBFactory;

/**
 *
 * @author ray
 */
public class FBUtil {

    Logger l = Logger.getLogger(FBUtil.class);
    static final FBUtil ins = new FBUtil();
    List<FBFactory> listFBFactory;

    FBUtil() {
        listFBFactory = new ArrayList<FBFactory>();
        FBFactory factory = new ClassLoaderFBFactory();
        listFBFactory.add(factory);
    }

    public static FBUtil getInstance() {
        return ins;
    }

    final public void registerFBFactory(FBFactory factory) {
        String name = factory.getClass().getName();
        // If already contains same factory, then return directly.
        for (FBFactory f : listFBFactory) {
            if (f.getClass().getName().equals(name)) {
                System.out.println("find same FBFactory: " + name);
                return;
            }
        }
        listFBFactory.add(factory);
    }

    public FunctionBlock loadFunctionBlock(String fbName, RuntimeContext context) {
        try {
            FunctionBlock fb = create(fbName, context);
            if (null != fb) {
                Class clazz = fb.getClass();
                FBInfo info = new FBInfo(clazz);
                context.addFunctionBlockInfo(clazz, info);

                loadDefaultParameters(fb, clazz, info, context);
                loadEventLinks(fb, clazz, info, context);
                loadDataLinks(fb, clazz, info, context);
                return fb;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            l.error(ex);
        }
        return null;
    }

    private FunctionBlock create(String fbName, RuntimeContext context) {
        FunctionBlock fb = null;
        for (FBFactory factory : listFBFactory) {
            fb = factory.create(fbName, context);
            if (null != fb) {
                return fb;
            }
        }
        return fb;
    }

    public void loadDefaultParameters(FunctionBlock fb, Class clazz, FBInfo info, RuntimeContext context) throws IllegalArgumentException, IllegalAccessException {
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(DI.class)) {
                DI _di = field.getAnnotation(DI.class);
                Class type = field.getType();
                String paramName = "DI." + _di.name();
                Object defaultData = context.getConfig().getBean(fb.getName(), paramName, type);
                if (null != defaultData) {
                    field.set(fb, defaultData);
                }
            }
        }
    }

    public void loadEventLinks(FunctionBlock fb, Class clazz, FBInfo info, RuntimeContext context) {
        if (clazz.isAnnotationPresent(EO.class)) {
            EO _eo = (EO) clazz.getAnnotation(EO.class);
            String[] eventOutputs = _eo.name();
            for (String eo : eventOutputs) {
                String key = "EO." + eo;
                Map<String, String> links = context.getConfig().getMapProperty(fb.getName(), key);
                if (null != links) {
                    for (String dest : links.keySet()) {
                        String ei = links.get(dest);
                        if (null != ei) {
                            EventLink elink = new EventLink();
                            elink.src = fb.getName();
                            elink.dest = dest;
                            elink.eventOutput = eo;
                            elink.eventInput = ei;
                            context.addEventLink(elink);
                        }
                    }
                }
            }
        }
    }

    public void loadDataLinks(FunctionBlock fb, Class clazz, FBInfo info, RuntimeContext context) {
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(DO.class)) {
                DO _do = field.getAnnotation(DO.class);
                String key = "DO." + _do.name();
                Map<String, String> links = context.getConfig().getMapProperty(fb.getName(), key);
                if (null != links) {
                    for (String dest : links.keySet()) {
                        String di = links.get(dest);
                        if (null != di) {
                            DataLink dlink = new DataLink();
                            dlink.src = fb.getName();
                            dlink.dataOutput = _do.name();
                            dlink.dest = dest;
                            dlink.dataInput = di;
                            context.addDataLink(dlink);
                        }
                    }
                }
            }
        }
    }
}
