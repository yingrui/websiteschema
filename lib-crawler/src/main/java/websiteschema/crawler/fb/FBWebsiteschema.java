/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import websiteschema.fb.annotation.*;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.model.domain.Websiteschema;
import websiteschema.persistence.Mapper;

/**
 *
 * @author ray
 */
@EO(name = {"EO", "FAIL"})
@EI(name = {"EI:INIT"})
public class FBWebsiteschema extends FunctionBlock {

    @DI(name = "SITE")
    public String siteId = "";
    @DO(name = "OUT", relativeEvents = {"EO"})
    public Websiteschema out = null;

    @Algorithm(name = "INIT")
    public void create() {
        Mapper<Websiteschema> mapper = getContext().getSpringBeanFactory().getBean("websiteschemaMapper", Mapper.class);
        out = mapper.get(siteId);
        if (null != out) {
            triggerEvent("EO");
        } else {
            triggerEvent("FAIL");
        }
    }
}
