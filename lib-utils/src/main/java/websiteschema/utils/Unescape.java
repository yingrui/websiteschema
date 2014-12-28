package websiteschema.utils;

import java.io.UnsupportedEncodingException;

public class Unescape {

    public static void main(String args[]) {
        System.out.println(unescape(" &sms.chl=%E8%B4%A2%E9%87%91%E6%96%B0%E9%97%BB&sms.src=%E7%BB%8F%E6%B5%8E%E5%8F%82%E8%80%83%E6%8A%A5%E7%BD%91&sms.stype=1&sms.msg=%E5%8D%8E%E5%A4%8F%E5%9F%BA%E9%87%91%E4%B8%93%E6%88%B7%E4%B8%9A%E5%8A%A1%E8%A2%AB%E6%9A%82%E5%81%9C&sms.ctime=2010-04-09&sms.refer=http%253A%252F%252Fjjckb%252Exinhuanet%252Ecom%252Fcaijing%252F2010%252D04%252F09%252Fcontent%255F216199%252Ehtm&sms.wtime=1270778728", "utf-8"));
    }

    public static String unescape(String src, String encoding) {
        String ret = "";
        try {
            ret = java.net.URLDecoder.decode(src, encoding);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
    }
}
