package websiteschema.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**  
 * 日期操作
 *
 * @author xxx
 * @version 2.0 jdk1.4.0 tomcat5.1.0 * Updated Date:2005/03/10
 */
public class DateUtil {

    public static void main(String[] args) {
        System.out.println(DateUtil.changeTime("2010-03-23 00"));
        System.out.println(DateUtil.changeTime("2010-03-23 00:00"));
        System.out.println(DateUtil.changeTime("2010-03-23 00:00:00"));
        System.out.println(DateUtil.changeTime("2010-03-23 23:00:00"));
        System.out.println(DateUtil.changeTime("2010-03-23 23:02:33"));
        System.out.println(DateUtil.parseDate("2010年03-23 23:02:33来源","yyyy年MM-dd HH:mm:SS"));
    }

    public static String changeTime(String tStr) {
        if (tStr == null) {
            return "";
        }
        String[] dateTime = tStr.split(" ");
        if (dateTime == null) {
            return "";
        }
        if (dateTime.length < 2) {
            return tStr;
        }
        //String []  dateArr = dateTime[0].split("-");
        //String []  timeArr = dateTime[1].split(":");

        //if(dateArr == null || dateArr.length <3) return "";
        StringBuilder sb = new StringBuilder(dateTime[0]);
        //if(timeArr != null){
        if ("00:00:00".equals(dateTime[1]) || "00:00".equals(dateTime[1]) || "00".equals(dateTime[1])) {
            return sb.toString();
        } else if (dateTime[1].endsWith(":00")) {
            sb.append(" ").append(dateTime[1].substring(0, 5));
        } else if (dateTime[1].endsWith("00:00")) {
            sb.append(" ").append(dateTime[1].substring(0, 5));
        } else {
            sb.append(" ").append(dateTime[1]);
        }
        //}
        return sb.toString();
    }

    public static String formatString(String date) {
        try {
            if (date == null || "".equals(date)) {
                return "";
            }
            return date.substring(0, 10);
        } catch (Exception e) {
            return date;
        }
    }

    public static String formatDate(String date) {
        try {
            if (date == null || "".equals(date)) {
                return "";
            }
            String dates[] = date.trim().split(" ");
            Calendar c = Calendar.getInstance();
            if (dates != null && dates.length > 0) {
                String[] ymd = dates[0].trim().replaceAll(":", "-").replaceAll("－", "-").split("-");
                int y = Integer.parseInt(ymd[0]);
                int m = Integer.parseInt(ymd[1]);
                int d = Integer.parseInt(ymd[2]);
                c.set(Calendar.YEAR, y);
                c.set(Calendar.MONTH, m - 1);
                c.set(Calendar.DAY_OF_MONTH, d);
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
            }
            if (dates != null && dates.length == 2) {
                String[] hms = dates[1].replaceAll("-", ":").split(":");
                int h = hms.length > 0 ? Integer.parseInt(hms[0]) : 0;
                int m = hms.length > 1 ? Integer.parseInt(hms[1]) : 0;
                int s = hms.length > 2 ? Integer.parseInt(hms[2]) : 0;
                c.set(Calendar.HOUR_OF_DAY, h);
                c.set(Calendar.MINUTE, m);
                c.set(Calendar.SECOND, s);
            }
            long space = (System.currentTimeMillis() - c.getTimeInMillis()) / (60 * 1000);

            if (space < 0) {
                return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
            }
            if (space < 60) {
                return space + "分钟前";
            } else if (space < (24 * 60)) {
                return space / 60 + "小时前";//+space%60+"分钟前";
            } else if (space < 24 * 60 * 2) {
                return space / (24 * 60) + "天前";//+(space%(24*60))/60+"小时前";
            } else {
                return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
            }
        } catch (Exception e) {
            return date;
        }
    }
    //标准，不使用1小时前等

    public static String formatDateStandard(String date) {
        try {
            if (date == null || "".equals(date)) {
                return "";
            }
            String dates[] = date.trim().split(" ");
            Calendar c = Calendar.getInstance();
            if (dates != null && dates.length > 0) {
                String[] ymd = dates[0].trim().replaceAll(":", "-").replaceAll("－", "-").split("-");
                int y = Integer.parseInt(ymd[0]);
                int m = Integer.parseInt(ymd[1]);
                int d = Integer.parseInt(ymd[2]);
                c.set(Calendar.YEAR, y);
                c.set(Calendar.MONTH, m - 1);
                c.set(Calendar.DAY_OF_MONTH, d);
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
            }
            if (dates != null && dates.length == 2) {
                String[] hms = dates[1].replaceAll("-", ":").split(":");
                int h = hms.length > 0 ? Integer.parseInt(hms[0]) : 0;
                int m = hms.length > 1 ? Integer.parseInt(hms[1]) : 0;
                int s = hms.length > 2 ? Integer.parseInt(hms[2]) : 0;
                c.set(Calendar.HOUR_OF_DAY, h);
                c.set(Calendar.MINUTE, m);
                c.set(Calendar.SECOND, s);
            }
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(c.getTime());
        } catch (Exception e) {
            return date;
        }
    }

