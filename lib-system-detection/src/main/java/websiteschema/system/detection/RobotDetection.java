/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.system.detection;

import java.awt.Robot;
import java.awt.event.KeyEvent;

/**
 *
 * @author ray
 */
public class RobotDetection {

    public static boolean isSupport() {
        try {
            Robot robot = new Robot();
//            robot.mouseMove(0, 0);
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.keyRelease(KeyEvent.VK_SHIFT);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
