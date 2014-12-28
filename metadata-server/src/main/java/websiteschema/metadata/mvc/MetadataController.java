/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.metadata.mvc;

//import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import websiteschema.conf.Configure;

/**
 *
 * @author ray
 */
@Controller
@RequestMapping(value = "/metadata")
public class MetadataController {

    Logger l = Logger.getRootLogger();

//    @RequestMapping(value="/user", method = RequestMethod.GET)
//    public String user() {
//        System.out.println("getUser");
//        return "metadata/user";
//    }
//
//    @RequestMapping(value="/{id}", method = RequestMethod.GET)
//    public String getId(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
//        System.out.println("getUser " + id);
//        return "metadata/user";
//    }
    @RequestMapping(value = "/**", method = RequestMethod.GET)
    public ModelAndView view(HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getQueryString();
        String url = request.getPathInfo();
        l.trace("path:" + url + " uri: " + uri);
        Map model = new HashMap();
        if (null != url && !"".equals(url)) {
            if (url.contains("metadata/site")) {
                model.put("AnalyzerTips", Configure.getDefaultConfigure().getProperty("AnalyzerTips"));
            } else if (url.contains("metadata/url")) {
                model.put("AnalyzerTips", Configure.getDefaultConfigure().getProperty("URLAnalyzerTips"));
            }
        }
        return new ModelAndView(url, model);
    }
}
