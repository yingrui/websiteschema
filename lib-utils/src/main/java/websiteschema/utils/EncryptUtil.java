/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.*;

/*
 *  TestEncrypt.java
 *  Author: MKing
 *  Last Date: 2005-11-21
 *  Description: A test progrm to encrypt a string using MD5 or SHA-1,etc.
 */
public class EncryptUtil {

    public EncryptUtil() {
    }

    public String Encrypt(String strSrc, String encName) {
        //parameter strSrc is a string will be encrypted,
        //parameter encName is the algorithm name will be used.
        //encName dafault to "MD5"
        MessageDigest md = null;
        String strDes = null;

        byte[] bt = strSrc.getBytes();
        try {
            if (encName == null || encName.equals("")) {
                encName = "MD5";
            }
            md = MessageDigest.getInstance(encName);
            md.update(bt);
            strDes = bytes2Hex(md.digest());  //to HexString
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Invalid algorithm.");
            return null;
        }
        return strDes;
    }

    @SuppressWarnings("unchecked")
    public String base64(String str) {
        return new sun.misc.BASE64Encoder().encode(str.getBytes());
    }

    public String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        EncryptUtil te = new EncryptUtil();
        String strSrc = "Fengyingrui1!";
        System.out.println("Source String:" + strSrc);
        System.out.println("Encrypted String:");
        System.out.println("Use Def:" + te.Encrypt(strSrc, null));
        System.out.println("Use MD5:" + te.Encrypt(strSrc, "MD5"));
        System.out.println("Use SHA:" + te.Encrypt(strSrc, "SHA-1"));
        System.out.println("Use SHA-256:" + te.Encrypt(strSrc, "SHA-256"));
        System.out.println("Use BASE64:" + te.base64(""));

        //dW5kZWZpbmVk

        //username=sinaSSOEncoder.base64.encode(urlencode(username));
        String username = "yingrui.f@gmail.com";
        System.out.println(URLEncoder.encode(username, "UTF-8"));
        System.out.println("username:" + te.base64(URLEncoder.encode(username, "UTF-8")));
        //sinaSSOEncoder.hex_sha1(""+sinaSSOEncoder.hex_sha1(sinaSSOEncoder.hex_sha1(password))+me.servertime+me.nonce)
        String nonce = "TEZ47T";
        String serverTime = "1333185355";
        String password = "websiteschema";
        System.out.println("password:"+te.Encrypt(te.Encrypt(te.Encrypt(password, "SHA-1"), "SHA-1") + serverTime + nonce, "SHA-1"));
    }
}
