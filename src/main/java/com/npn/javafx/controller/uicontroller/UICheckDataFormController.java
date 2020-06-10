package com.npn.javafx.controller.uicontroller;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Контроллер для UICheckDataForm.fxml
 */
public class UICheckDataFormController extends UIMainChildAbstractController {

    public UICheckDataFormController(UIMainFormController mainController, ResourceBundle resourceBundle) throws IOException {
        super(mainController, resourceBundle);
    }

    /**
     * Возвращает путь к файлу FXML для закрузки элемента
     *
     * @return путь к файлу FXML
     */
    @Override
    public String getFXMLPath() {
        return "ui/UICheckDataForm.fxml";
    }

    /**
     * Обновляет элемент в соотвествией со стадией программы
     */
    @Override
    public void update() {
        ///TODO остановился здесь
    }
}
