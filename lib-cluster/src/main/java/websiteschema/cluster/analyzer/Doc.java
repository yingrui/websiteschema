/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer;

import java.util.*;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import websiteschema.element.W3CDOMUtil;
import websiteschema.utils.CollectionUtil;

/**
 *
 * @author ray
 */
public final class Doc {

    public final static String CONTENT_FIELD = "CONTENT";
    String createTimeField = "CREATE_TIME";
    private Map<String, Collection<String>> data;
    private Map<String, Collection<Map<String, String>>> extData;

    public Doc() {
        data = new HashMap<String, Collection<String>>();
        addField(createTimeField, String.valueOf(System.currentTimeMillis() / 1000));
        extData = new HashMap<String, Collection<Map<String, String>>>();
    }

    public Doc(Document doc) {
        this();
        parseDocument(doc);
    }

    private void parseDocument(Document doc) {
        Element root = doc.getDocumentElement();
        if ("DOCUMENT".equalsIgnoreCase(root.getNodeName())) {
            NodeList children = root.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if (Node.ELEMENT_NODE == child.getNodeType()) {
                    if (isVirtualTextNode(child)) {
                        String name = child.getNodeName();
                        String text = W3CDOMUtil.getInstance().getNodeText(child);
                        addField(name.toUpperCase(), text);
                    } else {
                        // 解析扩展数据
                        parseExtData(child);
                    }
                }
            }
        }
    }

    private void parseExtData(Node node) {
        String name = node.getNodeName();
        NodeList children = node.getChildNodes();
        Map<String, String> obj = new LinkedHashMap<String, String>();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (Node.ELEMENT_NODE == child.getNodeType()) {
                String key = child.getNodeName();
                String value = W3CDOMUtil.getInstance().getNodeText(child);
                obj.put(key, value);
            }
        }
        addExtField(name, obj);
    }

    /**
     * 如果一个节点为TextNode，或仅包含TextNode作为子节点，则此节点为虚拟文本节点。
     * @return
     */
    private boolean isVirtualTextNode(Node node) {
        boolean ret = true;

        NodeList children = node.getChildNodes();
        if (null != children) {
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if (child.getNodeType() != Node.TEXT_NODE) {
                    return false;
                }
            }
        }

        return ret;
    }

    public Set<String> keySet() {
        return data.keySet();
    }

    public Collection<String> getValues(String field) {
        if (null != field) {
            return data.get(field.toUpperCase());
        }
        return null;
    }

    public Collection<Map<String, String>> getExtValues(String field) {
        if (null != field) {
            return extData.get(field.toUpperCase());
        }
        return null;
    }

    public String getValue(String field) {
        Collection<String> values = getValues(field);
        if (null != values) {
            return values.iterator().next();
        }
        return null;
    }

    public List<String> getExtValue(String field) {
        if (null != field) {
            String array[] = field.split("/");
            if (null != array && array.length == 2) {
                return getExtValue(array[0], array[1]);
            }
        }
        return null;
    }

    public List<String> getExtValue(String field, String key) {
        Collection<Map<String, String>> values = getExtValues(field);
        if (null != values) {
            List<String> lst = new ArrayList<String>();
            for (Map<String, String> value : values) {
                lst.add(value.get(key));
            }
            return lst;
        }
        return null;
    }

    public void setValues(String field, Collection<String> values) {
        if (null != field && null != values) {
            field = field.toUpperCase();
            data.put(field, values);
        }
    }

    public void setExtValues(String field, Collection<Map<String, String>> values) {
        if (null != field && null != values) {
            field = field.toUpperCase();
            extData.put(field, values);
        }
    }

    public void remove(String field) {
        data.remove(field);
        extData.remove(field);
    }

    public void remove(String field, String value) {
        if (null != field && null != value) {
            field = field.toUpperCase();
            if (data.containsKey(field)) {
                Collection<String> values = data.get(field);
                if (null != values) {
                    values.remove(value);
                }
            }
        }
    }

    public void addField(String field, String value) {
        if (null != field && null != value) {
            field = field.toUpperCase();
            if (data.containsKey(field)) {
                Collection<String> values = data.get(field);
                if (null != values) {
                    if (!values.contains(value)) {
                        values.add(value);
                    }
                } else {
                    values = new ArrayList<String>();
                    values.add(value);
                    data.put(field, values);
                }
            } else {
                Collection<String> values = new ArrayList<String>();
                values.add(value);
                data.put(field, values);
            }
        }
    }

    // not been tested!
    @Deprecated
    public void addValues(String field, Collection<String> values) {
        if (null != field && null != values) {
            field = field.toUpperCase();
            Collection<String> tmpValues = null;
            if (data.containsKey(field)) {
                tmpValues = data.get(field);
                if (null == tmpValues) {
                    tmpValues = new ArrayList<String>();
                }
            } else {
                tmpValues = new ArrayList<String>();
            }
            tmpValues.addAll(values);
            data.put(field, tmpValues);
        }
    }

    public void addExtField(String field, Map<String, String> value) {
        if (null != field && null != value) {
            field = field.toUpperCase();
            if (extData.containsKey(field)) {
                Collection<Map<String, String>> values = extData.get(field);
                if (null != values) {
                    if (!values.contains(value)) {
                        values.add(value);
                    }
                } else {
                    values = new ArrayList<Map<String, String>>();
                    values.add(value);
                    extData.put(field, values);
                }
            } else {
                Collection<Map<String, String>> values = new ArrayList<Map<String, String>>();
                values.add(value);
                extData.put(field, values);
            }
        }
    }

    /**
     * 将Doc对象转换成DOM
     * @return
     */
    public Document toW3CDocument() {
        return toW3CDocument(null);
    }

    /**
     * 将Doc对象转换成DOM，并且按照tagMap中的映射关系，同时将标签名称替换。
     * @param tagMap
     * @return
     */
    public Document toW3CDocument(Map<String, String> tagMap) {
        Document ret = create();
        Element root = ret.createElement("DOCUMENT");
        ret.appendChild(root);
        toW3CDocument(ret, root, tagMap);
        return ret;
    }

    /**
     * 在指定的DOM对象中，将Doc对象转换成root节点的子节点和其他后代，并且按照tagMap中的映射关系，同时将标签名称替换。
     * @param ret
     * @param root
     * @param tagMap
     */
    public void toW3CDocument(Document ret, Element root, Map<String, String> tagMap) {

        List<String> keys = sort(data.keySet(), tagMap);
        //添加抽取结果到doc中
        for (String field : keys) {
            Collection<String> list = data.get(field);
            if (null != list && !list.isEmpty()) {
                for (String value : list) {
                    String f = field;
                    if (null != tagMap && tagMap.containsKey(field)) {
                        f = tagMap.get(field);
                    }
                    Element ele = ret.createElement(f);
                    ele.setTextContent(value);
                    root.appendChild(ele);
                }
            }
        }

        for (String field : extData.keySet()) {
            Collection<Map<String, String>> list = extData.get(field);
            if (null != list && !list.isEmpty()) {
                for (Map<String, String> obj : list) {
                    String f = field;
                    if (null != tagMap && tagMap.containsKey(field)) {
                        f = tagMap.get(field);
                    }
                    Element ele = ret.createElement(f);

                    for (String key : obj.keySet()) {
                        String value = obj.get(key);
                        String k = key;
                        if (null != tagMap && tagMap.containsKey(key)) {
                            k = tagMap.get(key);
                        }
                        Element child = ret.createElement(k);
                        child.setTextContent(value);
                        ele.appendChild(child);
                    }

                    root.appendChild(ele);
                }
            }
        }
    }

    private List<String> sort(Collection<String> ori, Map<String, String> map) {
        if (!ori.isEmpty() && null != map && !map.isEmpty()) {
            Map<String, String> tags = new HashMap<String, String>();
            for (String str : ori) {
                if (map.containsKey(str)) {
                    tags.put(str, map.get(str));
                } else {
                    tags.put(str, str);
                }
            }
            List<Entry<String, String>> entrys = CollectionUtil.sortMapAsc(tags);
            List<String> ret = new ArrayList<String>();
            for (Entry<String, String> entry : entrys) {
                ret.add(entry.getKey());
            }

            return ret;
        } else {
            List<String> ret = new ArrayList<String>(ori);
            Collections.sort(ret);
            return ret;
        }
    }

    private Document create() {
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true); // never forget this!
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document doc = builder.newDocument();
            return doc;
        } catch (Exception ex) {
            return null;
        }
    }
}
