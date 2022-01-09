package de.jnicrunner.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ConsoleController {

    private static ConsoleController consoleController;
    private final JnicController jnicController = JnicController.getJnicController();

    @FXML
    private TextArea console;

    @FXML
    public void initialize() {
        consoleController = this;

        this.jnicController.setConsoleController(this);

        this.setText("JnicRunner initialized");
    }

    public void setText(String text) {
        String now = this.console.getText().isEmpty() ? "" : this.console.getText() + "\n";
        now += text;
        this.console.setText(now);
    }

    public TextArea getConsole() {
        return console;
    }

    public static ConsoleController getConsoleController() {
        return consoleController;
    }
}