    /**
     * 格式化日期
     *
     * @param dateStr
     *            字符型日期
     * @param format
     *            格式
     * @return 返回日期
     */
    public static Date parseDate(String dateStr, String format) {
        Date date = null;
        try {
            DateFormat df = new SimpleDateFormat(format);
            String dt = dateStr;//.trim();
            if (dt.equals("") && (dt.length() != format.length())) {
                throw new Exception();
            }
            date = (Date) df.parse(dt);
        } catch (Exception e) {
        }
        return date;
    }

    public static Date parseDate(String dateStr) {
        if (null == dateStr) {
            return new Date();
        }

        Date d = parseDate(dateStr, "dd/MM/yyyy");
        if (null != d) {
            return d;
        }

        char c = dateStr.charAt(0);
        if ('-' == c) {
            d = new Date();
            String strDate = format(d);
            d = parseDate(strDate);
            d = addDate(d, Integer.parseInt(dateStr));
            return d;
        }

        long i = Long.parseLong(dateStr);
        if (0 < i) {
            d = new Date(i);
            return d;
        }

        return new Date();
    }

    /**
     * 格式化输出日期
     *
     * @param date
     *            日期
     * @param format
     *            格式
     * @return 返回字符型日期
     */
    public static String format(Date date, String format) {
        String result = "";
        try {
            if (date != null) {
                DateFormat df = new SimpleDateFormat(format);
                result = df.format(date);
            }
        } catch (Exception e) {
        }
        return result;
    }

    public static String format(Date date) {
        return format(date, "dd/MM/yyyy");
    }

    public static String format2(Date date) {
        return format(date, "yyyy-MM-dd");
    }

    /**
     * 返回年份
     *
     * @param date
     *            日期
     * @return 返回年份
     */
    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * 返回月份
     *
     * @param date
     *            日期
     * @return 返回月份
     */
    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 返回日份
     *
     * @param date
     *            日期
     * @return 返回日份
     */
    public static int getDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 返回小时
     *
     * @param date
     *            日期
     * @return 返回小时
     */
    public static int getHour(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 返回分钟
     *
     * @param date
     *            日期
     * @return 返回分钟
     */
    public static int getMinute(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MINUTE);
    }

    /**
     * 返回秒钟
     *
     * @param date
     *            日期
     * @return 返回秒钟
     */
    public static int getSecond(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.SECOND);
    }

    /**
     * 返回毫秒
     *
     * @param date
     *            日期
     * @return 返回毫秒
     */
    public static long getMillis(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getTimeInMillis();
    }

    /**
     * 返回字符型日期
     *
     * @param date
     *            日期
     * @return 返回字符型日期
     */
    public static int getDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 返回字符型日期时间
     *
     * @param date
     *            日期
     * @return 返回字符型日期时间
     */
    public static String getDateTime(Date date) {
        return format(date, "dd/MM/yyyy HH:mm:ss");
    }

    /**
     * 日期相加
     *
     * @param date
     *            日期
     * @param day
     *            天数
     * @return 返回相加后的日期
     */
    public static Date addDate(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(getMillis(date) + ((long) day) * 24 * 3600 * 1000);
        return c.getTime();
    }

    /**
     * 日期相减
     *
     * @param date
     *            日期
     * @param date1
     *            日期
     * @return 返回相减后的日期
     */
    public static int diffDate(Date date, Date date1) {
        return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
    }

    /**
     * 格式化秒格式，输入：150303，输出：15:03:03
     */
    public static String format_sec(String str, String r) {
        if (str == null || str.length() != 6) {
            return r;
        }
        return str.substring(0, 2) + "-" + str.substring(2, 4) + "-" + str.substring(4, 6);
    }

    /**
     * 格式化日期：date
     * 输入：2008-03-09|2008/03/09|2008.03.09
     * 输出：指定的格式
     */
    public static String format_date(String date, String pattern, String r) {
        if (date == null || "".equals(date)) {
            return r;
        }
        Calendar c = readCalendar(date);
        if (c == null) {
            return r;
        }
        SimpleDateFormat s = new SimpleDateFormat(pattern);
        String rs = r;
        try {
            rs = s.format(c.getTime());
        } catch (Exception e) {
            return r;
        }
        return rs;
    }

    /**
     * 日期读入到Calendar
     */
    public static Calendar readCalendar(String date) {
        try {
            int year = -1;
            int month = -1;
            int day = -1;
            if (date.indexOf("/") != -1) {
                String[] dateArray = date.split("/");
                year = Integer.parseInt(dateArray[0]);
                month = Integer.parseInt(dateArray[1]);
                day = Integer.parseInt(dateArray[2]);
            } else if (date.indexOf("-") != -1) {
                String[] dateArray = date.split("-");
                year = Integer.parseInt(dateArray[0]);
                month = Integer.parseInt(dateArray[1]);
                day = Integer.parseInt(dateArray[2]);
            } else {
                if (date.length() != 8) {
                    return null;
                }
                year = Integer.parseInt(date.substring(0, 4));
                month = Integer.parseInt(date.substring(4, 6));
                day = Integer.parseInt(date.substring(6, 8));
            }

            Calendar c = Calendar.getInstance();
            c.set(year, month - 1, day);
            return c;
        } catch (Exception e) {
            return null;
        }
    }
}
