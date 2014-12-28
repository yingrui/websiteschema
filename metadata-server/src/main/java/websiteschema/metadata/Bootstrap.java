/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.metadata;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import websiteschema.metadata.utils.MetadataServerContext;
import websiteschema.persistence.rdbms.SysConfMapper;

/**
 *
 * @author ray
 */
public class Bootstrap extends HttpServlet {

    private Logger l = Logger.getLogger(Bootstrap.class);

    @Override
    public void init() {
        l.info("Bootstrap..");
        ServletContext sc = getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
        SysConfMapper sysConfMapper = ctx.getBean("sysConfMapper", SysConfMapper.class);
        MetadataServerContext.getInstance().setPropLoader(sysConfMapper);
        MetadataServerContext.getInstance().reload();
    }

    @Override
    public void destroy() {
        l.info("Stop..");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setStatus(404);
    }
}
