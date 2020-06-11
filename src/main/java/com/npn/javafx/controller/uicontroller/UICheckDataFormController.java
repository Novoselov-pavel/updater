package com.npn.javafx.controller.uicontroller;

import com.npn.javafx.model.MainFormStage;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Контроллер для UICheckDataForm.fxml
 */
public class UICheckDataFormController extends UIMainChildAbstractController {
    private final String proceedImg = "/ui/pics/checking.png";
    private final String failImg = "/ui/pics/ok.png";
    private final String okImg = "/ui/pics/fail.png";

    @FXML
    private ImageView currentStageImage;

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
     * Обновляет элемент в соотвествии со стадией программы
     */
    @Override
    public void update() {

        if (stage == MainFormStage.CHECK_INPUT) {
            mainController.setDataValid(false);
            URL checking =  this.getClass().getResource(proceedImg);
            Image image = new Image(checking.toString());
            currentStageImage.setImage(image);
            currentNode.setVisible(true);
            proceedCheck();
        } else {
            currentNode.setVisible(false);
        }
    }

    /**
     * Проверяет значения исходных данных на правильность и выводит в указанный ImageView
     * картинки положительного и отрицательного результата
     * результат также записывается в переменную {@link UIMainFormController#isDataValid}
     */
    private void proceedCheck(){
        new Thread(()->{
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                return;
            }
            URL checking;

            if (mainController.isFilesListIsValid()) {
                checking =  this.getClass().getResource(okImg);
                mainController.setDataValid(true);
            } else {
                checking =  this.getClass().getResource(failImg);
                mainController.setDataValid(false);
            }
            Image image = new Image(checking.toString());
            currentStageImage.setImage(image);
        }).start();
    }
}
