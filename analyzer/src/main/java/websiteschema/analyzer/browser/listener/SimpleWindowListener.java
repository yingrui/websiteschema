/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.analyzer.browser.listener;

import com.webrenderer.swing.IBrowserCanvas;
import com.webrenderer.swing.event.WindowAdapter;
import com.webrenderer.swing.event.WindowEvent;
import com.webrenderer.swing.event.WindowListener;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import org.apache.log4j.Logger;
import websiteschema.analyzer.context.BrowserContext;

/**
 *
 * @author ray
 */
public class SimpleWindowListener implements WindowListener {

    Logger l = Logger.getRootLogger();
    BrowserContext context;

    public SimpleWindowListener(BrowserContext context) {
        this.context = context;
    }

    @Override
    public void onNewWindow(WindowEvent we) {
        l.debug("onNewWindow");
        final JFrame popup = new JFrame("Popup");
        popup.setSize(800, 600);
        IBrowserCanvas browserPopup = we.getPopupBrowser();
        browserPopup.addWindowListener(new WindowAdapter() {

            @Override
            public void onWindowResize(WindowEvent e) {
                popup.setSize(e.getWidth(), e.getHeight());
                popup.validate();
                popup.repaint();
            }
        });

        popup.getContentPane().add(BorderLayout.CENTER, browserPopup.getComponent());
        popup.setVisible(true);

    }

    @Override
    public void onWindowDestroy(WindowEvent we) {
        l.debug("onWindowDestroy");
    }

    @Override
    public void onWindowResize(WindowEvent we) {
        l.debug("onWindowResize");
    }
}
