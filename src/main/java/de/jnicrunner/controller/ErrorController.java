package de.jnicrunner.controller;

import de.jnicrunner.JnicRunner;
import de.jnicrunner.util.enums.StageID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ErrorController {

    private static ErrorController errorController;
    private final JnicRunner jnicRunner = JnicRunner.getInstance();
    private final JnicController jnicController = JnicController.getJnicController();

    @FXML
    private Button errorButton;
    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        errorController = this;

        this.jnicController.setErrorController(this);
    }

    public void onButtonClick(ActionEvent event) {
        if(event.getSource() == this.errorButton) {
            this.jnicRunner.getFxmlLoader().get(StageID.JNIC_ERROR.getId()).getStage().hide();
        }
    }

    public void setError(String error) {
        this.errorLabel.setText(error);
    }
}
