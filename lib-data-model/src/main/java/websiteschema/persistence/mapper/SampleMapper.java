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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websiteschema.common.base.Function;
import websiteschema.common.wrapper.SQLBeanWrapper;
import websiteschema.model.domain.cluster.Sample;
import websiteschema.persistence.Mapper;
import websiteschema.persistence.rdbms.SampleDBMapper;
import websiteschema.utils.DateUtil;

/**
 *
 * @author ray
 */
@Service
public class SampleMapper implements Mapper<Sample> {

    private static Pattern pat = Pattern.compile("([A-z0-9_ .]+)\\+([0-9:\\- ]+).*");
    @Autowired
    SampleDBMapper sampleDBMapper;

    @Override
    public boolean exists(String rowKey) {
        int count = sampleDBMapper.exists(rowKey);
        return count > 0;
    }

    @Override
    public Sample get(String rowKey) {
        Map map = sampleDBMapper.getByRowKey(rowKey);
        return SQLBeanWrapper.getBean(map, Sample.class, false);
    }

    @Override
    public Sample get(String rowKey, String family) {
        if ("cf".equals(family)) {
            return SQLBeanWrapper.getBean(sampleDBMapper.getSimpleResult(rowKey), Sample.class, false);
        }
        return get(rowKey);
    }

    @Override
    public List<Sample> getList(String start, String end) {
        return getList(start, end, null);
    }

    @Override
    public List<Sample> getList(String start, String end, String family) {
        return getList(start, end, family, -1);
    }

    @Override
    public List<Sample> getList(String start, String end, String family, int maxResults) {
        String siteId = getSiteId(start);
        Date startDate = getDate(start);
        Date endDate = getDate(end);
        boolean getAll = null == family;

        Map req = new HashMap();
        req.put("siteId", siteId);
        req.put("s_date", startDate);
        req.put("e_date", endDate);

        List res = null;
        if (getAll) {
            res = sampleDBMapper.getResults(req);
        } else {
            res = sampleDBMapper.getSimpleResults(req);
        }

        if (null != res && !res.isEmpty()) {
            List<Sample> ret = new ArrayList<Sample>();
            for (Object obj : res) {
                ret.add(SQLBeanWrapper.getBean((Map) obj, Sample.class, false));
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

    private Date getDate(String rowKey) {
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
                return d;
            }
        }
        return null;
    }

    @Override
    public void put(Sample obj) {
        if (null != obj) {
            if (exists(obj.getRowKey())) {
                sampleDBMapper.update(SQLBeanWrapper.getMap(obj, Sample.class));
            } else {
                sampleDBMapper.insert(SQLBeanWrapper.getMap(obj, Sample.class));
            }
        }
    }

    @Override
    public void put(List<Sample> lst) {
        if (null != lst) {
            for (Sample obj : lst) {
                put(obj);
            }
        }
    }

    @Override
    public void delete(String rowKey) {
        sampleDBMapper.deleteByRowKey(rowKey);
    }

    @Override
    public void scan(String start, String end, Function<Sample> func) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void batchScan(String start, String end, int batchSize, Function<List<Sample>> func) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
