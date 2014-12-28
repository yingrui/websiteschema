/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.core.compiler;

/**
 *
 * @author ray
 */
public class EventOutputMissDestFBException extends Exception {

    String msg = "";

    public EventOutputMissDestFBException() {
    }

    public EventOutputMissDestFBException(String message) {
        msg = message;
    }

    @Override
    public String getMessage() {
        return msg;
    }

    @Override
    public String toString() {
        return "Application compile exception : " + msg;
    }
}
