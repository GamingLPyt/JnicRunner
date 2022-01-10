package de.jnicrunner.util;

import de.jnicrunner.controller.ConsoleController;
import de.jnicrunner.controller.JnicController;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class JnicProcess {

    private final String args;
    private final JnicController jnicController;
    private final ConsoleController consoleController;
    private Process process;

    public JnicProcess(String args, JnicController jnicController) {
        this.args = args;
        this.jnicController = jnicController;
        this.consoleController = this.jnicController.getConsoleController();
    }

    public void exec() throws Exception {
        this.process = Runtime.getRuntime().exec(this.args.split(" "));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            this.consoleController.setText(line);
            this.jnicController.autoScroll();
        }

        while ((line = errorReader.readLine()) != null) {
            this.consoleController.setText(line);
            this.jnicController.autoScroll();
        }
    }

    public boolean isReady() throws Exception {
        return this.process.waitFor() == 0;
    }
}
