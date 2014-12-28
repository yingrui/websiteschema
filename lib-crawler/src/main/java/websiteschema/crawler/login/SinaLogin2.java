/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.login;

import com.webrenderer.swing.IMozillaBrowserCanvas;
import java.awt.BorderLayout;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import websiteschema.crawler.browser.BrowserWebCrawler;
import websiteschema.crawler.browser.MyBrowserFactory;

/**
 *
 * @author ray
 */
public class SinaLogin2 {

    String username = "websiteschema@gmail.com";
    String password = "websiteschema";
    IMozillaBrowserCanvas browser = null;
    JFrame frame;

    public SinaLogin2() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(content());
        frame.setSize(1024, 600);
        frame.setVisible(true);
    }

    private JPanel content() {
        JPanel panel = new JPanel(new BorderLayout());
        //Core function to create browser
        browser = MyBrowserFactory.getInstance().getBrowser();
        panel.add(BorderLayout.CENTER, browser.getComponent());
        return panel;
    }

    public void login() {
        browser.loadURL("http://weibo.com/");
        browser.getDocument().getForms();
    }


    public static void main(String args[]) throws IOException {
        SinaLogin2 tool = new SinaLogin2();
        tool.login();
    }
}
