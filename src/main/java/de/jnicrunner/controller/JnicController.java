package de.jnicrunner.controller;

import de.jnicrunner.JnicRunner;
import de.jnicrunner.util.JnicProcess;
import de.jnicrunner.util.Replacer;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;

public class JnicController {

    private static JnicController jnicController;
    private final JnicRunner jnicRunner_ = JnicRunner.getInstance();
    private ConsoleController consoleController;

    private Parent activation = null;
    private Stage activationStage = new Stage();
    private double xOffset, yOffset;
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
        this.jnicHelperTextField.setText("%java_path% -jar ./libs/JnicHelper.jar %input_file% %output_file% -s");

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

                        {
                            if (!this.firstUse) {
                                jnicProcess = new JnicProcess(new Replacer("%java_path% -jar %jnic_path%", this).replace(), false, this);
                                jnicProcess.exec();
                                jnicProcess.isReady();

                                if (jnicProcess.getConsoleOutPut().contains("Could not open file jnic.licence")) {
                                    this.activation = FXMLLoader.load(JnicRunner.class.getResource("javafx/ActivateJnic.fxml"));

                                    this.activationStage.getIcons().add(new Image(JnicRunner.class.getResourceAsStream("icons/logo.png")));
                                    this.activationStage.setTitle("JnicRunner | Activation");
                                    this.activationStage.initStyle(StageStyle.TRANSPARENT);

                                    this.activation.setOnMousePressed(mouseEvent -> {
                                        this.xOffset = mouseEvent.getSceneX();
                                        this.yOffset = mouseEvent.getSceneY();
                                    });

                                    this.activation.setOnMouseDragged(mouseEvent -> {
                                        this.activationStage.setX(mouseEvent.getScreenX() - this.xOffset);
                                        this.activationStage.setY(mouseEvent.getScreenY() - this.yOffset);
                                    });

                                    Scene scene = new Scene(this.activation);

                                    scene.setFill(Color.TRANSPARENT);
                                    this.activationStage.setScene(scene);
                                    this.activationStage.setY(this.jnicRunner_.getStage().getY() + 150);
                                    this.activationStage.setX(this.jnicRunner_.getStage().getX() - 320);
                                    this.activationStage.show();

                                    break buttons;
                                }
                            }

                            this.firstUse = true;
                        }

                        if (this.isJnicHelper.isSelected()) {
                            args = this.jnicHelperTextField.getText().replace("%output_file%", this.inputTextField.getText().replace(".jar", ".jnicHelper.jar"));
                            jnicProcess = new JnicProcess(new Replacer(args, this).replace(), true, this);
                            jnicProcess.exec();

                            jnicProcess.isReady();
                            args = this.jnicArgsTextField.getText()
                                    .replace("%input_file%", this.inputTextField.getText().replace(".jar", ".jnicHelper.jar"));

                            jnicProcess = new JnicProcess(new Replacer(args, this).replace(), true, this);
                            jnicProcess.exec();
                        } else {
                            jnicProcess = new JnicProcess(new Replacer(this.jnicArgsTextField.getText(), this).replace(), true, this);
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

    public Stage getActivationStage() {
        return activationStage;
    }

    public ConsoleController getConsoleController() {
        return consoleController;
    }

    public static JnicController getJnicController() {
        return jnicController;
    }
}
