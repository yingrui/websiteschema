/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.utils;

import javax.swing.JTextArea;

/**
 *
 * @author ray
 */
public class AWTConsole implements Console {

    JTextArea textArea;

    public AWTConsole(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void log(String info) {
        if (textArea.getLineCount() > 1000) {
            textArea.setText("");
        }
        textArea.append(info);
        textArea.append("\n");
        textArea.setCaretPosition(textArea.getText().length() - 1);
    }

    public void print(String info) {
        textArea.append(info);
    }

    public void println() {
        textArea.append("\n");
    }

    public void println(String info) {
        log(info);
    }
}
