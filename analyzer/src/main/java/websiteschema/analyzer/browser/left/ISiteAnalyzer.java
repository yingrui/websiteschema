/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.analyzer.browser.left;

import websiteschema.analyzer.context.BrowserContext;

/**
 *
 * @author ray
 */
public interface ISiteAnalyzer {

    public void setSiteId(String siteId);

    public String getSiteId();

    public void setBrowserContext(BrowserContext context);

    public void setConfigureHandler(IConfigureHandler confHandler);

    public void start();
}
