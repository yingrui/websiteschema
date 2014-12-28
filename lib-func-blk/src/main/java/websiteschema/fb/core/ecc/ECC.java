/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.fb.core.ecc;

import java.util.*;

/**
 *
 * @author ray
 */
public class ECC {

    public final static String START = "START";
    public String status = START;
    //Map<Status, Map<Event, ExecutionControl>>
    Map<String, Map<String, ExecutionControl>> ecc = new HashMap<String, Map<String, ExecutionControl>>();

    public ExecutionControl getExecutionControl(String evt) {
        if (ecc.containsKey(getStatus())) {
            Map<String, ExecutionControl> map = ecc.get(getStatus());
            return map.get(evt);
        }
        return null;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
