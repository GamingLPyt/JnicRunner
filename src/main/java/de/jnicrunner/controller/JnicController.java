package de.jnicrunner.controller;

import de.jnicrunner.JnicRunner;
import de.jnicrunner.util.FxmlLoader;
import de.jnicrunner.util.JnicProcess;
import de.jnicrunner.util.Replacer;
import de.jnicrunner.util.enums.StageID;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;

public class JnicController {

    private static JnicController jnicController;

    private final JnicRunner jnicRunner_ = JnicRunner.getInstance();
    private final ArrayList<FxmlLoader> fxmlLoader = this.jnicRunner_.getFxmlLoader();
    private ConsoleController consoleController;
    private ErrorController errorController;

    private boolean firstUse;

    @FXML
    private ImageView jnicRunner, settings, arguments, selectedMover;
    @FXML
    private Pane jnicRunnerPane, settingsPane, argumentsPane;
    @FXML
    private Button exitButton, inputChoose, outputChoose, configChoose, process;
    @FXML
    private TextField inputTextField, outputTextField, configTextField, jnicArgsTextField, jnicHelperTextField, javaPathTextField, jnicPathTextField;
    @FXML
    private CheckBox isJnicHelper, console, autoScroll;

    @FXML
    public void initialize() {
        jnicController = this;

        this.javaPathTextField.setText("./libs/openjdk-16.0.1/bin/javaw.exe");
        this.jnicPathTextField.setText("./libs/jnic.jar");
        this.jnicArgsTextField.setText("%java_path% -jar %jnic_path% %input_file% %output_file% %config_file% -debug");
        this.jnicHelperTextField.setText("%java_path% -jar ./libs/JnicHelper.jar %input_file% %config_file% -s -m");

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
                    File file = fileChooser.showOpenDialog(this.fxmlLoader.get(StageID.JNIC_RUNNER.getId()).getStage());

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
                    File file = fileChooser.showOpenDialog(this.fxmlLoader.get(StageID.JNIC_RUNNER.getId()).getStage());

                    if (file != null) {
                        this.outputTextField.setText(file.getAbsolutePath());
                    }

                    break buttons;
                }
            }

            configChoose:
            {
                if (event.getSource() == this.configChoose) {
                    File file;

                    if (this.isJnicHelper.isSelected()) {
                        DirectoryChooser directoryChooser = new DirectoryChooser();
                        directoryChooser.setTitle("Choose your config folder");
                        file = directoryChooser.showDialog(this.fxmlLoader.get(StageID.JNIC_RUNNER.getId()).getStage());
                    } else {
                        FileChooser fileChooser = new FileChooser();
                        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Xml Files (*.xml)", "*.xml");

                        fileChooser.getExtensionFilters().add(extensionFilter);
                        fileChooser.setTitle("Choose your config file");
                        file = fileChooser.showOpenDialog(this.fxmlLoader.get(StageID.JNIC_RUNNER.getId()).getStage());
                    }

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
                        this.fxmlLoader.get(StageID.JNIC_CONSOLE.getId()).getStage().show();
                        this.autoScroll.setDisable(false);
                    } else {
                        this.fxmlLoader.get(StageID.JNIC_CONSOLE.getId()).getStage().hide();
                        this.autoScroll.setDisable(true);
                    }

                    break buttons;
                }
            }

            process:
            {
                if (event.getSource() == this.process) {
                    try {

                        if (!(this.inputTextField.getText().isEmpty() || this.outputTextField.getText().isEmpty() || this.configTextField.getText().isEmpty())) {
                            String args;
                            JnicProcess jnicProcess;

                            /*{
                                if (!this.firstUse) {
                                    jnicProcess = new JnicProcess(new Replacer("%java_path% -jar %jnic_path%", this).replace(), false, this);
                                    jnicProcess.exec();
                                    jnicProcess.isReady();

                                    if (jnicProcess.getConsoleOutPut().contains("Could not open file jnic.licence")) {
                                        this.fxmlLoader.get(StageID.JNIC_ACTIVATION.getId()).getStage().show();
                                        break buttons;
                                    }
                                }

                                this.firstUse = true;
                            }*/

                            if (this.isJnicHelper.isSelected()) {
                                if (new File(this.configTextField.getText()).isDirectory()) {
                                    args = this.jnicHelperTextField.getText().replace("%output_file%", this.inputTextField.getText().replace(".jar", ".jnicHelper.jar"));
                                    jnicProcess = new JnicProcess(new Replacer(args, this).replace(), true, this);
                                    jnicProcess.exec();

                                    jnicProcess.isReady();
                                    args = this.jnicArgsTextField.getText().replace("%input_file%", this.inputTextField.getText().replace(".jar", ".jnicHelper.jar"));

                                    jnicProcess = new JnicProcess(new Replacer(args, this).replace(), true, this);
                                    jnicProcess.exec();
                                } else {
                                    Stage stage = this.jnicRunner_.getFxmlLoader().get(StageID.JNIC_ERROR.getId()).getStage();
                                    this.getErrorController().setError("You are using JnicHelper\nPlease set the Config-Path\nAs an Folder");
                                    stage.show();
                                }
                            } else {
                                jnicProcess = new JnicProcess(new Replacer(this.jnicArgsTextField.getText(), this).replace(), true, this);
                                jnicProcess.exec();
                            }
                        } else {
                            Stage stage = this.jnicRunner_.getFxmlLoader().get(StageID.JNIC_ERROR.getId()).getStage();
                            this.getErrorController().setError("Input-/Output- or Config-\nText-Field isn't set");
                            stage.show();
                            break buttons;
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

    public void setErrorController(ErrorController errorController) {
        this.errorController = errorController;
    }

    public TextField getInputTextField() {
        return inputTextField;
    }

    public TextField getOutputTextField() {
        return outputTextField;
    }

    public TextField getConfigTextField() {
        return configTextField;
    }

    public TextField getJavaPathTextField() {
        return javaPathTextField;
    }

    public TextField getJnicPathTextField() {
        return jnicPathTextField;
    }

    public CheckBox getIsJnicHelper() {
        return isJnicHelper;
    }

    public ConsoleController getConsoleController() {
        return consoleController;
    }

    public ErrorController getErrorController() {
        return errorController;
    }

    public static JnicController getJnicController() {
        return jnicController;
    }
}
