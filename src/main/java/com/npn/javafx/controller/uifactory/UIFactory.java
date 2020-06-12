package com.npn.javafx.controller.uifactory;

import com.npn.javafx.controller.uicontroller.*;
import com.npn.javafx.model.MainFormStage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class UIFactory {
    private final ResourceBundle resourceBundle;
    Stage primaryStage;

    public UIFactory(ResourceBundle resourceBundle, Stage primaryStage) {
        this.resourceBundle = resourceBundle;
        this.primaryStage = primaryStage;
    }

    public void initUI() throws Exception {

        //UIMain loading
        FXMLForm rootForm = loadNode(UIMainFormController.getFXMLPath(),resourceBundle);
        primaryStage.setScene(new Scene((Parent)rootForm.getNode()));
        UIMainFormController mainController = (UIMainFormController) rootForm.getController();

        //Header node loading
        FXMLForm headerForm = loadNode(UIHeaderController.getFXMLPath(),resourceBundle);
        UIHeaderController headerController = (UIHeaderController) headerForm.getController();
        headerController.init(headerForm.getNode(),mainController);
        //
        List<FXMLForm> formList = new ArrayList<>();
        for (MainFormStage value : MainFormStage.values()) {
            formList.add(loadFormFromStage(value,resourceBundle,mainController));
        }
        primaryStage.show();
        rootForm.getNode().setVisible(true);
        mainController.init(primaryStage,
                headerController,
                formList.stream().filter(Objects::nonNull).map(x->(UIMainChildAbstractController)x.getController()).
                collect(Collectors.toList()));

    }


    /**
     *
     * @param stage
     * @param resourceBundle
     * @param mainController
     * @return
     * @throws IOException
     */
    public FXMLForm loadFormFromStage(MainFormStage stage, ResourceBundle resourceBundle, UIMainFormController mainController) throws IOException {
        switch (stage) {
            case SELECT_BASE_PATH:
                return loadChildNode(UISelectPathFormController.getFXMLPath(),resourceBundle,mainController);
            case SELECT_FILE:
                return loadChildNode(UISelectFilesFormController.getFXMLPath(),resourceBundle,mainController);
            case DISTR_VIEW:
                return loadChildNode(UIArchiveItemFormController.getFXMLPath(),resourceBundle,mainController);
            case SELECT_DESTINATION_DIR:
                return loadChildNode(UISelectDestinationDirController.getFXMLPath(),resourceBundle,mainController);
            case PACK_DISTR:
                return null; //TODO
            default:
                return null;


        }
    }


    /**
     * Загружает любую FXML формы
     *
     * @param resourcePath путь к форме
     * @param resourceBundle ResourceBundle с локализацией формы
     * @return FXMLForm
     * @throws IOException
     */
    private FXMLForm loadNode(String resourcePath, ResourceBundle resourceBundle) throws IOException {
        URL xmlUrl = UIMainChildAbstractController.class.getResource(resourcePath);
        FXMLLoader loader = new FXMLLoader(xmlUrl,resourceBundle);
        Parent root = loader.load();
        root.setVisible(false);
        return new FXMLForm(root,loader.getController());
    }

    /**
     * Загружает и инициализирует дочерние формы с контроллерами имплементирующими {@link UIMainChildAbstractController}
     *
     * @param resourcePath
     * @param resourceBundle
     * @param mainController
     * @return
     * @throws IOException
     */
    private FXMLForm loadChildNode(String resourcePath, ResourceBundle resourceBundle, UIMainFormController mainController) throws IOException {
        FXMLForm fxmlForm = loadNode(resourcePath, resourceBundle);

        UIMainChildAbstractController controller = (UIMainChildAbstractController) fxmlForm.getController();
        controller.init(fxmlForm.node,mainController);
        return fxmlForm;
    }

    private class FXMLForm {
        private final Node node;
        private final Object controller;

        public FXMLForm(final Node node,final Object controller) {
            this.node = node;
            this.controller = controller;
        }

        public Node getNode() {
            return node;
        }

        public Object getController() {
            return controller;
        }
    }

}
