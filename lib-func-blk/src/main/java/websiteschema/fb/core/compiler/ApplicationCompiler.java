/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.core.compiler;

import java.util.*;
import websiteschema.conf.Configure;
import websiteschema.fb.core.FBInfo;

/**
 *
 * @author ray
 */
public class ApplicationCompiler {

    Configure config = null;

    public void setConfig(Configure config) {
        this.config = config;
    }

    public void compile() throws MissStartFBException, EventOutputMissDestFBException, DataOutputMissDestFBException {
        String startFB = null != config.getNamespace() && !"".equals(config.getNamespace())
                ? config.getNamespace() + "." + config.getProperty("StartFB") : config.getProperty("StartFB");
        Map<String, FBInfo> fbs = getAllFunctionBlockInfo();
        if (!fbs.containsKey(startFB.toLowerCase())) {
            throw new MissStartFBException("StartFB is " + startFB);
        }

        for (String fbName : fbs.keySet()) {
            FBInfo fbInfo = fbs.get(fbName);
            checkEventAndDataLink(fbName, fbInfo, config, fbs);
        }
    }

    private void checkEventAndDataLink(String fbName, FBInfo fb, Configure conf, Map<String, FBInfo> fbs) throws EventOutputMissDestFBException, DataOutputMissDestFBException {
        List<String> allEO = fb.getEventOutputs();
        for (String eo : allEO) {
            Map<String, String> map = conf.getMapProperty(fbName, "EO." + eo);
            if (null != map) {
                for (String dest : map.keySet()) {
                    String ei = map.get(dest);
                    String destFBName = null == conf.getNamespace() || "".equals(conf.getNamespace()) ? dest : conf.getNamespace() + "." + dest;
                    FBInfo destFB = fbs.get(destFBName.toLowerCase());
                    if (null == destFB) {
                        throw new EventOutputMissDestFBException(fbName + " EO." + eo + "=" + dest + ":" + ei);
                    }
                }
            }
            List<String> allDO = fb.eventRelativeData(eo);
            for (String DO : allDO) {
                Map<String, String> links = conf.getMapProperty(fbName, "DO." + DO);
                if (null != links) {
                    for (String dest : links.keySet()) {
                        String di = links.get(dest);
                        String destFBName = null == conf.getNamespace() || "".equals(conf.getNamespace()) ? dest : conf.getNamespace() + "." + dest;
                        FBInfo destFB = fbs.get(destFBName.toLowerCase());
                        if (null == destFB) {
                            throw new DataOutputMissDestFBException(fbName + " DO." + DO + "=" + dest + ":" + di);
                        }
                    }
                }
            }
        }
    }

    /**
     * 取得配置文件中所有的功能块
     * @return
     */
    private Map<String, FBInfo> getAllFunctionBlockInfo() {
        Set<String> fields = config.getAllFields();
        Map<String, FBInfo> fbs = new HashMap<String, FBInfo>();
        for (String field : fields) {
            Map<String, String> prop = config.getAllPropertiesInField(field);
            if (prop.containsKey("fbtype")) {
                String fbType = prop.get("fbtype");
                try {
                    Class clazz = Class.forName(fbType);
                    fbs.put(field, new FBInfo(clazz));
                } catch (Exception ex) {
                }
            }
        }
        return fbs;
    }
}
