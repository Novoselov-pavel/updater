package com.npn.javafx.controller.uicontroller;

import com.npn.javafx.model.MainFormStage;
import com.npn.javafx.ui.checkers.IsStringDirPath;
import com.npn.javafx.ui.eventhandler.TextAreaCheck;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.io.IOException;

/**
 * Контроллер для UISelectPathForm.fxml
 */
public class UISelectPathFormController extends UIMainChildAbstractController {

    @FXML
    private Button selectPathButton;

    @FXML
    private TextArea textPathArray;

    /**
     * Инициализация переменных и объектов контроллера
     *
     * @param currentNode
     * @param mainController
     * @throws IOException
     */
    @Override
    public void init(Node currentNode, UIMainFormController mainController) throws IOException {
        super.init(currentNode, mainController);
        textPathArray.addEventHandler(KeyEvent.KEY_RELEASED,new TextAreaCheck(IsStringDirPath::test));
    }

    /**
     * Возвращает путь к файлу FXML для закрузки элемента
     *
     * @return путь к файлу FXML
     */
    public static String getFXMLPath() {
        return "/ui/UISelectPathForm.fxml";
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
     * @return stage
     */
    @Override
    public MainFormStage getFormStage() {
        return MainFormStage.SELECT_BASE_PATH;
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
