/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.core;

import java.io.InputStream;
import java.util.*;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import websiteschema.fb.utils.FBUtil;
import websiteschema.conf.Configure;
import static websiteschema.fb.core.spring.SpringBeanFactory.getBeanFactory;

/**
 *
 * @author ray
 */
public class RuntimeContext {

    Logger l = Logger.getLogger(RuntimeContext.class);
    private Queue<Event> eventQueue = new LinkedList<Event>();
    private Map<String, FunctionBlock> fbMap = new HashMap<String, FunctionBlock>();
    private Map<Class, FBInfo> fbInfo = new HashMap<Class, FBInfo>();
    private Configure config = null;
    private Map<String, Map<String, List<DataLink>>> dataLinks = new HashMap<String, Map<String, List<DataLink>>>();
    private Map<String, Map<String, List<EventLink>>> evtLinks = new HashMap<String, Map<String, List<EventLink>>>();

    public ApplicationContext getSpringBeanFactory() {
        String bean = config.getProperty("Bean", "SpringBeans", "spring-beans.xml");
        if (null != bean) {
            return getBeanFactory(bean);
        }
        return null;
    }

    public <T> T getSpringBean(String bean, Class<T> clazz) {
        ApplicationContext ctx = getSpringBeanFactory();
        if (null != ctx) {
            try {
                return ctx.getBean(bean, clazz);
            } catch (Exception ex) {
                l.error(ex.getMessage(), ex);
            }
        }
        return null;
    }

    public void loadConfigure(String cp) {
        loadConfigure(cp, null);
    }

    public void loadConfigure(InputStream cp) {
        loadConfigure(cp, null);
    }

    public void loadConfigure(String cp, Map<String, String> prop) {
        config = new Configure(cp, prop);
        String startFB = config.getProperty("StartFB");
        loadFunctionBlock(startFB);
    }

    public void loadConfigure(InputStream cp, Map<String, String> prop) {
        config = new Configure(cp, prop);
        String startFB = config.getProperty("StartFB");
        loadFunctionBlock(startFB);
    }

    public FunctionBlock getStartFB() {
        String startFB = config.getProperty("StartFB");
        return fbMap.get(startFB);
    }

    public Configure getConfig() {
        return config;
    }

    public void setConfig(Configure config) {
        this.config = config;
    }

    private void loadFunctionBlock(String fbName) {
        FunctionBlock fb = FBUtil.getInstance().loadFunctionBlock(fbName, this);
        fbMap.put(fbName, fb);
    }

    public Queue<Event> getEventQueue() {
        return eventQueue;
    }

    public void addEvent(Event evt) {
        getEventQueue().add(evt);
    }

    public void addEventLink(EventLink elink) {
        Map<String, List<EventLink>> mapEvtLink = null;
        if (evtLinks.containsKey(elink.src)) {
            mapEvtLink = evtLinks.get(elink.src);
        } else {
            mapEvtLink = new HashMap<String, List<EventLink>>();
            evtLinks.put(elink.src, mapEvtLink);
        }
        List<EventLink> links = null;
        if (mapEvtLink.containsKey(elink.eventOutput)) {
            links = mapEvtLink.get(elink.eventOutput);
        } else {
            links = new ArrayList<EventLink>();
            mapEvtLink.put(elink.eventOutput, links);
        }
        links.add(elink);
    }

    public void addDataLink(DataLink dlink) {
        Map<String, List<DataLink>> mapDataLink = null;
        if (dataLinks.containsKey(dlink.src)) {
            mapDataLink = dataLinks.get(dlink.src);
        } else {
            mapDataLink = new HashMap<String, List<DataLink>>();
            dataLinks.put(dlink.src, mapDataLink);
        }
        List<DataLink> links = null;
        if (mapDataLink.containsKey(dlink.dataOutput)) {
            links = mapDataLink.get(dlink.dataOutput);
        } else {
            links = new ArrayList<DataLink>();
            mapDataLink.put(dlink.dataOutput, links);
        }
        links.add(dlink);
    }

    public FunctionBlock getFunctionBlockByName(String name) {
        if (!fbMap.containsKey(name)) {
            loadFunctionBlock(name);
        }
        return fbMap.get(name);
    }

    public FBInfo getFunctionBlockInfo(Class clazz) {
        return fbInfo.get(clazz);
    }

    public void addFunctionBlockInfo(Class clazz, FBInfo info) {
        fbInfo.put(clazz, info);
    }

    public List<DataLink> getDataLinks(String name, String dataOutput) {
        if (dataLinks.containsKey(name)) {
            return dataLinks.get(name).get(dataOutput);
        }
        return null;
    }

    public List<EventLink> getEventLinks(String name, String eventOutput) {
        if (evtLinks.containsKey(name)) {
            return evtLinks.get(name).get(eventOutput);
        }
        return null;
    }
}
