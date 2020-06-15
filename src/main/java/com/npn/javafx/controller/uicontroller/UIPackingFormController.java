package com.npn.javafx.controller.uicontroller;


import com.npn.javafx.model.MainFormStage;
import com.npn.javafx.ui.ArchiveItemTableView;
import javafx.scene.Node;

import java.io.IOException;
import java.util.List;

public class UIPackingFormController extends UIMainChildAbstractController {

    private List<ArchiveItemTableView.ArchiveObject> archiveObjects = null;


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
    }

    /**
     * Обновляет элемент в соотвествии со стадией программы
     */
    @Override
    public void update() {
        if (stage == getFormStage()) {
            currentNode.setVisible(true);
            archiveObjects = mainController.getArchiveItemsList();

        } else {
            currentNode.setVisible(false);
        }
    }

    public static String getFXMLPath() {
        return "/ui/UIPackingForm.fxml";
    }

    /**
     * Выводит правильные ли данные на форме
     *
     * @return
     */
    @Override
    public boolean isValid() {
        return false;
    }

    /**
     * Возвращает стадию к которой относится данная форма
     *
     * @return stage
     */
    @Override
    public MainFormStage getFormStage() {
        return MainFormStage.PACK_DISTR;
    }

    //TODO остановился тут

}
