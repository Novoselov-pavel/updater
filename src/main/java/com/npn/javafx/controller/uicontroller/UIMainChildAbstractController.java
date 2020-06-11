package com.npn.javafx.controller.uicontroller;

import com.npn.javafx.model.MainFormStage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Абстрактный класс шаблон для реализации частей главного окна
 */
public abstract class UIMainChildAbstractController {
    UIMainFormController mainController;
    ResourceBundle resourceBundle;
    Node currentNode;
    MainFormStage stage = MainFormStage.SELECT_BASE_PATH;
    ResourceBundle textResource = ResourceBundle.getBundle("ui.uimainlocale",
            Locale.getDefault());

    UIMainChildAbstractController() {

    }

    /**
     *  TODO переделать - вынести загрузку в UIFactory переделать инициализацию.
     * @param resourcePath
     * @param mainController
     * @param resourceBundle
     * @return
     * @throws IOException
     */
    public UIMainChildAbstractController loadNode(String resourcePath, UIMainFormController mainController, ResourceBundle resourceBundle) throws IOException {
        URL xmlUrl = UIMainChildAbstractController.class.getResource(resourcePath);
        FXMLLoader loader = new FXMLLoader(xmlUrl,resourceBundle);
        UIMainChildAbstractController controller = loader.getController();
        Parent root = loader.load();
        root.setVisible(false);
        controller.currentNode = root;
        controller.mainController = mainController;
        controller.resourceBundle = resourceBundle;
        return controller;
    }

    /**
     * Устанавливает стадию главного окна и обновляет элемент
     *
     * @param stage стадия окна
     * @return Node
     */
    public Node setStage(MainFormStage stage) {
        this.stage = stage;
        update();
        return currentNode;
    }

    /**
     * Получает элемент
     *
     * @return Node
     */
    public Node getNode() {
        return currentNode;
    }

    /**
     * Обновляет элемент в соотвествии со стадией программы
     */
    public abstract void update();
}
