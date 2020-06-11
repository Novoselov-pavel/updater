package com.npn.javafx.controller.uicontroller;

import com.npn.javafx.model.MainFormStage;
import com.npn.javafx.ui.checkers.IsStringDirPath;
import com.npn.javafx.ui.eventhandler.TextAreaCheck;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ResourceBundle;

/**
 * Контроллер для UISelectPathForm.fxml
 */
public class UISelectPathFormController extends UIMainChildAbstractController {

    @FXML
    private Button selectPathButton;

    @FXML
    private TextArea textPathArray;

    public UISelectPathFormController(UIMainFormController mainController, ResourceBundle resourceBundle) throws IOException {
        super(mainController, resourceBundle);
    }

    @Override
    Node loadNode(String resourcePath) throws IOException {
        Node node = super.loadNode(resourcePath);
        textPathArray.addEventHandler(KeyEvent.KEY_RELEASED,new TextAreaCheck(IsStringDirPath::test));
        return node;
    }

    /**
     * Возвращает путь к файлу FXML для закрузки элемента
     *
     * @return путь к файлу FXML
     */
    @Override
    public String getFXMLPath() {
        return "ui/UISelectPathForm.fxml";
    }

    /**
     * Обновляет элемент в соотвествии со стадией программы
     */
    @Override
    public void update() {
        if (stage == MainFormStage.SELECT_BASE_PATH) {
            currentNode.setVisible(true);
        } else {
            mainController.setBasePath(textPathArray.getText());
            currentNode.setVisible(false);
        }
    }


    /**
     * Обработчик нажатия кнопки
     */
    public void selectBasePath() {
        UIOpenFileDialog openFileDialog = new UIOpenFileDialog(mainController.getMainWindows(),
                                                textResource,
                                                "SELECT_BASE_PATH",
                                                textPathArray.getText(),
                                                mainController.getOpenFileDialogIniFolder());

        File selectedDir = openFileDialog.showOpenDirDialog();
        if (selectedDir!=null) {
            textPathArray.setText(selectedDir.toPath().toString());
            mainController.setOpenFileDialogIniFolder(selectedDir);
        }
    }




}
