package de.jnicrunner.controller;

import de.jnicrunner.JnicRunner;
import de.jnicrunner.util.JnicProcess;
import de.jnicrunner.util.Replacer;
import de.jnicrunner.util.enums.StageID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ActivationController {

    private final JnicController jnicController = JnicController.getJnicController();
    private final JnicRunner jnicRunner = JnicRunner.getInstance();

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

                    if (jnicProcess.getConsoleOutPut().contains("Activation failed: could not connect to licencing server")) {
                        Stage stage = this.jnicRunner.getFxmlLoader().get(StageID.JNIC_ERROR.getId()).getStage();
                        this.jnicController.getErrorController().setError("Invalid License\nPlease try again");
                        stage.show();
                    } else {
                        this.jnicRunner.getFxmlLoader().get(StageID.JNIC_ACTIVATION.getId()).getStage().hide();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
