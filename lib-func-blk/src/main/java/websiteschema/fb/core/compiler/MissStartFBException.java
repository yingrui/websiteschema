/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.core.compiler;

/**
 *
 * @author ray
 */
public class MissStartFBException extends Exception {

    String msg = "";

    public MissStartFBException() {
    }

    public MissStartFBException(String message) {
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
