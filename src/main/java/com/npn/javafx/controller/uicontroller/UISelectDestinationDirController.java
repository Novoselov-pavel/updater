package com.npn.javafx.controller.uicontroller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class UISelectDestinationDirController extends UIMainChildAbstractController {


    @FXML
    private ImageView versionStatus;

    @FXML
    private TextField versionField;

    @FXML
    private Label versionLabel;

    @FXML
    private Button openFolder;

    @FXML
    private TextField folderPathField;

    @FXML
    private ImageView pathStatus;


    /**
     * Возвращает путь к файлу FXML для закрузки элемента
     *
     * @return путь к файлу FXML
     */
    public static String getFXMLPath() {
        return "/ui/UISelectDestinationDir.fxml";
    }



    /**
     * Обновляет элемент в соотвествии со стадией программы
     */
    @Override
    public void update() {
        ///TODO
    }

    /**
     * Выводит правильные ли данные на форме
     *
     * @return
     */
    @Override
    public boolean isValid() {
        return false; ///TODO
    }
}