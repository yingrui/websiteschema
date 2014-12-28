/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain.factory;

import java.util.Date;
import websiteschema.element.XPathAttributes;
import websiteschema.model.domain.Site;
import websiteschema.model.domain.Websiteschema;
import websiteschema.model.domain.cralwer.CrawlerSettings;

/**
 *
 * @author ray
 */
public class WebsiteschemaFactory {

    public static Websiteschema apply(Site site) {
        Websiteschema w = new Websiteschema();

        w.setCreateTime(new Date());
        w.setLastUpdateTime(new Date());
        w.setRowKey(site.getSiteId());

        XPathAttributes attr = new XPathAttributes();
        attr.setUsingClass(true);
        attr.setUsingId(true);
        w.setXpathAttr(attr);

        CrawlerSettings setting = new CrawlerSettings();
        String[] array = {site.getSiteDomain()};
        setting.setMustHave(array);
        w.setCrawlerSettings(setting);
        return w;
    }
}
