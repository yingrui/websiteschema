/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.device.handler;

import org.apache.log4j.Logger;
import websiteschema.utils.MD5;
import websiteschema.utils.FileUtil;
import java.io.File;
import websiteschema.device.DeviceContext;
import websiteschema.model.domain.Wrapper;
import websiteschema.persistence.rdbms.WrapperMapper;
import static websiteschema.device.DeviceContext.*;

/**
 * 负责缓存Wrapper
 * @author ray
 */
public class WrapperHandler {

    private static WrapperHandler ins = new WrapperHandler();
    private WrapperMapper wrapperMapper = getSpringContext().getBean("wrapperMapper", WrapperMapper.class);
    private static final int hour = 60 * 60 * 1000;
    private Logger l = Logger.getLogger(DeviceContext.class);

    public static WrapperHandler getInstance() {
        return ins;
    }

    public Wrapper getWrapper(long wrapperId) {
        Wrapper ret = getCachedWrapper(wrapperId);
        if (null != ret) {
            return ret;
        } else {
            ret = getWrapperFromRemote(wrapperId);
            if (null != ret) {
                saveWrapper(ret);
            }
        }
        return ret;
    }

    private boolean isExpired(File file) {
        long lastUpdateTime = file.lastModified();
        long now = System.currentTimeMillis();
        if (now - lastUpdateTime > hour) {
            return true;
        }
        return false;
    }

    /**
     * 根据ID，从服务器端读取Wrapper。
     * @param wrapperId
     * @return
     */
    private Wrapper getWrapperFromRemote(long wrapperId) {
        l.debug("load cache from remote: " + wrapperId);
        return wrapperMapper.getById(wrapperId);
    }

    /**
     * 从cache目录中读取已经存在的Wrapper，这里只保存类型为TYPE_FB的Wrapper。
     * 如果Wrapper已经过期了，就删除。
     * @param wrapperId
     * @return
     */
    private Wrapper getCachedWrapper(long wrapperId) {
        try {
            File fileLock = DeviceContext.getInstance().getCacheFile(wrapperId + ".wrapper.lock");
            int count = 5;
            while (null != fileLock && fileLock.exists() && count-- > 0) {
                Thread.sleep(100);
                l.debug("wait for lock: " + fileLock.getAbsolutePath());
            }
            File fileWrapper = DeviceContext.getInstance().getCacheFile(wrapperId + ".wrapper");
            if (null != fileWrapper) {
                File fileChecksum = DeviceContext.getInstance().getCacheFile(wrapperId + ".wrapper.md5");
                if (null != fileChecksum) {
                    if (!isExpired(fileWrapper)) {

                        String content = FileUtil.read(fileWrapper).trim();
                        String md5 = MD5.getMD5(content.getBytes("UTF-8"));
                        String checksum = FileUtil.read(fileChecksum).trim();
                        if (md5.equals(checksum)) {
                            Wrapper wrapper = new Wrapper();
                            wrapper.setId(wrapperId);
                            wrapper.setApplication(content);
                            wrapper.setChecksum(checksum);
                            wrapper.setWrapperType(Wrapper.TYPE_FB);
                            l.debug("get Wrapper from cache: " + wrapperId);
                            return wrapper;
                        } else {
                            l.debug("cached checksum wasn't correct: " + wrapperId);
                        }
                    } else {
                        //Wrapper已经过期了
                        // do Nothing
                        l.debug("cache expired: " + fileWrapper.getAbsolutePath());
                    }
                }
            }
        } catch (Exception ex) {
            l.error("load cache failure", ex);
        }
        return null;
    }

    /**
     * 这里只保存类型为TYPE_FB的Wrapper。
     * @param wrapper
     */
    private void saveWrapper(Wrapper wrapper) {
        if (null != wrapper && Wrapper.TYPE_FB.equals(wrapper.getWrapperType())) {
            long wrapperId = wrapper.getId();
            try {
                File fileLock = DeviceContext.getInstance().getCacheFile(wrapperId + ".wrapper.lock");
                if (null == fileLock || !fileLock.exists()) {
                    fileLock = DeviceContext.getInstance().createCacheFile(wrapperId + ".wrapper.lock");
                    l.debug("save cache wrapper: " + wrapperId);
                    try {
                        File fileWrapper = DeviceContext.getInstance().getCacheFile(wrapperId + ".wrapper");
                        if (null == fileWrapper) {
                            fileWrapper = DeviceContext.getInstance().createCacheFile(wrapperId + ".wrapper");
                        }
                        File fileChecksum = DeviceContext.getInstance().getCacheFile(wrapperId + ".wrapper.md5");
                        if (null == fileChecksum) {
                            fileChecksum = DeviceContext.getInstance().createCacheFile(wrapperId + ".wrapper.md5");
                        }
                        FileUtil.save(fileWrapper, wrapper.getApplication());
                        FileUtil.save(fileChecksum, wrapper.getChecksum());
                    } finally {
                        //如果成功创建锁文件，则要保证删除锁文件
                        fileLock.delete();
                    }
                }
            } catch (Exception ex) {
                l.error("save cache failure", ex);
            }
        }
    }
}
