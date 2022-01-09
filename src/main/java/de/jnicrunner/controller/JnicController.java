package de.jnicrunner.controller;

import de.jnicrunner.JnicRunner;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class JnicController {

    private static JnicController jnicController;
    private final JnicRunner jnicRunner_ = JnicRunner.getInstance();
    private ConsoleController consoleController;

    @FXML
    private ImageView jnicRunner, settings, arguments, selectedMover;
    @FXML
    private Pane jnicRunnerPane, settingsPane, argumentsPane;
    @FXML
    private Button exitButton, inputChoose, outputChoose, configChoose, process;
    @FXML
    private TextField inputTextField, outputTextField, configTextField, jnicArgsTextField, jnicHelperTextField;
    @FXML
    private CheckBox isJnicHelper, autoScroll;

    @FXML
    public void initialize() {
        jnicController = this;

        this.jnicArgsTextField.setText("java -jar jnic.jar %input_file% %output_file% %config_file% -debug");
        this.jnicHelperTextField.setText("java -jar JnicHelper.jar %input_file% -s");

        this.jnicHelperTextField.setDisable(true);
    }

    public void onMouseClick(MouseEvent mouseEvent) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(100), selectedMover);

        hover:
        {
            if (!this.jnicRunnerPane.isVisible() && (mouseEvent.getSource() == this.jnicRunner)) {
                this.settingsPane.setVisible(false);
                this.argumentsPane.setVisible(false);
                this.jnicRunnerPane.setVisible(true);

                translateTransition.setToX(0);
                translateTransition.play();

                break hover;
            }

            if (!this.settingsPane.isVisible() && (mouseEvent.getSource() == this.settings)) {
                this.argumentsPane.setVisible(false);
                this.jnicRunnerPane.setVisible(false);
                this.settingsPane.setVisible(true);

                translateTransition.setToX(82);
                translateTransition.play();

                break hover;
            }

            if (!this.argumentsPane.isVisible() && (mouseEvent.getSource() == this.arguments)) {
                this.jnicRunnerPane.setVisible(false);
                this.settingsPane.setVisible(false);
                this.argumentsPane.setVisible(true);

                translateTransition.setToX(162);
                translateTransition.play();

                break hover;
            }
        }
    }

    public void onButtonClick(ActionEvent event) {
        buttons:
        {
            exit:
            {
                if (event.getSource() == exitButton) {
                    System.exit(0);
                    break buttons;
                }
            }

            inputChoose:
            {
                if (event.getSource() == this.inputChoose) {
                    FileChooser fileChooser = new FileChooser();
                    FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Jar Files (*.jar)", "*.jar");

                    fileChooser.getExtensionFilters().add(extensionFilter);
                    fileChooser.setTitle("Choose your input file");
                    File file = fileChooser.showOpenDialog(this.jnicRunner_.getStage());

                    if (file != null) {
                        this.inputTextField.setText(file.getAbsolutePath());
                    }

                    break buttons;
                }
            }

            outputChoose:
            {
                if (event.getSource() == this.outputChoose) {
                    FileChooser fileChooser = new FileChooser();
                    FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Jar Files (*.jar)", "*.jar");

                    fileChooser.getExtensionFilters().add(extensionFilter);
                    fileChooser.setTitle("Choose your output file");
                    File file = fileChooser.showOpenDialog(this.jnicRunner_.getStage());

                    if (file != null) {
                        this.outputTextField.setText(file.getAbsolutePath());
                    }

                    break buttons;
                }
            }

            configChoose:
            {
                if (event.getSource() == this.configChoose) {
                    FileChooser fileChooser = new FileChooser();
                    FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Xml Files (*.xml)", "*.xml");

                    fileChooser.getExtensionFilters().add(extensionFilter);
                    fileChooser.setTitle("Choose your config file");
                    File file = fileChooser.showOpenDialog(this.jnicRunner_.getStage());

                    if (file != null) {
                        this.configTextField.setText(file.getAbsolutePath());
                    }

                    break buttons;
                }
            }

            isJnicHelper:
            {
                if (event.getSource() == this.isJnicHelper) {
                    if (this.isJnicHelper.isSelected()) {
                        this.jnicHelperTextField.setDisable(false);
                    } else {
                        this.jnicHelperTextField.setDisable(true);
                    }
                }
            }

            process:
            {
                if (event.getSource() == this.process) {
                    Runtime runtime = Runtime.getRuntime();

                    String args;
                    Process process;
                    BufferedReader inputStream;
                    BufferedReader stdError;
                    String line;

                    try {
                        if (this.isJnicHelper.isSelected()) {
                            args = this.jnicHelperTextField.getText().replace("%input_file%", this.inputTextField.getText());
                            process = runtime.exec(args.split(" "));
                            inputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                            while ((line = inputStream.readLine()) != null) {
                                this.consoleController.setText(line);
                                this.autoScroll();
                            }

                            while ((line = stdError.readLine()) != null) {
                                this.consoleController.setText(line);
                                this.autoScroll();
                            }
                        }

                        args = this.jnicArgsTextField.getText()
                                .replace("%input_file%", this.inputTextField.getText())
                                .replace("%output_file%", this.outputTextField.getText())
                                .replace("%config_file%", this.configTextField.getText());
                        process = runtime.exec(args.split(" "));
                        inputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                        while ((line = inputStream.readLine()) != null) {
                            this.consoleController.setText(line);
                            this.autoScroll();
                        }

                        while ((line = stdError.readLine()) != null) {
                            this.consoleController.setText(line);
                            this.autoScroll();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void autoScroll() {
        if (this.autoScroll.isSelected())
            if (!this.consoleController.getConsole().isFocused())
                this.consoleController.getConsole().setScrollTop(Double.MAX_VALUE);
    }

    public void setConsoleController(ConsoleController consoleController) {
        this.consoleController = consoleController;
    }

    public static JnicController getJnicController() {
        return jnicController;
    }
}
