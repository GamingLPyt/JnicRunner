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
    private final boolean writeToConsole;

    private String consoleOutPut;

    public JnicProcess(String args, boolean writeToConsole, JnicController jnicController) {
        this.args = args;
        this.writeToConsole = writeToConsole;
        this.jnicController = jnicController;
        this.consoleController = this.jnicController.getConsoleController();
    }

    public void exec() throws Exception {
        this.process = Runtime.getRuntime().exec(this.args.split(" "));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(this.process.getErrorStream()));
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            this.consoleOutPut = this.consoleOutPut == null ? line : this.consoleOutPut + "\n" + line;

            if (this.writeToConsole) {
                this.consoleController.setText(line);
                this.jnicController.autoScroll();
            }
        }

        while ((line = errorReader.readLine()) != null) {
            this.consoleOutPut = this.consoleOutPut == null ? line : this.consoleOutPut + "\n" + line;

            if (this.writeToConsole) {
                this.consoleController.setText(line);
                this.jnicController.autoScroll();
            }
        }
    }

    public String getConsoleOutPut() {
        return consoleOutPut;
    }

    public void isReady() throws Exception {
        this.process.waitFor();
    }
}
