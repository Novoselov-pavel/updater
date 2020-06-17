package com.npn.javafx.controller.uicontroller;

import com.npn.javafx.model.MainFormStage;
import com.npn.javafx.model.Version;
import com.npn.javafx.ui.checkers.IsStringDirPath;
import com.npn.javafx.ui.eventhandler.TextAreaCheck;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class UISelectDestinationDirController extends UIMainChildAbstractController {
    private static final String tickPng = "/ui/pics/tick.png";
    private static final String crossPng = "/ui/pics/cross.png";
    private Version version = null;

    @FXML
    private ImageView versionStatus;

    @FXML
    private TextField versionField;

    @FXML
    private Label versionLabel;

    @FXML
    private Button openFolder;

    @FXML
    private TextArea folderPathField;

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
     * Инициализация переменных и объектов контроллера
     *
     * @param currentNode
     * @param mainController
     * @throws IOException
     */
    @Override
    public void init(Node currentNode, UIMainFormController mainController) throws IOException {
        super.init(currentNode, mainController);
        versionField.addEventHandler(KeyEvent.KEY_RELEASED,x->{
            if (x.getSource() instanceof TextField) {
                TextField field = (TextField) x.getSource();
                if (field.getText() != null && !field.getText().isBlank()) {
                    try {
                        Version version = new Version(field.getText());
                        setStatus(true, versionStatus);
                        this.version = version;
                    } catch (Exception e) {
                        setStatus(false, versionStatus);
                        version = null;
                    }
                } else {
                    versionStatus.setImage(null);
                }
            }
        });
        folderPathField.addEventHandler(KeyEvent.KEY_RELEASED,new TextAreaCheck(IsStringDirPath::test));
        folderPathField.textProperty().addListener((observable, oldValue, newValue) ->{
            if (newValue!=null && IsStringDirPath.test(newValue)) {
                setStatus(true,pathStatus);
            } else {
                setStatus(false,pathStatus);
            }
        });

    }

    public void selectDir() {
        UIOpenFileDialog openFileDialog = new UIOpenFileDialog(mainController.getMainWindows(),
                textResource,
                "SELECT_DIR",
                mainController.getOpenFileDialogIniFolder().toString(),
                mainController.getOpenFileDialogIniFolder());

        File selectedDir = openFileDialog.showOpenDirDialog();
        if (selectedDir!=null) {
            folderPathField.setText(selectedDir.toPath().toString());
        }
    }


    /**
     * Обновляет элемент в соотвествии со стадией программы
     */
    @Override
    public void update() {
        if (stage == MainFormStage.SELECT_DESTINATION_DIR) {
            currentNode.setVisible(true);
        } else {
            currentNode.setVisible(false);
            if (isValid()) {
                mainController.setVersion(version);
                mainController.setDistrDir(Paths.get(folderPathField.getText()));
            }
        }
    }

    /**
     * Выводит правильные ли данные на форме
     *
     * @return
     */
    @Override
    public boolean isValid() {
        return version!=null && IsStringDirPath.test(folderPathField.getText());
    }

    /**
     * Возвращает стадию к которой относится данная форма
     *
     * @return stage
     */
    @Override
    public MainFormStage getFormStage() {
        return MainFormStage.SELECT_DESTINATION_DIR;
    }

    /**
     * Устанавливает картинку версии
     *
     * @param isOk
     */
    private void setStatus(boolean isOk, ImageView imageView) {
        if (isOk) {
            try {
                URL checking =  this.getClass().getResource(tickPng);
                imageView.setImage(new Image(checking.toString()));
            } catch (Exception ignored) {}
        } else {
            try {
                URL checking =  this.getClass().getResource(crossPng);
                imageView.setImage(new Image(checking.toString()));
            } catch (Exception ignored) {}
        }
    }


}