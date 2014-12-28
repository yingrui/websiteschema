/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websiteschema.dwr.response.ListRange;
import websiteschema.model.domain.UrlLink;
import websiteschema.model.domain.UrlLog;
import websiteschema.persistence.Mapper;
import websiteschema.utils.DateUtil;

/**
 *
 * @author ray
 */
@Service
public class UrlLogService {

    @Autowired
    private Mapper<UrlLog> urlLogMapper;
    @Autowired
    private Mapper<UrlLink> urlLinkMapper;
    private Logger l = Logger.getLogger(getClass());

    public ListRange getResults(Map map) {
        ListRange listRange = new ListRange();
        String jobname = (String) map.get("jobname");
        String startTime = (String) map.get("startTime");
        String startRow = jobname + "+" + startTime;
        String endRow = jobname + "+" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm");
        List<UrlLog> res = urlLogMapper.getList(startRow, endRow, null, 20);
        if (null != res) {
            listRange.setData(res.toArray());
            listRange.setTotalSize(Long.valueOf(res.size()));
        } else {
            listRange.setData(new UrlLog[0]);
            listRange.setTotalSize(0L);
        }
        return listRange;
    }

    public UrlLink getUrlLink(UrlLog log) {
        return urlLinkMapper.get(log.getURLRowKey());
    }

    public void deleteRecord(UrlLog log) {
        urlLinkMapper.delete(log.getURLRowKey());
        urlLogMapper.delete(log.getRowKey());
    }
}
