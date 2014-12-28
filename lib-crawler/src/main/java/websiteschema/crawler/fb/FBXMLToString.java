/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.fb;

import websiteschema.fb.core.FunctionBlock;
import org.w3c.dom.Document;
import websiteschema.element.DocumentUtil;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;

/**
 *
 * @author ray
 */
@EO(name = {"EO"})
@EI(name = {"TRAN:CONVERT"})
public class FBXMLToString extends FunctionBlock {

    @DI(name = "DOC")
    public Document in;
    @DO(name = "OUT", relativeEvents = {"EO"})
    public String out;

    @Algorithm(name = "CONVERT")
    public void create() {
        try {
            out = DocumentUtil.getXMLString(in);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        triggerEvent("EO");
    }
}
