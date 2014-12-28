/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.cluster.analyzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 *
 * @author mgd
 */
public class SimpleLogger {

    private static PrintWriter pw = null;
    private static Calendar calendar = new GregorianCalendar();
    private static String DIR_BASE_STR = "D:/logs/";

    static {// 初始化块
        init();
    }

    private static String get_time_str() {
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String tmp_url_str = "";
        tmp_url_str = tmp_url_str + calendar.get(Calendar.YEAR)
                + "-" + (calendar.get(Calendar.MONTH) + 1)
                + "-" + calendar.get(Calendar.DAY_OF_MONTH)
                + "-" + calendar.get(Calendar.HOUR_OF_DAY)
                + "-" + calendar.get(Calendar.MINUTE);
        return tmp_url_str;
    }

    private static void init() {
        try {
            File dir_base = new File(DIR_BASE_STR);
            if (!dir_base.exists()) {
                dir_base.mkdir();
            }
            pw = new PrintWriter(new BufferedWriter(
                    new FileWriter(DIR_BASE_STR + get_time_str() + "_cluster.txt")));
        } catch (Exception e) {
            System.err.println("日志文件流异常!");
        }
    }

    /*  记录一行日志 */
    public static void record_line(String line_str) {
        pw.println(line_str);
        // pw.flush();
    }

    /* 结束日志块 */
    public static void end_block() {
        pw.println("-------------------------------------------\r\n\r\n");
        pw.flush();
    }


//    /* 测试代码 */
//    public static void main(String[] args) {
//        SimpleLogger.record_line("测试记录");
//        SimpleLogger.end_block();
//    }
}