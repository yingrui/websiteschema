/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.persistence;

import java.util.List;
import websiteschema.common.base.Function;

/**
 *
 * @author ray
 */
public interface Mapper<T> {

    public boolean exists(String rowKey);

    public T get(String rowKey);

    public T get(String rowKey, String family);

    public List<T> getList(String start, String end);

    public List<T> getList(String start, String end, String family);

    public List<T> getList(String start, String end, String family, int maxResults);

    public void put(T obj);

    public void put(List<T> lst);

    public void delete(String rowKey);

    public void scan(String start, String end, Function<T> func);

    public void batchScan(String start, String end, int batchSize, Function<List<T>> func);
}
