/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import websiteschema.persistence.rdbms.StartURLMapper;
import websiteschema.model.domain.Channel;
import websiteschema.persistence.rdbms.ChannelMapper;
import java.util.List;
import java.util.Map;
import java.util.Date;
import websiteschema.dwr.response.ListRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websiteschema.model.domain.StartURL;
import static websiteschema.persistence.rdbms.utils.ParameterUtil.*;

/**
 *
 * @author ray
 */
@Service
public class ChannelService {

    @Autowired
    private ChannelMapper channelMapper;
    @Autowired
    private StartURLMapper startURLMapper;

    public ListRange getResults(Map map) {
        ListRange listRange = new ListRange();
        Map params = buildParamWithInt(map, "start", "limit");
        listRange.setData(channelMapper.getChannels(params).toArray());
        listRange.setTotalSize(channelMapper.getTotalResults(params));
        return listRange;
    }

    public Channel getById(long id) {
        return channelMapper.getById(id);
    }

    @Transactional
    public void insert(Channel chl) {
        chl.setCreateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        chl.setCreateUser(userDetails.getUsername());
        chl.setCreateTime(new Date());
        long parentId = chl.getParentId();
        if (parentId > 0) {
            channelMapper.setHasLeaf(parentId);
        }
        channelMapper.insert(chl);
    }

    @Transactional
    public void update(Channel chl) {
        chl.setUpdateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        chl.setLastUpdateUser(userDetails.getUsername());
        chl.setUpdateTime(new Date());
        channelMapper.update(chl);
    }

    @Transactional
    public void deleteRecord(Channel chl) {
        deleteDescendant(chl.getId());
        channelMapper.delete(chl);
    }

    @Transactional
    public void deleteById(long id) {
        if (id > 0) {
            deleteDescendant(id);
            channelMapper.deleteById(id);
        }
    }

    /**
     * 删除此站点的后代，但不包括此站点
     *
     * @param site
     */
    private void deleteDescendant(long id) {
        List<Channel> children = channelMapper.getChildChannels(id);
        if (null != children) {
            for (Channel child : children) {
                deleteDescendant(child.getId());
                channelMapper.delete(child);
            }
        }

    }

    @Transactional
    public void addStartURL(Channel chl) {
        StartURL startURL = new StartURL();
        startURL.setCreateTime(chl.getCreateTime());
        startURL.setCreateUser(chl.getCreateUser());
        startURL.setLastUpdateUser(chl.getLastUpdateUser());
        startURL.setSiteId(chl.getSiteId());
        startURL.setName(chl.getChannel());
        startURL.setJobname(chl.getSiteId()+ "_" + chl.getId());
        startURL.setStatus(chl.getStatus());
        startURL.setStartURL(chl.getUrl());
        startURLMapper.insert(startURL);
    }
}
