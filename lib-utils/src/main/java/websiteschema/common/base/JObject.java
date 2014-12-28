/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.common.base;

import java.util.Map;

/**
 * A JObject could be initialize by a Map<String,String>
 * @author ray
 */
public interface JObject {

    /**
     * 用analyze方法得到的结果去初始化一个FieldAnalyzer，根据谁分析谁负责抽取的原则设计。
     * @param params - 用analyze方法得到的Map
     */
    public void init(Map<String, String> params);
}
