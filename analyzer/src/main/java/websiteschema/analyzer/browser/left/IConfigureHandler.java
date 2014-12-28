/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package websiteschema.analyzer.browser.left;

import java.util.Map;

/**
 *
 * @author ray
 */
public interface IConfigureHandler {

    public void save();

    public Map<String, String> getProperties();

    public void setProperties(Map<String, String> prop);
}
