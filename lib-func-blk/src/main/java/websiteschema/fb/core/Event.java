/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.fb.core;


/**
 *
 * @author ray
 */
public class Event {

    public FunctionBlock fb;
    public String ei;
    public final static String CEASE_COMMAND = "stop";

    public Event(){
        fb = null;
        ei = null;
    }

    public Event(FunctionBlock fb, String ei){
        this.fb = fb;
        this.ei = ei;
    }

    public static Event CeaseEvent() {
        return new Event(null, CEASE_COMMAND);
    }
}
