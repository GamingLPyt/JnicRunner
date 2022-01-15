package de.jnicrunner.util;

import de.jnicrunner.JnicRunner;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FxmlLoader {

    private int id;
    private String fxml, title;
    private Stage stage, otherStage;
    private double xOffset, yOffset;
    private int xPos, yPos;

    public FxmlLoader(int id, String fxml, String title, Stage otherStage, int xPos, int yPos) {
        this.id = id;
        this.fxml = fxml;
        this.title = title;
        this.otherStage = otherStage;
        this.xPos = xPos;
        this.yPos = yPos;

        this.stage = new Stage();
    }

    public FxmlLoader load(boolean show) throws Exception {
        Platform.setImplicitExit(false);

        Parent parent = FXMLLoader.load(JnicRunner.class.getResource("javafx/" + this.fxml));

        this.stage.getIcons().add(new Image(JnicRunner.class.getResourceAsStream("icons/logo.png")));
        this.stage.setTitle(this.title);
        this.stage.initStyle(StageStyle.TRANSPARENT);

        parent.setOnMousePressed(mouseEvent -> {
            this.xOffset = mouseEvent.getSceneX();
            this.yOffset = mouseEvent.getSceneY();
        });

        parent.setOnMouseDragged(mouseEvent -> {
            this.stage.setX(mouseEvent.getScreenX() - this.xOffset);
            this.stage.setY(mouseEvent.getScreenY() - this.yOffset);
        });

        Scene scene = new Scene(parent);

        scene.setFill(Color.TRANSPARENT);
        this.stage.setScene(scene);

        if (this.otherStage != null && (this.xPos != 0 || this.yPos != 0)) {
            this.stage.setX(this.otherStage.getX() + this.xPos);
            this.stage.setY(this.otherStage.getY() + this.yPos);
        }

        if (show)
            this.stage.show();
        return this;
    }

    public int getId() {
        return id;
    }

    public Stage getStage() {
        return stage;
    }
}
