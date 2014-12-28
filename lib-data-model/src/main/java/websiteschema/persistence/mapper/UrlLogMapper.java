/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websiteschema.common.base.Function;
import websiteschema.common.wrapper.SQLBeanWrapper;
import websiteschema.model.domain.UrlLog;
import websiteschema.persistence.Mapper;
import websiteschema.persistence.rdbms.UrlLogDBMapper;
import websiteschema.utils.DateUtil;

/**
 *
 * @author ray
 */
@Service
public class UrlLogMapper implements Mapper<UrlLog> {

    Logger l = Logger.getLogger(UrlLogMapper.class);
    private static Pattern pat = Pattern.compile("([A-z0-9_ .]+)\\+([0-9:\\- ]+).*");
    @Autowired
    UrlLogDBMapper urlLogDBMapper;

    @Override
    public boolean exists(String rowKey) {
        int count = urlLogDBMapper.exists(rowKey);
        return count > 0;
    }

    @Override
    public UrlLog get(String rowKey) {
        Map map = urlLogDBMapper.getByRowKey(rowKey);
        return SQLBeanWrapper.getBean(map, UrlLog.class, false);
    }

    @Override
    public UrlLog get(String rowKey, String family) {
        return get(rowKey);
    }

    @Override
    public List<UrlLog> getList(String start, String end) {
        return getList(start, end, null);
    }

    @Override
    public List<UrlLog> getList(String start, String end, String family) {
        return getList(start, end, family, -1);
    }

    public long getTotalResults(String start, String end) {
        String siteId = getSiteId(start);
        long startDate = getDate(start);
        long endDate = getDate(end);

        Map req = new HashMap();
        req.put("siteId", siteId);
        if (startDate > 0) {
            req.put("s_date", startDate);
        }
        if (endDate > 0) {
            req.put("e_date", endDate);
        }
        return urlLogDBMapper.getTotalResults(req);
    }

    @Override
    public List<UrlLog> getList(String start, String end, String family, int maxResults) {
        return getList(start, end, family, 0, maxResults);
    }

    public List<UrlLog> getList(String start, String end, String family, int begin, int maxResults) {
        String siteId = getSiteId(start);
        long startDate = getDate(start);
        long endDate = getDate(end);

        Map req = new HashMap();
        req.put("jobname", siteId);
        if (startDate > 0) {
            req.put("s_date", startDate);
        }
        if (endDate > 0) {
            req.put("e_date", endDate);
        }
        List res = null;
        maxResults = maxResults > 0 ? maxResults : 1000;
        req.put("start", begin);
        req.put("limit", maxResults);
        l.debug(req);
        res = urlLogDBMapper.getResults(req);


        if (null != res && !res.isEmpty()) {
            List<UrlLog> ret = new ArrayList<UrlLog>();
            for (Object obj : res) {
                ret.add(SQLBeanWrapper.getBean((Map) obj, UrlLog.class, false));
            }
            return ret;
        }
        return null;
    }

    private String getSiteId(String rowKey) {
        Matcher m = pat.matcher(rowKey);
        if (m.matches()) {
            return m.group(1);
        } else {
            return rowKey;
        }
    }

    private long getDate(String rowKey) {
        if (null != rowKey) {
            Matcher m = pat.matcher(rowKey);
            if (m.matches()) {
                String date = m.group(2);
                Date d = null;
                if (date.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
                    d = DateUtil.parseDate(date, "yyyy-MM-dd");
                } else if (date.matches("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}")) {
                    d = DateUtil.parseDate(date, "yyyy-MM-dd HH:mm");
                }
                return null != d ? d.getTime() : -1;
            }
        }
        return -1;
    }

    @Override
    public void put(UrlLog obj) {
        if (null != obj) {
            Map map = SQLBeanWrapper.getMap(obj, UrlLog.class);
            String jobname = obj.getJobname();
            if (exists(obj.getRowKey())) {
                map.put("jobname", jobname);
                urlLogDBMapper.update(map);
            } else {
                map.put("jobname", jobname);
                urlLogDBMapper.insert(map);
            }
        }
    }

    @Override
    public void put(List<UrlLog> lst) {
        if (null != lst) {
            for (UrlLog obj : lst) {
                put(obj);
            }
        }
    }

    @Override
    public void delete(String rowKey) {
        urlLogDBMapper.deleteByRowKey(rowKey);
    }

    @Override
    public void scan(String start, String end, Function<UrlLog> func) {
        int batchSize = 100;
        long total = this.getTotalResults(start, end);
        int begin = 0;
        while (begin <= total) {
            List<UrlLog> lst = getList(start, end, null, begin, begin + batchSize);
            if (null != lst) {
                for (UrlLog urlLog : lst) {
                    func.invoke(urlLog);
                }
                begin += lst.size();
                if (lst.size() < batchSize) {
                    break;
                }
            } else {
                break;
            }
        }
    }

    @Override
    public void batchScan(String start, String end, int batchSize, Function<List<UrlLog>> func) {
        long total = this.getTotalResults(start, end);
        int begin = 0;
        while (begin <= total) {
            List<UrlLog> lst = getList(start, end, null, begin, begin + batchSize);
            if (null != lst) {
                func.invoke(lst);
                begin += lst.size();
                if (lst.size() < batchSize) {
                    break;
                }
            } else {
                break;
            }
        }
    }
}
