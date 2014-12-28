/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import websiteschema.common.base.Function;

/**
 *
 * @author ray
 */
public class FileUtil {

    private static String defaultEnc = "UTF-8";

    public static String read(File file) {
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                return readInputStream(fis, defaultEnc);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (null != fis) {
                    try {
                        fis.close();
                    } catch (Exception ex) {
                    }
                }

            }
        }
        return null;
    }

    public static String read(String file) {
        File f = new File(file);
        return read(f);
    }

    public static String readResource(String resource) {
        InputStream is = FileUtil.class.getClassLoader().getResourceAsStream(resource);
        if (null != is) {
            try {
                return readInputStream(is, defaultEnc);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (null != is) {
                    try {
                        is.close();
                    } catch (Exception ex) {
                    }
                }

            }
        }
        return null;
    }

    public static String readInputStream(InputStream is, String encoding) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is, encoding));
            String line = br.readLine();
            while (null != line) {
                sb.append(line).append("\n");
                line = br.readLine();
            }
        } catch (Exception ex) {
        }
        return sb.toString();
    }

    public static void save(File file, String content) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos, defaultEnc);
            osw.write(content);
            osw.close();
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void scan(File dir, Function<File> handler) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (null != files) {
                for (File f : files) {
                    handler.invoke(f);
                }
            }
        }
    }
}
