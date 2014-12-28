/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.analyzer.browser.utils;

/**
 *
 * @author ray
 */
public class TextAreaSearch {

    private javax.swing.JTextArea sourceArea;
    boolean caseSensitive = true;

    public TextAreaSearch(javax.swing.JTextArea area) {
        sourceArea = area;
    }

    public TextAreaSearch(javax.swing.JTextArea area, boolean caseSensitive) {
        sourceArea = area;
        this.caseSensitive = caseSensitive;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public String getSource() {
        if (caseSensitive) {
            return this.sourceArea.getText();
        } else {
            return this.sourceArea.getText().toUpperCase();
        }
    }

    public void last(String target) {
        // TODO add your handling code here:
        String source = getSource();
        if (!caseSensitive) {
            target = target.toUpperCase();
        }
        int pos = sourceArea.getCaretPosition();
        if (null != source) {
            int at = source.lastIndexOf(target, pos > 0 ? pos - 1 : source.length() - 1);
            if (at >= 0) {
                sourceArea.setCaretPosition(at);

            } else {
                at = source.lastIndexOf(target, source.length() - 1);
                if (at >= 0) {
                    sourceArea.setCaretPosition(at);
                }
            }
        }
    }

    public void next(String target) {
        // TODO add your handling code here:
        String source = getSource();
        if (!caseSensitive) {
            target = target.toUpperCase();
        }
        int pos = sourceArea.getCaretPosition();
        if (null != source) {
            int at = source.indexOf(target, pos + 1);
            if (at >= 0) {
                sourceArea.setCaretPosition(at);
            } else {
                at = source.indexOf(target, 0);
                if (at >= 0) {
                    sourceArea.setCaretPosition(at);
                }
            }
        }
    }
}
