/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.device;

import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import websiteschema.conf.Configure;
import websiteschema.fb.core.app.ApplicationService;
import websiteschema.persistence.rdbms.SysConfMapper;

/**
 *
 * @author ray
 */
public class DeviceContext {

    private static final ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-beans.xml");
    private static DeviceContext ins = new DeviceContext();

    public static DeviceContext getInstance() {
        return ins;
    }

    public static ApplicationContext getSpringContext() {
        return ctx;
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return ctx.getBean(name, clazz);
    }
    private ApplicationService appRuntime;
    private Configure conf;
    private String home;
    private String tempDir;
    private String cacheDir;
    private Logger l = Logger.getLogger(DeviceContext.class);

    DeviceContext() {
        load();
    }

    public final void load() {
        try {
            conf = new Configure("configure-site.ini");
            if (null != conf) {
                //加载数据库中的配置
                SysConfMapper sysConfMapper = getBean("sysConfMapper", SysConfMapper.class);
                conf.setPropLoader(sysConfMapper);
                //获取路径信息
                home = conf.getProperty("Device", "home", "");
                //获取Home的绝对路径
                if (!"".equals(home)) {
                    File h = new File(home);
                    if (!h.exists()) {
                        h.mkdir();
                        home = h.getAbsolutePath();
                    }
                } else {
                    File h = new File("");
                    home = h.getAbsolutePath();
                }
                tempDir = home + File.separator + conf.getProperty("Device", "tempDir", "temp");
                cacheDir = home + File.separator + conf.getProperty("Device", "cacheDir", "cache");
                File tmp = new File(tempDir);
                if (!tmp.exists()) {
                    tmp.mkdir();
                }
                File cache = new File(cacheDir);
                if (!cache.exists()) {
                    cache.mkdir();
                }
            } else {
                l.error("Can not load configuration file: configure-site.ini");
                System.exit(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ApplicationService getAppRuntime() {
        return appRuntime;
    }

    public String getCacheDir() {
        return cacheDir;
    }

    /**
     * 如果此文件在Cache目录下存在，则返回，否则
     * @param filename
     * @return
     */
    public File getCacheFile(String filename) {
        if (null != filename) {
            filename = getCacheDir() + File.separator + filename;
            File f = new File(filename);
            if (f.exists()) {
                return f;
            }
        }
        return null;
    }

    /**
     * 在Cache目录下创建一个新文件
     * @param filename
     * @return
     * @throws IOException
     */
    public File createCacheFile(String filename) throws IOException {
        if (null != filename) {
            filename = getCacheDir() + File.separator + filename;
            File f = new File(filename);
            if (!f.exists()) {
                f.createNewFile();
                return f;
            }
        }
        return null;
    }

    public Configure getConf() {
        return conf;
    }

    public String getHome() {
        return home;
    }

    public String getTempDir() {
        return tempDir;
    }
}
