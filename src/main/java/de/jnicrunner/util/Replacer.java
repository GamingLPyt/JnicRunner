package de.jnicrunner.util;

import de.jnicrunner.controller.JnicController;

public class Replacer {

    private String text;
    private static JnicController jnicController;

    public Replacer(String text, JnicController jnicController) {
        this.text = text;
        Replacer.jnicController = jnicController;
    }

    public String replace() {
        String[] splitter = this.text.split(" ");
        String out = null;

        for(String split : splitter) {
            try {
                Placeholders placeholder = null;
                for(Placeholders placeholders : Placeholders.values()) {
                    if(placeholders.getPlaceholder().equals(split)) placeholder = placeholders;
                }

                if(placeholder != null) {
                    out += split.replace(placeholder.getPlaceholder(), placeholder.getReplace()) + " ";
                } else {
                    out += split + " ";
                }
            } catch (Exception e) {}
        }

        out = out.substring(4);
        out = out.substring(0, out.length() - 1);
        return out;
    }

    private enum Placeholders {

        JAVA_PATH("%java_path%", jnicController.getJavaPathTextField().getText()),
        JNIC_PATH("%jnic_path%", jnicController.getJnicPathTextField().getText()),
        INPUT_FILE("%input_file%", jnicController.getInputTextField().getText()),
        OUTPUT_FILE("%output_file%", jnicController.getOutputTextField().getText()),
        CONFIG_FILE("%config_file%", (jnicController.getConfigTextField().getText() + (jnicController.getIsJnicHelper().isSelected() ?  "/config.xml" : "")));

        private String placeholder;
        private String replace;

        Placeholders(String placeholder, String replace) {
            this.placeholder = placeholder;
            this.replace = replace;
        }

        public String getPlaceholder() {
            return placeholder;
        }

        public String getReplace() {
            return replace;
        }
    }
}
