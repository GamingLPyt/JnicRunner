package de.jnicrunner.controller;

import de.jnicrunner.JnicRunner;
import de.jnicrunner.util.JnicProcess;
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

import java.io.File;

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
    private CheckBox isJnicHelper, console, autoScroll;

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

                    break buttons;
                }
            }

            isConsole:
            {
                if (event.getSource() == this.console) {
                    if (this.console.isSelected()) {
                        this.jnicRunner_.getConsoleStage().show();
                        this.autoScroll.setDisable(false);
                    } else {
                        this.jnicRunner_.getConsoleStage().hide();
                        this.autoScroll.setDisable(true);
                    }

                    break buttons;
                }
            }

            process:
            {
                if (event.getSource() == this.process) {
                    try {
                        String args;
                        JnicProcess jnicProcess;

                        if (this.isJnicHelper.isSelected()) {
                            args = this.jnicHelperTextField.getText()
                                    .replace("%input_file%", this.inputTextField.getText())
                                    .replace("%output_file%", this.inputTextField.getText().replace(".jar", ".jnicHelper.jar"));
                            jnicProcess = new JnicProcess(args, this);
                            jnicProcess.exec();

                            if (jnicProcess.isReady()) {
                                args = this.jnicArgsTextField.getText()
                                        .replace("%input_file%", this.inputTextField.getText().replace(".jar", ".jnicHelper.jar"))
                                        .replace("%output_file%", this.outputTextField.getText())
                                        .replace("%config_file%", this.configTextField.getText());

                                jnicProcess = new JnicProcess(args, this);
                                jnicProcess.exec();
                            }
                        } else {
                            args = this.jnicArgsTextField.getText()
                                    .replace("%input_file%", this.inputTextField.getText())
                                    .replace("%output_file%", this.outputTextField.getText())
                                    .replace("%config_file%", this.configTextField.getText());

                            jnicProcess = new JnicProcess(args, this);
                            jnicProcess.exec();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break buttons;
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

    public ConsoleController getConsoleController() {
        return consoleController;
    }

    public static JnicController getJnicController() {
        return jnicController;
    }
}
