/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import websiteschema.utils.PojoMapper;
import websiteschema.utils.StringUtil;

/**
 *
 * @author ray
 */
public final class AnalysisResult {

    private BasicAnalysisResult basicAnalysisResult = null;
    private Map<String, String> result = null;
    private Map<String, String> fieldAnalyzers = null;
    private Map<String, String> fieldExtractors = null;
    final public static String DefaultClusterName = "custom";

    public AnalysisResult() {
        result = new HashMap<String, String>();
        fieldAnalyzers = new HashMap<String, String>();
        fieldExtractors = new HashMap<String, String>();
    }

    public Map<String, String> getFieldAnalyzers() {
        return fieldAnalyzers;
    }

    public Map<String, String> getFieldExtractors() {
        return fieldExtractors;
    }

    public AnalysisResult(Map<String, String> map) {
        init(map);
    }

    public void init(Map<String, String> map) {
        result = map;
        basicAnalysisResult = AnalysisResultFactory.createBasicAnalysisResult(map);
        fieldAnalyzers = AnalysisResultFactory.getFieldAnalyzers(map);
        fieldExtractors = AnalysisResultFactory.getFieldExtractors(map);
    }

    public Map<String, String> getResult() {
        try {
            if (null != basicAnalysisResult) {
                result.put("ValidNodes", PojoMapper.toJson(basicAnalysisResult.getValidNodes()));
                result.put("InvalidNodes", PojoMapper.toJson(basicAnalysisResult.getInvalidNodes()));
                result.put("TitlePrefix", PojoMapper.toJson(basicAnalysisResult.getTitlePrefix()));
                result.put("TitleSuffix", PojoMapper.toJson(basicAnalysisResult.getTitleSuffix()));
            }
            if (null != fieldAnalyzers && !fieldAnalyzers.isEmpty()) {
                result.put("FieldAnalyzers", PojoMapper.toJson(fieldAnalyzers));
            }
            if (null != fieldExtractors && !fieldExtractors.isEmpty()) {
                result.put("FieldExtractors", PojoMapper.toJson(fieldExtractors));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public Map<String, String> getResult(Map<String, String> old) {
        Map<String, String> ret = null != old ? old : new HashMap<String, String>();
        ret.putAll(getResult());
        return ret;
    }

    public BasicAnalysisResult getBasicAnalysisResult() {
        return basicAnalysisResult;
    }

    public void setBasicAnalysisResult(BasicAnalysisResult basicAnalysisResult) {
        this.basicAnalysisResult = basicAnalysisResult;
    }

    /**
     * 根据fieldName查询相应IFieldAnalyzer的配置
     * @param fieldName
     * @return
     */
    public List<Map<String, String>> getFieldAnalysisResult(String fieldName) {
        return getFieldAnalysisResult(DefaultClusterName, fieldName);
    }

    public List<Map<String, String>> getFieldAnalysisResult(String clusterName, String fieldName) {
        if (fieldAnalyzers.containsKey(fieldName)) {
            return getListByField(clusterName, fieldName);
        }
        return null;
    }

    /**
     * 根据fieldName查询相应IFieldExtractor的配置
     * @param FieldName
     * @return
     */
    public List<Map<String, String>> getFieldExtractorConfig(String fieldName) {
        return getFieldExtractorConfig(DefaultClusterName, fieldName);
    }

    public List<Map<String, String>> getFieldExtractorConfig(String clusterName, String fieldName) {
        if (fieldExtractors.containsKey(fieldName)) {
            return getListByField(clusterName, fieldName);
        }
        return null;
    }

    public List<Map<String, String>> getListByField(String fieldName) {
        String json = result.get(fieldName);
        try {
            List<Map<String, String>> ret = PojoMapper.fromJson(json, List.class);
            return ret;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Map<String, String>> getListByField(String clusterName, String fieldName) {
        String json = result.get(fieldName);
        if (null != json && !"".equals(json)) {
            try {
                List<Map<String, String>> ret = sort(clusterName, PojoMapper.fromJson(json, List.class));
                return ret;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 让Map中，包含指定clusterName的map排在List的前列。
     * @param clusterName
     * @param list
     * @return
     */
    private List<Map<String, String>> sort(String clusterName, List<Map<String, String>> list) {
        if (null != clusterName) {
            List<Map<String, String>> ret = new ArrayList<Map<String, String>>();
            for (Map<String, String> map : list) {
                List<String> properClusters = getProperCluster(map);
                if (properClusters.contains(clusterName)) {
                    ret.add(0, map);
                } else {
                    ret.add(map);
                }
            }
            return ret;
        } else {
            return list;
        }
    }

    public void setFieldAnalysisResult(String fieldName, String clsName, Map<String, String> res) {
        setFieldAnalysisResult(DefaultClusterName, fieldName, clsName, res);
    }

    /**
     *
     * @param clusterName - 
     * @param fieldName - IFieldAnalysis.getFieldName()
     * @param clsName - IFieldAnalysis.getClass().getName()
     * @param res - IFieldAnalysis.getResult()
     */
    public void setFieldAnalysisResult(String clusterName, String fieldName, String clsName, Map<String, String> res) {
        if (null != res && !res.isEmpty()) {
            if (!fieldAnalyzers.containsKey(fieldName)) {
                fieldAnalyzers.put(fieldName, clsName);
            }
            mergeResult(clusterName, fieldName, res);
        }
    }

    /**
     *
     * @param clusterName -
     * @param fieldName - IFieldAnalysis.getFieldName()
     * @param clsName - IFieldAnalysis.getClass().getName()
     * @param res - IFieldAnalysis.getResult()
     */
    public void setFieldExtractorSetting(String clusterName, String fieldName, String clsName, Map<String, String> res) {
        if (null != res && !res.isEmpty()) {
            if (!fieldExtractors.containsKey(fieldName)) {
                fieldExtractors.put(fieldName, clsName);
            }
            mergeResult(clusterName, fieldName, res);
        }
    }

    private void removeProperCluster(String clusterName, List<Map<String, String>> list) {
        for (Map<String, String> map : list) {
            List<String> properClusters = getProperCluster(map);
            if (properClusters.contains(clusterName)) {
                properClusters.remove(clusterName);
                setProperCluster(properClusters, map);
                break;
            }
        }
    }

    public void mergeResult(String clusterName, String fieldName, Map<String, String> res) {
        //如果不为空，则将结果保存到Map中
        if (null != res && !res.isEmpty()) {
            String ori = result.get(fieldName);
            List<Map<String, String>> list = null;
            if (null != ori) {
                list = getListByField(fieldName);
            } else {
                list = new ArrayList<Map<String, String>>();
            }
            removeProperCluster(clusterName, list);
            if (!alreadyHas(list, res, clusterName)) {
                //在res中添加Cluster:[clusterName]
                addProperCluster(clusterName, res);
                list.add(res);
            }
            try {
                //将数据序列化之后，存储在Map中
                result.put(fieldName, PojoMapper.toJson(list));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 检查List中是否已经存在一个Map，这个Map和map一样。
     * @param old
     * @param map
     * @return
     */
    private boolean alreadyHas(List<Map<String, String>> old, Map<String, String> map, String clusterName) {
        if (null != old) {
            try {
                for (Map<String, String> compareTo : old) {
                    boolean same = true;
                    for (String key : map.keySet()) {
                        String value = map.get(key);
                        if (!compareTo.containsKey(key)) {
                            same = false;
                            break;
                        } else {
                            String v1 = compareTo.get(key);
                            if (!value.equals(v1)) {
                                same = false;
                                break;
                            }
                        }
                    }
                    if (same) {
                        addProperCluster(clusterName, compareTo);
                        return true;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 一个字段分析的结果要对应到一个Cluster中，所有其返回的Map中要加一个字段叫Cluster。
     * @param map
     * @return
     */
    public List<String> getProperCluster(Map<String, String> map) {
        String properClusters = map.get("Cluster");
        if (null != properClusters) {
            try {
                List<String> list = PojoMapper.fromJson(properClusters, List.class);
                return list;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    private void setProperCluster(List<String> properClusters, Map<String, String> map) {
        if (null != properClusters) {
            try {
                map.put("Cluster", PojoMapper.toJson(properClusters));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 将对应的cluster名称加入到返回结果中
     * @param clusterName
     * @param map
     */
    private void addProperCluster(String clusterName, Map<String, String> map) {
        List<String> properClusters = getProperCluster(map);
        if (null != properClusters && !properClusters.contains(clusterName)) {
            properClusters.add(clusterName);
        } else {
            properClusters = new ArrayList<String>();
            properClusters.add(clusterName);
        }
        //将添加后的结果写入到Map
        setProperCluster(properClusters, map);
    }

    static class AnalysisResultFactory {

        static BasicAnalysisResult createBasicAnalysisResult(Map<String, String> map) {
            BasicAnalysisResult ret = new BasicAnalysisResult();
            try {
                String vnode = map.get("ValidNodes");
                Set<String> validNodes = StringUtil.isNotEmpty(vnode) ? (Set<String>) PojoMapper.fromJson(vnode, Set.class) : new HashSet<String>();
                ret.setValidNodes(validNodes);
                String ivnode = map.get("InvalidNodes");
                Set<String> invalidNodes = StringUtil.isNotEmpty(ivnode) ? (Set<String>) PojoMapper.fromJson(ivnode, Set.class) : new HashSet<String>();
                ret.setInvalidNodes(invalidNodes);
                String tp = map.get("TitlePrefix");
                Set<String> titlePrefix = StringUtil.isNotEmpty(tp) ? (Set<String>) PojoMapper.fromJson(tp, Set.class) : new HashSet<String>();
                ret.setTitlePrefix(titlePrefix);
                String ts = map.get("TitleSuffix");
                Set<String> titleSuffix = StringUtil.isNotEmpty(ts) ? (Set<String>) PojoMapper.fromJson(ts, Set.class) : new HashSet<String>();
                ret.setTitleSuffix(titleSuffix);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return ret;
        }

        static Map<String, String> getFieldAnalyzers(Map<String, String> map) {
            return getMap("FieldAnalyzers", map);
        }

        static Map<String, String> getFieldExtractors(Map<String, String> map) {
            return getMap("FieldExtractors", map);
        }

        static Map<String, String> getMap(String key, Map<String, String> map) {
            Map<String, String> ret = null;
            try {
                String fa = map.get(key);
                ret = null != fa ? (Map<String, String>) PojoMapper.fromJson(fa, Map.class) : new HashMap<String, String>();
                return ret;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return ret;
        }
    }
}
