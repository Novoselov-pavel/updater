package com.npn.javafx.controller.uicontroller;

import com.npn.javafx.model.MainFormStage;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class UIHeaderController {

    private final Stage mainWindows;
    private ResourceBundle resourceBundle;
    private Node currentNode;
    private MainFormStage stage = MainFormStage.SELECT_BASE_PATH;
    private ResourceBundle textResource = ResourceBundle.getBundle("ui.uimainlocale",
            Locale.getDefault());

    @FXML
    private TextFlow textFlow;


    public UIHeaderController(Stage mainWindows, ResourceBundle resourceBundle) throws IOException {
        this.mainWindows = mainWindows;
        this.resourceBundle = resourceBundle;
        currentNode = loadNode();
        setStage(stage);
    }

    private Node loadNode() throws IOException {
        URL xmlUrl = getClass().getResource("/ui/UIMainForm.fxml");
        FXMLLoader loader = new FXMLLoader(xmlUrl,resourceBundle);
        Parent root = loader.load();
        return root;
    }

    /**
     * Устанавливает стадию главного окна
     *
     * @param stage
     * @return
     */
    public Node setStage(MainFormStage stage) {
        this.stage = stage;
        update();
        return currentNode;
    }

    public Node getNode() {
        return currentNode;
    }

    /**
     * Обновляет вид
     */
    private void update() {
        updateText(stage.getHeaderText());
    }

    /**
     * Устанавливает текст заголовка
     *
     * @param resourceText строка из ui/uimainlocale.properties
     */
    private void updateText(String resourceText) {
        Text text = new Text(resourceBundle.getString(resourceText));
        text.setFill(Color.BLACK);
        text.setFont(Font.font("sans-serif", FontPosture.ITALIC,15));
        ObservableList<Node> list = textFlow.getChildren();
        list.clear();
        list.add(text);
    }





}
