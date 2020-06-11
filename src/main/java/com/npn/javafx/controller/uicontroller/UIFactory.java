package com.npn.javafx.controller.uicontroller;

import javafx.stage.Stage;


import java.util.ResourceBundle;

public class UIFactory {
    private final ResourceBundle resourceBundle;
    Stage primaryStage;

    public UIFactory(ResourceBundle resourceBundle, Stage primaryStage) {
        this.resourceBundle = resourceBundle;
        this.primaryStage = primaryStage;
    }

    public void initUI() throws Exception {
        UIMainFormController controller = new UIMainFormController();

        UIHeaderController headerController = new UIHeaderController(controller,resourceBundle);
        UISelectPathFormController selectPathFormController = new UISelectPathFormController(controller,resourceBundle);
        UISelectFilesFormController selectFilesFormController = new UISelectFilesFormController(controller,resourceBundle);
        UICheckDataFormController checkDataFormController = new UICheckDataFormController(controller,resourceBundle);
        UIArchiveItemFormController archiveItemFormController = new UIArchiveItemFormController(controller,resourceBundle);

        controller.init(primaryStage,
                headerController,
                selectPathFormController,
                selectFilesFormController,
                checkDataFormController,
                archiveItemFormController);

        primaryStage.show();

    }



}
