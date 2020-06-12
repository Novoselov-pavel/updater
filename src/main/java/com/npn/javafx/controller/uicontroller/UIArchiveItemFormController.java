package com.npn.javafx.controller.uicontroller;

import com.npn.javafx.model.MainFormStage;
import com.npn.javafx.ui.ArchiveItemTableView;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableView;

import java.io.IOException;

public class UIArchiveItemFormController extends UIMainChildAbstractController {

    @FXML
    private TableView<ArchiveItemTableView.ArchiveObject> packTable;

    private ArchiveItemTableView archiveItemTable = null;


    public static String getFXMLPath() {
        return "/ui/UIArchiveItemForm.fxml";
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
        ArchiveItemTableView archiveItemTable = new ArchiveItemTableView(packTable,textResource);
        archiveItemTable.init();
        this.archiveItemTable = archiveItemTable;
    }

    /**
     * Обновляет элемент в соотвествии со стадией программы
     */
    @Override
    public void update() {
        if (stage == MainFormStage.DISTR_VIEW) {
            currentNode.setVisible(true);
            archiveItemTable.addAllArchiveObject(mainController.getTableItems());
        } else {
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
}
