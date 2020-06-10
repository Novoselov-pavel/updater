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
    final UIMainFormController mainController;
    ResourceBundle resourceBundle;
    Node currentNode;
    MainFormStage stage = MainFormStage.SELECT_BASE_PATH;
    ResourceBundle textResource = ResourceBundle.getBundle("ui.uimainlocale",
            Locale.getDefault());

    public UIMainChildAbstractController(UIMainFormController mainController, ResourceBundle resourceBundle) throws IOException {
        this.mainController = mainController;
        this.resourceBundle = resourceBundle;
        currentNode = loadNode(getFXMLPath());
        setStage(stage);
    }

    /**
     * Возвращает путь к файлу FXML для закрузки элемента
     *
     * @return путь к файлу FXML
     */
    public abstract String getFXMLPath();

    Node loadNode(String resourcePath) throws IOException {
        URL xmlUrl = getClass().getResource(resourcePath);
        FXMLLoader loader = new FXMLLoader(xmlUrl,resourceBundle);
        Parent root = loader.load();
        return root;
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
     * Обновляет элемент в соотвествией со стадией программы
     */
    public abstract void update();
}
