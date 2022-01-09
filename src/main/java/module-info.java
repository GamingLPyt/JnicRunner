module de.jnicrunner {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens de.jnicrunner to javafx.fxml;
    opens de.jnicrunner.controller to javafx.fxml;
    exports de.jnicrunner;
    exports de.jnicrunner.controller;
}