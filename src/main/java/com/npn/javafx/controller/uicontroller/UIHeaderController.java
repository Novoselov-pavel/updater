package com.npn.javafx.controller.uicontroller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Контроллер для UIHeader.fxml
 */
public class UIHeaderController extends UIMainChildAbstractController {

    UIHeaderController() {
    }

    @FXML
    private TextFlow textFlow;

    /**
     * Возвращает путь к файлу FXML для закрузки элемента
     *
     * @return путь к файлу FXML
     */
    public static String getFXMLPath() {
        return "/ui/UIMainForm.fxml";
    }

    /**
     * Обновляет вид
     */
    public void update() {
        updateText(stage.getHeaderText());
    }

    /**
     * Устанавливает текст заголовка
     *
     * @param resourceText строка из ui/uimainlocale.properties
     */
    private void updateText(String resourceText) {
        Text text = new Text(textResource.getString(resourceText));
        text.setFill(Color.BLACK);
        text.setFont(Font.font("sans-serif", FontPosture.ITALIC,15));
        ObservableList<Node> list = textFlow.getChildren();
        list.clear();
        list.add(text);
    }

}
