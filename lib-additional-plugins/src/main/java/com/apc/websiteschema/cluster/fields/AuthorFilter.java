/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apc.websiteschema.cluster.fields;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import websiteschema.cluster.analyzer.Doc;
import websiteschema.cluster.analyzer.IFieldFilter;
import websiteschema.mpsegment.core.SegmentEngine;
import websiteschema.mpsegment.core.SegmentResult;
import websiteschema.mpsegment.core.SegmentWorker;
import websiteschema.mpsegment.dict.POSUtil;
import websiteschema.utils.StringUtil;

/**
 *
 * @author ray
 */
public class AuthorFilter implements IFieldFilter {

    String fieldName = "AUTHOR";
    String contentFieldName = "CONTENT";

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public void filtering(Doc doc) {
        Collection<String> res = doc.getValues(getFieldName());
        if (null != res && !res.isEmpty()) {
            String author = findAuthorInField(res);
            if (StringUtil.isNotEmpty(author)) {
                doc.remove(getFieldName());
                doc.addField(getFieldName(), author);
            } else {
                if (res.size() > 1) {
                    res.removeAll(res);
                }
            }
        }

        res = doc.getValues(getFieldName());
        if (null == res || res.isEmpty()) {
            String author = findAuthorInContent(doc);
            if (StringUtil.isNotEmpty(author)) {
                doc.addField(getFieldName(), author);
            }
        }
    }

    private SegmentResult segment(String text) {
        SegmentWorker worker = SegmentEngine.instance().getSegmentWorker();
        return worker.segment(text);
    }

    private String findAuthorInField(Collection<String> authorStrSet) {
        for (String authorName : authorStrSet) {
            authorName = findAuthorInField(authorName);
            if (null != authorName) {
                return authorName;
            }
        }
        return null;
    }

    public String findAuthorInField(String authorString) {
        if (null != authorString) {
            SegmentResult words = segment(authorString);
            if (null != words) {
                String ret = "";
                for (int i = 0; i < words.length(); i++) {
                    int tag = words.getPOS(i);
                    if (POSUtil.POS_NR() == tag) {
                        ret += words.getWord(i) + " ";
                    }
                }
                return ret.trim();
            }
        }
        return null;
    }

    private String findAuthorInContent(Doc doc) {
        String content = doc.getValue(contentFieldName);
        if (null != content) {
            String headContent = content.substring(0, content.length() > 100 ? 100 : content.length());
            String author = findAuthorInContent(headContent);
            if (StringUtil.isEmpty(author)) {
                if (content.length() > 100) {
                    String tailContent = content.substring(content.length() - 100);
                    author = findAuthorInContent(tailContent);
                }
            }
            if (StringUtil.isNotEmpty(author)) {
                return author;
            }
        }
        return null;
    }

    private final static int POS_NR = 27;
    private final static int POS_UNKNOWN = 44;
    
    private String findAuthorInContent(String content) {
        String ret = "";

        boolean hasWordAuthor = false;
        int positionWordAuthor = 0;
        SegmentResult words = segment(content);
        Map<String, Integer> names = new LinkedHashMap<String, Integer>();
        int index = 0;
        for (int i = 0; i < words.length(); i++) {
            switch (words.getPOS(i)) {
                // 自定义分词其tag总是1
                case 1:
                    index++;
                    String word = words.getWord(i);
                    if (word.matches("^(记者|作者)") && !hasWordAuthor) {
                        hasWordAuthor = true;
                        positionWordAuthor = index;
                    }
                    if (10001 == words.getDomainType(i)) {
                        names.put(word, index);
                    }
                    break;
                case POS_NR:
                    names.put(words.getWord(i), index++);
                    break;
                case POS_UNKNOWN:
                    break;
                default:
                    index++;
                    break;
            }
        }

        Set<String> nameSet = names.keySet();
        int count = nameSet.size();
        if (count > 0 && hasWordAuthor) {
            for (String key : nameSet) {
                if (key.length() > 1) {
                    int pos = names.get(key);
                    if (pos - positionWordAuthor < 3
                            && pos - positionWordAuthor >= 0) {
                        ret += key + " ";
                    }
                }
            }
        }
        return ret.trim();
    }

    @Override
    public void init(Map<String, String> params) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
}
