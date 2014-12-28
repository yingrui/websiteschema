/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer.fields;

import java.util.*;
import java.util.regex.*;
import websiteschema.cluster.analyzer.Doc;
import websiteschema.cluster.analyzer.IFieldFilter;
import websiteschema.utils.StringUtil;

/**
 *
 * @author ray
 */
public class SourceNameFilter implements IFieldFilter {

    String fieldName = "SOURCENAME";

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void filtering(Doc doc) {
        Collection<String> res = doc.getValues(getFieldName());
        if (null != res && !res.isEmpty()) {
            String sourcename = findTarget(res);
            if (null != sourcename) {
                doc.remove(getFieldName());
                doc.addField(getFieldName(), sourcename);
            } else {
                if (res.size() > 1) {
                    res.removeAll(res);
                }
            }
        }
    }

    public String filtering(String sourcename) {
        if (null != sourcename) {
            if (sourcename.matches("([一-龥]+)(-[一-龥]+)?")) {
                //来源全部是汉字，并且长度小于15。
                if (sourcename.length() < 15) {
                    return sourcename;
                } else {
                    return null;
                }
            } else {
                Pattern pat = Pattern.compile(".*(来源|来自)([:： 　]+)(([一-龥]+)(-[一-龥]+)?)\\b.*");
                Matcher m = pat.matcher(sourcename);
                if (m.matches()) {
                    String ret = m.group(3);
                    if (!ret.endsWith("-") && !ret.startsWith("-")) {
                        return ret;
                    }
                } else {
                    String[] array = sourcename.split("[ 　]+");
                    for (String candidate : array) {
                        candidate = StringUtil.trim(candidate);
                        if (candidate.matches("([一-龥]+)(-[一-龥]+)?")) {
                            if (candidate.matches("(.*(网|网站|报|社|在线|频道|中心|博客))|((网易|搜狐|新浪|腾讯|中国).*)")) {
                                return candidate;
                            } else {
                                if (candidate.length() <= 4) {
                                    if (candidate.matches(".*(新闻|视频|图片|评论|财经|股票|港股|基金|娱乐|明星|电影|音乐|体育|NBA|彩票|奥运|汽车|房产|家居|家电|科技|数码|手机|下载|女性|情感|美容|育儿|时尚|购物|旅游|杂志|宏观|金融|国际|博客|股票|行情|操盘|新股|港股|美股|期货|商业|能源|交通|房产|汽车|商贸|基金|净值|理财).*")) {
                                        return candidate;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private String findTarget(Collection<String> old) {
        for (String sourcename : old) {
            sourcename = filtering(sourcename);
            if (null != sourcename) {
                return sourcename;
            }
        }
        return null;
    }

    public void init(Map<String, String> params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
