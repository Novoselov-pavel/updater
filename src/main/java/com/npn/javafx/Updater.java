package com.npn.javafx;

import com.npn.javafx.controller.uicontroller.BashController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Updater extends Application {

    public static void main(String[] args) {
        launch(args);
        if (args!=null && args.length!=0) {
            BashController controller = new BashController(args);
            try {
                controller.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void start(Stage primaryStage) {

    }
}
