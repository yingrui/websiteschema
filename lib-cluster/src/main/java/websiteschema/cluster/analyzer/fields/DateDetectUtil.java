/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer.fields;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import websiteschema.utils.DateUtil;
import websiteschema.utils.StringUtil;

/**
 *
 * @author ray
 */
public class DateDetectUtil {

    static DateDetectUtil ins = new DateDetectUtil();

    public static DateDetectUtil getInstance() {
        return ins;
    }
    private final static Pattern p1 =
            Pattern.compile("(^|.*?[^0-9])" //开始
            + "(([0-9]{2,4})([-/.年])([0-9]{1,2})([-/.月])([0-9]{1,2})(日| |　)?)" //年月日
            + "(([0-9]{1,2})(:|时)([0-9]{1,2})((:|分)([0-9]{1,2}))?)?" //时分秒
            + "($|.*?)"); //结束

    public String detectPattern(String text) {
        String pat = null;
        Matcher mYmd = p1.matcher(text);
        if (mYmd.matches()) {
            StringBuilder ymd = new StringBuilder();
            String yMd = mYmd.group(2);
            if (null != yMd) {
                String y = mYmd.group(3);
                String s1 = mYmd.group(4);
                String M = mYmd.group(5);
                String s2 = mYmd.group(6);
                String d = mYmd.group(7);
                String s3 = mYmd.group(8);
                String hms = mYmd.group(9);

                //年
                if (null != y) {
                    if (4 == y.length()) {
                        ymd.append("yyyy");
                    } else {
                        ymd.append("yy");
                    }
                }
                //分割符
                if (null != s1) {
                    ymd.append(s1);
                }
                //月
                if (null != M) {
                    ymd.append("MM");
                }
                //分割符
                if (null != s2) {
                    ymd.append(s2);
                }
                //日
                if (null != d) {
                    ymd.append("dd");
                }
                //分割符
                if (null != s3) {
                    ymd.append(s3);
                }
                //
                if (null != hms) {
                    String h = mYmd.group(10);
                    String s4 = mYmd.group(11);
                    String m = mYmd.group(12);
                    String s5 = mYmd.group(14);
                    String s = mYmd.group(15);
                    if (null != h) {
                        ymd.append("HH");
                    }
                    if (null != s4) {
                        ymd.append(s4);
                    }
                    if (null != m) {
                        ymd.append("mm");
                    }
                    if (null != s5 && null != s) {
                        ymd.append(s5).append("SS");
                    }
                }
                if (null != y && null != M && null != d) {
                    pat = ymd.toString();
                }
            }

        }
        return pat;
    }

    public String parseDate(String date, String pat, String format) {
        date = StringUtil.replaceSpace(date);
        pat = StringUtil.replaceSpace(pat);
        String pattern = buildPat(pat);
        if (date.matches(pattern)) {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(date);
            if (m.matches()) {
                String res = m.group(2);
                if (null != res) {
                    Date d = DateUtil.parseDate(res, pat);
                    return DateUtil.format(d, format);
                }
            }
        } else {
            //Heuristic(启发式搜索)
            pattern = buildPatYmd(pat);
            if (date.matches(pattern)) {
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(date);
                if (m.matches()) {
                    String res = m.group(2);
                    if (null != res) {
                        Date d = DateUtil.parseDate(res, pat);
                        if (null != d) {
                            return DateUtil.format(d, format);
                        } else {
                            pat = pat.replaceAll(".SS", "");
                            d = DateUtil.parseDate(res, pat);
                            if (null == d) {
                                pat = pat.replaceAll(".mm", "");
                                d = DateUtil.parseDate(res, pat);
                                if (null == d) {
                                    pat = pat.replaceAll(".HH", "");
                                    d = DateUtil.parseDate(res, pat);
                                }
                            }
                            if(null != d) {
                                return DateUtil.format(d, format);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private String buildPat(String pat) {
        pat = pat.replaceAll("\\.", "\\\\.");
        pat = pat.replaceAll("y+", "([0-9]{2}|[0-9]{4})");
        pat = pat.replaceAll("M+", "([0-9]{1,2})");
        pat = pat.replaceAll("d+", "([0-9]{1,2})");
        pat = pat.replaceAll("H+", "([0-9]{1,2})");
        pat = pat.replaceAll("m+", "([0-9]{1,2})");
        pat = pat.replaceAll("S+", "([0-9]{1,2})");
        pat = "(^|.*?)(" + pat + ")($|.*?)";
        return pat;
    }

    private String buildPatYmd(String pat) {
        pat = pat.replaceAll("\\.", "\\\\.");
        pat = pat.replaceAll("y+", "([0-9]{2}|[0-9]{4})");
        pat = pat.replaceAll("M+", "([0-9]{1,2})");
        pat = pat.replaceAll("d+", "([0-9]{1,2})");
        pat = pat.replaceAll("H.*", "");
        pat += "(([0-9]{1,2})([:：\\-]([0-9]{1,2})([:：\\-]([0-9]{1,2}))?)?)?";
        pat = "(^|.*?)(" + pat + ")($|.*?)";
        return pat;
    }

    public boolean hasYMD(String text) {
        int i = 0;
        if (text.matches(".*\\d{2,4}年.*")) {
            i++;
        }
        if (text.matches(".*\\d{1,2}月.*")) {
            i++;
        }
        if (text.matches(".*\\d{1,2}日.*")) {
            i++;
        }
        if (i >= 3) {
            return true;
        }
        if (text.matches("(^|.*?[^0-9])(([0-9]{2,4})[-/.][0-9]{1,2}([-/.][0-9]{1,2})?)($|[^0-9].*?)")) {
            return true;
        }
        return false;
    }

    public boolean hasSourceOrAuthor(String text) {
        if (text.contains("来源") || text.contains("来自")) {
            return true;
        }
        if (text.contains("作者") || text.contains("来自")) {
            return true;
        }
        if (text.matches(".*网(( .*)|$)") || text.matches(".*报(( .*)|$)")) {
            return true;
        }
        return false;
    }

    public boolean hasPublishDate(String text) {
        if (text.contains("发布时间")) {
            return true;
        }
        return false;
    }

    public boolean hasHMS(String text) {
        if (text.matches(".*([^0-9][0-9]{1,2}:[0-9]{1,2})(:[0-9]{1,2})?.*")) {
            return true;
        }
        return false;
    }

    public boolean hasWeather(String text) {
        if (text.matches(".*(晴|阴|雨|风).*")) {
            return true;
        }
        if (text.contains("天气") || text.contains("今日") || text.contains("今天")) {
            return true;
        }
        return false;
    }

    public boolean hasWeek(String text) {
        if (text.contains("星期")) {
            return true;
        }
        return false;
    }

    public boolean hasMultiYMD(String text) {
        if (text.contains("年")) {
            String str[] = text.split("年");
            if (str.length > 2) {
                boolean b = true;
                for (int i = 0; i < str.length; i++) {
                    String s = str[i];
                    if (i < str.length - 1) {
                        if (!s.matches(".*([0-9]{2}|[0-9]{4})$")) {
                            b = false;
                            break;
                        }
                    }
                }
                if (b) {
                    return true;
                }
            }
        } else {
            text = text.replaceAll("(([0-9]{2}|[0-9]{4})[-/.][0-9]{1,2}([-/.][0-9]{1,2})?)", "#!#");
            int start = text.indexOf("#!#");
            if (start >= 0) {
                int second = text.indexOf("#!#", start + 1);
                if (second > 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
