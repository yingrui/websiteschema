/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.rdbms;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import websiteschema.model.domain.Channel;

/**
 *
 * @author ray
 */
public interface ChannelMapper {

    public long getTotalResults(Map map);

    public List<Channel> getChannels(Map map);

    public List<Channel> getChildChannels(long parentId);

    public List<Channel> getChannelsBySiteId(String siteId);

    public List<Channel> getAllChannels();

    public Channel getChannel(@Param("siteId") String siteId, @Param("url") String url);

    public Channel getById(long id);

    public void setHasLeaf(long id);

    public void insert(Channel channel);

    public void update(Channel channel);

    public void delete(Channel channel);

    public void deleteById(long id);
}
