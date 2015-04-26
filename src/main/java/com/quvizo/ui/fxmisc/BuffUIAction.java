/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quvizo.ui.fxmisc;

import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;

/**
 *
 * @author bufflogic
 */
public interface BuffUIAction {
    public void perform(TextInputControl[] actionInputs, Stage stageOfDialog);
}
