package com.npn.javafx.controller.uicontroller;

import com.npn.javafx.model.MainFormStage;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Контроллер для UIHeader.fxml
 */
public class UIHeaderController extends UIMainChildAbstractController {


    @FXML
    private TextFlow textFlow;

    /**
     * Возвращает путь к файлу FXML для загрузки элемента
     *
     * @return путь к файлу FXML
     */
    public static String getFXMLPath() {
        return "/ui/UIHeader.fxml";
    }

    /**
     * Обновляет вид
     */
    public void update() {
        updateText(stage.getHeaderText());
        currentNode.setVisible(true);
    }

    /**
     * Выводит правильные ли данные на форме
     *
     * @return
     */
    @Override
    public boolean isValid() {
        return true;
    }

    /**
     * Возвращает стадию к которой относится данная форма
     *
     * @return null для данного элемента, так как он относится ко всем стадиям
     */
    @Override
    public MainFormStage getFormStage() {
        return null;
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
