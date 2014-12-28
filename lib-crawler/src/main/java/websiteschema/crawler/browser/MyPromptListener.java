/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.crawler.browser;

import com.webrenderer.swing.event.PromptEvent;
import com.webrenderer.swing.event.PromptListener;
import org.apache.log4j.Logger;

/**
 *
 * @author ray
 */
public class MyPromptListener implements PromptListener {

    Logger l = Logger.getRootLogger();

    @Override
    public void onPromptDialog(PromptEvent pe) {
        int type = pe.getDialogType();
        String dialogType = "";
        switch (type) {
            case PromptEvent.DIALOG_ALERT:
                dialogType = "DIALOG_ALERT";
                pe.blockPromptDialog();
                l.debug("closed prompt dialog.");
                break;
            case PromptEvent.DIALOG_ALERT_CHECK:
                dialogType = "DIALOG_ALERT_CHECK";
                break;
            case PromptEvent.DIALOG_CONFIRM:
                dialogType = "DIALOG_CONFIRM";
                break;
            case PromptEvent.DIALOG_CONFIRM_CHECK:
                dialogType = "DIALOG_CONFIRM_CHECK";
                break;
            case PromptEvent.DIALOG_CONFIRM_EX:
                dialogType = "DIALOG_CONFIRM_EX";
                break;
            case PromptEvent.DIALOG_PROMPT:
                dialogType = "DIALOG_PROMPT";
                break;
            case PromptEvent.DIALOG_PROMPT_PASSWORD:
                dialogType = "DIALOG_PROMPT_PASSWORD";
                break;
            case PromptEvent.DIALOG_PROMPT_USERNAME_AND_PASSWORD:
                dialogType = "DIALOG_PROMPT_USERNAME_AND_PASSWORD";
                break;
            case PromptEvent.DIALOG_SAVE_TO_DISK:
                dialogType = "DIALOG_SAVE_TO_DISK";
                break;
        }

        l.debug("PromptDialog " + dialogType);
    }
}
