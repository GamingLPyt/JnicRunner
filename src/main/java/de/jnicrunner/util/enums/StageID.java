package de.jnicrunner.util.enums;

public enum StageID {

    JNIC_RUNNER(0, "JnicRunner.fxml", "JnicRunner | Developed By GamingLPyt", 0, 0),
    JNIC_CONSOLE(1, "Console.fxml", "JnicRunner | Console", 280, 0),
    JNIC_ACTIVATION(2, "ActivateJnic.fxml", "JnicRunner | Activation", -320, 150),
    JNIC_ERROR(3, "Error.fxml", "JnicRunner | Error", 0, 0);

    private int id;
    private String fxml, title;
    private int xPos, yPos;

    StageID(int id, String fxml, String title, int xPos, int yPos) {
        this.id = id;
        this.fxml = fxml;
        this.title = title;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public int getId() {
        return id;
    }

    public String getFxml() {
        return fxml;
    }

    public String getTitle() {
        return title;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
