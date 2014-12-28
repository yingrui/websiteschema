/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.analyzer.browser.listener;

import com.webrenderer.swing.IBrowserCanvas;
import com.webrenderer.swing.dom.IDocument;
import com.webrenderer.swing.event.NetworkEvent;
import com.webrenderer.swing.event.NetworkListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import org.apache.log4j.Logger;
import websiteschema.analyzer.context.BrowserContext;

/**
 *
 * @author ray
 */
public class SimpleNetworkListener {

    Logger l = Logger.getRootLogger();
    BrowserContext context;
    IBrowserCanvas browser;
    javax.swing.JTextField addressTextField;
    int sec = 1000;
    long delay = 30 * sec;
    boolean started = false;
    Timer timer = null;
    javax.swing.JProgressBar progress;

    public SimpleNetworkListener(BrowserContext context) {
        this.context = context;
        this.browser = context.getBrowser();
    }

    public JTextField getAddressTextField() {
        return addressTextField;
    }

    public void setAddressTextField(JTextField addressTextField) {
        this.addressTextField = addressTextField;
    }

    public void onProgressChange(NetworkEvent ne) {
        l.debug("onProgressChange" + ((float) ne.getCurrentProgress() / (float) ne.getMaximumProgress()));
        float f = ((float) ne.getCurrentProgress() / (float) ne.getMaximumProgress());
        progress.setValue((int) (progress.getMaximum() * f));
    }

    public void onDocumentLoad(NetworkEvent ne) {
        l.debug("onDocumentLoad ");
        started = false;
        timer = new Timer();
        timer.schedule(new MyTimerTask(), delay);
        progress.setVisible(true);
        progress.setValue(0);
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            if (couldProcess()) {
                process();
            }
        }
    }

    public synchronized boolean couldProcess() {
        if (!started) {
            started = true;
            return started;
        } else {
            return false;
        }
    }

    private void process() {
        context.getConsole().log("Referrer: " + context.getBrowser().getDocument().getReferrer());
//        VIPSImpl vips = new VIPSImpl(context);
//        vips.segment(context.getBrowser().getDocument());
    }

    public void onDocumentComplete(NetworkEvent ne) {
        l.debug("onDocumentComplete " + ne.getURL());
        try {
            context.setReference(ne.getURL());
            progress.setValue(progress.getMaximum());
            progress.setVisible(false);
            addressTextField.setText(ne.getURL());
            String title = null != context.getBrowser().getDocument() ? context.getBrowser().getDocument().getTitle() : "";
            context.getConsole().log("title: " + title);
            context.getSimpleBrowser().getAnalyzerFrame().setTitle(title);
            // 更新页面信息
            context.getSimpleBrowser().getPageInfoPanel().update();

            // 显示源代码
            IDocument doc = context.getBrowser().getDocument();
            if (null != doc) {
                context.getSimpleBrowser().setSource(doc.getBody().getParentElement().getInnerHTML());
            }

            timer.cancel();
            timer = null;
            if (couldProcess()) {
                process();
            }
        } catch (Exception ex) {
        }
    }

    public void onHTTPResponse(NetworkEvent ne) {
//        l.debug("onHTTPResponse\n" + ne.getResponseHeaders());
        l.debug("onHTTPResponse\n" + ne.getURL() + " : " + ne.getStatus() + " : " + ne.getStatusText());
        l.debug("onHTTPResponse\n" + ne.getFrame());
        context.addResponseHeader(ne.getURL(), ne.getResponseHeaders());
    }
}
