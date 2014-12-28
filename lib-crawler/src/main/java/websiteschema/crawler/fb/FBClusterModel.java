/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.io.File;
import java.util.concurrent.locks.ReentrantLock;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.model.domain.cluster.ClusterModel;
import websiteschema.persistence.Mapper;
import websiteschema.utils.FileUtil;
import websiteschema.utils.PojoMapper;

/**
 *
 * @author mgd
 */
@EI(name = {"EI:M"})
@EO(name = {"EO", "FAIL"})
public class FBClusterModel extends FunctionBlock {

    @DI(name = "SITE")
    public String siteId_str;
    @DI(name = "CACHE", desc = "是否使用本地缓存")
    public boolean is_cached = false;
    @DI(name = "TIMEOUT", desc = "默认240分钟")
    public int timeout = 240 * 60 * 1000;
    @DI(name = "LOCAL", desc = "缓存地址，默认：./cache")
    public String dir_str;
    @DO(name = "CM", relativeEvents = {"EO"})
    public ClusterModel cm = null;
    //
    private Mapper<ClusterModel> mapper = null;
    private final static ReentrantLock lock = new ReentrantLock();

    @Algorithm(name = "M")
    public void modeling() {
        mapper = getContext().getSpringBean("clusterModelMapper", Mapper.class);
        if (is_cached) {
            lock.lock();
            try {
                Cache cache = new Cache(dir_str);
                cm = cache.get(siteId_str);
                if (null != cm) {
                    cache.put(siteId_str, cm);
                }
            } finally {
                lock.unlock();
            }
        } else {
            cm = mapper.get(siteId_str);
        }
        if (null == cm || false) {
            triggerEvent("FAIL");
        } else {
            triggerEvent("EO");
        }
    }

    class Cache {

        String dir = "cache";

        Cache(String path) {
            if (null != path) {
                dir = path;
            }
        }

        public void put(String key, ClusterModel cm) {
            try {
                File d = new File(dir);
                if (!d.exists()) {
                    d.mkdir();
                }
                File f = new File(dir + File.separator + key + ".cm");
                if (isExpired(f)) {
                    String json = PojoMapper.toJson(cm);
                    save(f, key, json);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public ClusterModel get(String key) {
            try {
                File f = new File(dir + File.separator + key + ".cm");
                if (f.exists()) {
                    if (!isExpired(f)) {
                        String json = FileUtil.read(f);
                        ClusterModel cm = PojoMapper.fromJson(json, ClusterModel.class);
                        return cm;
                    }
                }
                return mapper.get(siteId_str);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        private boolean isExpired(File file) {
            long lastUpdateTime = file.lastModified();
            long now = System.currentTimeMillis();
            if (now - lastUpdateTime > timeout) {
                return true;
            }
            return false;
        }

        private void save(File f, String key, String cm) {
            FileUtil.save(f, cm);
        }
    }
}
