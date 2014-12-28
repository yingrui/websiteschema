/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import websiteschema.fb.annotation.*;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.common.wrapper.BeanWrapper;

/**
 *
 * @author ray
 */
@EI(name = {"EI:TRAN"})
@EO(name = {"EO"})
public class FBBeanWrapper extends FunctionBlock {

    @DI(name = "TYPE")
    public String classType;
    @DI(name = "IN")
    public List<Map<String, String>> input;
    @DO(name = "OUT", relativeEvents = {"EO"})
    public List<Object> output;

    @Algorithm(name = "TRAN")
    public void wrap() throws ClassNotFoundException {
        output = wrap(classType, input);
        triggerEvent("EO");
    }

    private List<Object> wrap(String type, List<Map<String, String>> data) throws ClassNotFoundException {
        if (null != data) {
            Class clazz = Class.forName(type);
            List<Object> ret = new ArrayList<Object>(data.size());
            for (Map<String, String> map : data) {
                ret.add(BeanWrapper.getBean(map, clazz));
            }
            return ret;
        }
        return null;
    }
}
