package de.jnicrunner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class JnicRunner extends Application {

    private static JnicRunner instance;

    private Stage stage;
    private Stage consoleStage;
    private double xOffset, yOffset;
    private double xOffsetConsole, yOffsetConsole;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        instance = this;

        Parent parent = FXMLLoader.load(this.getClass().getResource("javafx/JnicRunner.fxml"));

        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("icons/logo.png")));
        stage.setTitle("JnicRunner | Developed By GamingLPyt");
        stage.initStyle(StageStyle.TRANSPARENT);

        parent.setOnMousePressed(mouseEvent -> {
            this.xOffset = mouseEvent.getSceneX();
            this.yOffset = mouseEvent.getSceneY();
        });

        parent.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - this.xOffset);
            stage.setY(mouseEvent.getScreenY() - this.yOffset);
        });

        Scene scene = new Scene(parent);

        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
        this.stage = stage;



        Parent console = FXMLLoader.load(this.getClass().getResource("javafx/Console.fxml"));
        Stage consoleStage = new Stage();
        consoleStage.getIcons().add(new Image(this.getClass().getResourceAsStream("icons/logo.png")));
        consoleStage.setTitle("JnicRunner | Console");

        console.setOnMousePressed(mouseEvent -> {
            this.xOffsetConsole = mouseEvent.getSceneX();
            this.yOffsetConsole = mouseEvent.getSceneY();
        });

        console.setOnMouseDragged(mouseEvent -> {
            consoleStage.setX(mouseEvent.getScreenX() - this.xOffsetConsole);
            consoleStage.setY(mouseEvent.getScreenY() - this.yOffsetConsole);
        });

        Scene consoleScene = new Scene(console);

        consoleStage.initStyle(StageStyle.TRANSPARENT);
        consoleScene.setFill(Color.TRANSPARENT);
        consoleStage.setScene(consoleScene);
        consoleStage.setX(stage.getX() + 280);
        consoleStage.setY(stage.getY());
        consoleStage.show();
        this.consoleStage = consoleStage;
    }

    public Stage getStage() {
        return stage;
    }

    public Stage getConsoleStage() {
        return consoleStage;
    }

    public static JnicRunner getInstance() {
        return instance;
    }
}
