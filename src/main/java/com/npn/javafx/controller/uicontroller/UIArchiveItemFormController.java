package com.npn.javafx.controller.uicontroller;

import com.npn.javafx.model.MainFormStage;
import com.npn.javafx.ui.ArchiveItemTableView;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.util.ResourceBundle;

public class UIArchiveItemFormController extends UIMainChildAbstractController {

    @FXML
    private TableView<ArchiveItemTableView.ArchiveObject> packTable;

    private ArchiveItemTableView archiveItemTable = null;

    public UIArchiveItemFormController(UIMainFormController mainController, ResourceBundle resourceBundle) throws IOException {
        super(mainController, resourceBundle);
    }

    /**
     * Возвращает путь к файлу FXML для закрузки элемента
     *
     * @return путь к файлу FXML
     */
    @Override
    public String getFXMLPath() {
        return "ui/UIArchiveItemForm.fxml";
    }

    @Override
    Node loadNode(String resourcePath) throws IOException {
        Node node = super.loadNode(resourcePath);
        ArchiveItemTableView archiveItemTable = new ArchiveItemTableView(packTable,textResource);
        archiveItemTable.init();
        this.archiveItemTable = archiveItemTable;
        return node;
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
}
