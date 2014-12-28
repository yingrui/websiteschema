/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb.bbs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import websiteschema.cluster.analyzer.Link;
import websiteschema.fb.annotation.Description;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.model.domain.Channel;
import websiteschema.persistence.rdbms.ChannelMapper;

/**
 *
 * @author ray
 */
@EI(name = {"EI:ADD"})
@EO(name = {"EO"})
@Description(desc = "将URL添加至表Channel")
public class FBForumList extends FunctionBlock {

    @DI(name = "SITEID", desc = "起始URL的站点ID")
    public String siteId;
    @DI(name = "LINKS", desc = "需要保存的链接列表")
    public List<Link> links;
    private ChannelMapper channelMapper = null;

    @Algorithm(name = "ADD", desc = "将添加链接保存至表Channel")
    public void addList() {
        channelMapper = getContext().getSpringBean("channelMapper", ChannelMapper.class);
        assert (null != links && null != siteId && null != channelMapper);
        saveLinks(links, siteId, channelMapper);
        triggerEvent("EO");
    }

    public static void saveLinks(List<Link> links, String siteId, ChannelMapper channelMapper) {
        List<Channel> channels = channelMapper.getChannelsBySiteId(siteId);
        Set<String> setUrls = new HashSet<String>();
        if (null != channels && !channels.isEmpty()) {
            for (Channel chl : channels) {
                setUrls.add(chl.getUrl());
            }
        }
        for (Link lnk : links) {
            String url = lnk.getHref();
            if (!setUrls.contains(url)) {
                Channel chnl = new Channel();
                chnl.setSiteId(siteId);
                chnl.setUrl(url);
                chnl.setChannel(lnk.getText());
                channelMapper.insert(chnl);
            }
        }
    }
}
