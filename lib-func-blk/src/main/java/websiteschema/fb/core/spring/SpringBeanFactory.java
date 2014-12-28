/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.core.spring;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 * @author ray
 */
public class SpringBeanFactory {

    private static Map<String, ApplicationContext> storage = new HashMap<String, ApplicationContext>();
    private static final Boolean lock = false;

    public static ApplicationContext getBeanFactory(String context) {
        if (storage.containsKey(context)) {
            return storage.get(context);
        } else {
            synchronized (lock) {
                if (!storage.containsKey(context)) {
                    ApplicationContext ctx = null;
                    File file = new File(context);
                    if (file.exists()) {
                        ctx = new FileSystemXmlApplicationContext(context);
                    } else {
                        ctx = new ClassPathXmlApplicationContext(context);
                    }
                    storage.put(context, ctx);
                }
                return storage.get(context);
            }
        }
    }
}
