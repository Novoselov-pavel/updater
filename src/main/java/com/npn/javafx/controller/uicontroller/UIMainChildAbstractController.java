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
    Node currentNode;
    MainFormStage stage = MainFormStage.SELECT_BASE_PATH;
    ResourceBundle textResource = ResourceBundle.getBundle("ui.uimainlocale",
            Locale.getDefault());

    public UIMainChildAbstractController() {

    }

    /**
     * Инициализация переменных и объектов контроллера
     *
     * @param currentNode
     * @param mainController
     * @throws IOException
     */
    public void init(Node currentNode, UIMainFormController mainController) throws IOException {
        this.mainController = mainController;
        this.currentNode = currentNode;
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

    /**
     * Выводит правильные ли данные на форме
     *
     * @return
     */
    public abstract boolean isValid();

    /**
     * Возвращает стадию к которой относится данная форма
     *
     * @return stage
     */
    public abstract MainFormStage getFormStage();
}
