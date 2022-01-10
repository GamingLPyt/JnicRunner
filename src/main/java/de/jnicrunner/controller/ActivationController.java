package de.jnicrunner.controller;

import de.jnicrunner.util.JnicProcess;
import de.jnicrunner.util.Replacer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ActivationController {

    private final JnicController jnicController = JnicController.getJnicController();

    @FXML
    TextField activationTextField;
    @FXML
    Button activate;

    public void onButtonClick(ActionEvent event) {
        try {
            if (event.getSource() == activate) {
                if (!this.activationTextField.getText().isEmpty()) {
                    JnicProcess jnicProcess = new JnicProcess(new Replacer("%java_path% -jar %jnic_path% activate " + this.activationTextField.getText(), this.jnicController).replace(), false, this.jnicController);
                    jnicProcess.exec();
                    jnicProcess.isReady();

                    this.jnicController.getActivationStage().hide();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
