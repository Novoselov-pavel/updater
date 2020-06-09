package com.npn.javafx.controller.uicontroller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.net.URL;
import java.util.ResourceBundle;

public class UIFactory {
    private ResourceBundle resourceBundle;
    Stage primaryStage;
    UIHeaderController headerController;

    public UIFactory(ResourceBundle resourceBundle, Stage primaryStage) {
        this.resourceBundle = resourceBundle;
        this.primaryStage = primaryStage;
    }

    public void initUI() throws Exception {
        URL xmlUrl = getClass().getResource("/ui/UIMainForm.fxml");
        FXMLLoader loader = new FXMLLoader(xmlUrl, resourceBundle);

        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        UIMainFormController controller = loader.getController();
        controller.init(primaryStage);
        primaryStage.show();

        UIHeaderController headerController = new UIHeaderController(primaryStage,resourceBundle);
        this.headerController = headerController;
    }



}
