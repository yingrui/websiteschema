/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence.hbase.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import websiteschema.common.base.Function;
import websiteschema.model.domain.HBaseBean;
import websiteschema.persistence.Mapper;

/**
 *
 * @author ray
 */
public class HBaseMapper<T extends HBaseBean> extends HbaseBasicMapper implements Mapper<T> {

    private Class<T> clazz;
    private JavaBeanWrapper wrapper = JavaBeanWrapper.getInstance();

    public HBaseMapper(Class<T> clazz) {
        super(clazz.getName().replaceAll(".*\\.", "").toLowerCase());
        this.clazz = clazz;
//        HBaseMapperFactory.getInstance().createTableIfNotExists(getTableName(), clazz);
    }

    @Override
    public T get(String rowKey) {
        Result result = select(rowKey);
        return wrapper.getBean(result, clazz);
    }

    @Override
    public T get(String rowKey, String family) {
        Result result = select(rowKey, family);
        return wrapper.getBean(result, clazz);
    }

    public void scan(Function<T> func) {
        ResultScanner rs = scan();
        if (null != rs) {
            try {
                for (Result r : rs) {
                    T obj = wrapper.getBean(r, clazz);
                    func.invoke(obj);
                }
            } finally {
                rs.close();
            }
        }
    }

    public void scan(String start, Function<T> func) {
        if (null != start && !"".equals(start)) {
            ResultScanner rs = scan(start);
            if (null != rs) {
                try {
                    for (Result r : rs) {
                        T obj = wrapper.getBean(r, clazz);
                        func.invoke(obj);
                    }
                } finally {
                    rs.close();
                }
            }
        }
    }

    @Override
    public void scan(String start, String end, Function<T> func) {
        if (null != start && !"".equals(start)) {
            ResultScanner rs = scan(start, end);
            if (null != rs) {
                try {
                    for (Result r : rs) {
                        T obj = wrapper.getBean(r, clazz);
                        func.invoke(obj);
                    }
                } finally {
                    rs.close();
                }
            }
        }
    }

    @Override
    public void batchScan(String start, String end, int batchSize, Function<List<T>> func) {
        if (null != start && !"".equals(start)) {
            ResultScanner rs = scan(start, end);
            if (null != rs) {
                try {
                    List<T> list = new ArrayList<T>(batchSize);
                    for (Result r : rs) {
                        T obj = wrapper.getBean(r, clazz);
                        list.add(obj);
                        if (list.size() >= batchSize) {
                            func.invoke(list);
                            list.clear();
                        }
                    }
                    if (!list.isEmpty()) {
                        func.invoke(list);
                        list.clear();
                    }
                } finally {
                    rs.close();
                }
            }
        }
    }

    @Override
    public List<T> getList(String start, String end) {
        if (null != start && !"".equals(start)) {
            ResultScanner rs = scan(start, end);
            if (null != rs) {
                List<T> ret = new ArrayList<T>();
                try {
                    for (Result r : rs) {
                        T obj = wrapper.getBean(r, clazz);
                        ret.add(obj);
                    }
                } finally {
                    rs.close();
                }
                return ret;
            }
        }
        return null;

    }

    @Override
    public List<T> getList(String start, String end, String family) {
        if (null != start && !"".equals(start)) {
            ResultScanner rs = scan(start, end, family);
            if (null != rs) {
                List<T> ret = new ArrayList<T>();
                try {
                    for (Result r : rs) {
                        T obj = wrapper.getBean(r, clazz);
                        ret.add(obj);
                    }
                } finally {
                    rs.close();
                }
                return ret;
            }
        }
        return null;

    }

    @Override
    public List<T> getList(String start, String end, String family, int maxResults) {
        if (null != start && !"".equals(start)) {
            ResultScanner rs = null;
            if (null != family) {
                rs = scan(start, end, family);
            } else {
                rs = scan(start, end);
            }
            if (null != rs) {
                List<T> ret = new ArrayList<T>();
                try {
                    int count = 0;
                    for (Result r : rs) {
                        if (maxResults >= 0 && count >= maxResults) {
                            break;
                        }
                        T obj = wrapper.getBean(r, clazz);
                        ret.add(obj);
                        count++;
                    }
                } finally {
                    rs.close();
                }
                return ret;
            }
        }
        return null;

    }

    @Override
    public void put(T obj) {
        Map<String, String> record = wrapper.getMap(obj, clazz);
        write(obj.getRowKey(), record);
    }

    @Override
    public void put(List<T> lst) {
        List<Map<String, String>> records = new ArrayList<Map<String, String>>();
        for (T obj : lst) {
            Map<String, String> record = wrapper.getMap(obj, clazz);
            record.put("rowKey", obj.getRowKey());
            records.add(record);
        }
        if (!records.isEmpty()) {
            write(records);
        }
    }
}
