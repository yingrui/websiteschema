/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.system.detection;

import java.util.Properties;

/**
 *
 * @author ray
 */
public class SystemDetection {

    public static Properties getProperties() {
        Properties prop = System.getProperties();
        return prop;
    }

    public static String getProperty(String prop) {
        return System.getProperty(prop);
    }

    public static String getProperty(String prop, String def) {
        return System.getProperty(prop, def);
    }

    public static boolean isSupportAWTRobot() {
        return RobotDetection.isSupport();
    }

    public static boolean isSupportCurrentOS() {
        return System.getProperty("os.name").equalsIgnoreCase("Linux");
    }

    public static boolean isCompatibleRuntimeEnv() {
        return isSupportAWTRobot() && isSupportCurrentOS();
    }

    public static void main(String args[]) {
        print("Support AWT Robot: " + SystemDetection.isSupportAWTRobot());
        Properties prop = System.getProperties();
        for (String k : prop.stringPropertyNames()) {
            print(k + " : " + prop.getProperty(k));
        }
    }

    private static void print(String str) {
        System.out.println(str);
    }

    private static void println() {
        System.out.println();
    }
}
