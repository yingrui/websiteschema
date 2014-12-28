/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.analyzer.browser;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.application.Platform;

/**
 * @author ray
 */
public final class TestBrowser {

    JFXPanel jfxPanel = new JFXPanel();
    private WebEngine engine;
    JTextField textfield;

    public TestBrowser() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(content());
        frame.setSize(750, 450);
        frame.setVisible(true);
    }

    public void load(String url) {
        engine.load(url);
    }

    public void createScene() {
        Platform.runLater(new Runnable() {
            public void run() {
                WebView view = new WebView();
                engine = view.getEngine();
                jfxPanel.setScene(new Scene(view));
            }
        });
    }

    public JPanel content() {
        JPanel panel = new JPanel(new BorderLayout());

        textfield = new JTextField();

        textfield.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    public void run(){
                    engine.load(textfield.getText());}
                });
            }
        });

        panel.add(BorderLayout.NORTH, textfield);
        panel.add(BorderLayout.CENTER, jfxPanel);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                TestBrowser browser = new TestBrowser();
                browser.createScene();
                browser.load("http://www.baidu.com");
            }
        });
    }
}
