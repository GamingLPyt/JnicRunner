package de.jnicrunner;

import de.jnicrunner.util.FxmlLoader;
import de.jnicrunner.util.enums.StageID;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;

public class JnicRunner extends Application {

    private static JnicRunner instance;
    private ArrayList<FxmlLoader> fxmlLoader = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        instance = this;

        FxmlLoader fxmlLoader;
        this.fxmlLoader.add(fxmlLoader = new FxmlLoader(StageID.JNIC_RUNNER.getId(), StageID.JNIC_RUNNER.getFxml(), StageID.JNIC_RUNNER.getTitle(), null, StageID.JNIC_RUNNER.getXPos(), StageID.JNIC_RUNNER.getYPos()).load(true));
        this.fxmlLoader.add(new FxmlLoader(StageID.JNIC_CONSOLE.getId(), StageID.JNIC_CONSOLE.getFxml(), StageID.JNIC_CONSOLE.getTitle(), fxmlLoader.getStage(), StageID.JNIC_CONSOLE.getXPos(), StageID.JNIC_CONSOLE.getYPos()).load(true));
        this.fxmlLoader.add(new FxmlLoader(StageID.JNIC_ACTIVATION.getId(), StageID.JNIC_ACTIVATION.getFxml(), StageID.JNIC_ACTIVATION.getTitle(), fxmlLoader.getStage(), StageID.JNIC_ACTIVATION.getXPos(), StageID.JNIC_ACTIVATION.getYPos()).load(false));
        this.fxmlLoader.add(new FxmlLoader(StageID.JNIC_ERROR.getId(), StageID.JNIC_ERROR.getFxml(), StageID.JNIC_ERROR.getTitle(), fxmlLoader.getStage(), StageID.JNIC_ERROR.getXPos(), StageID.JNIC_ERROR.getYPos()).load(false));
    }

    public ArrayList<FxmlLoader> getFxmlLoader() {
        return fxmlLoader;
    }

    public static JnicRunner getInstance() {
        return instance;
    }
}
