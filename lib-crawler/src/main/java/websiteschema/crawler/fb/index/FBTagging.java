/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb.index;

import java.util.Map;
import websiteschema.cluster.analyzer.Doc;
import websiteschema.fb.annotation.Description;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.model.domain.Site;
import websiteschema.persistence.rdbms.SiteMapper;

/**
 *
 * @author ray
 */
@EI(name = {"EI:TAG"})
@EO(name = {"EO"})
@Description(desc = "根据数据库websiteschema中的配置，为Doc增加标签")
public class FBTagging extends FunctionBlock {

    @DI(name = "DOC", desc = "输入的Doc对象", relativeEvents = {"EI"})
    @DO(name = "DOC", desc = "输出的Doc对象", relativeEvents = {"EO"})
    public Doc doc;
    @DI(name = "TAGS")
    public Map<String, String> tags;
    @DI(name = "SITE")
    public String siteId;
    @DI(name = "TAG_SITE")
    public String siteTagName = "SOURCEINFO";

    @Algorithm(name = "TAG", desc = "根据配置打标签")
    public void tagging() {
        try {
            if (null != siteId) {
                addSiteName(doc, siteId);
            }
        } catch (Exception ex) {
            l.error(ex.getMessage(), ex);
        } finally {
            triggerEvent("EO");
        }
    }

    private void addSiteName(Doc doc, String siteId) {
        SiteMapper mapper = getContext().getSpringBean("siteMapper", SiteMapper.class);
        Site site = mapper.getBySiteId(siteId);
        String name = site.getSiteName();
        doc.addField(siteTagName, name);
    }
}
