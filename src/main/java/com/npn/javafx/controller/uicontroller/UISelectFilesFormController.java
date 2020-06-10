package com.npn.javafx.controller.uicontroller;

import com.npn.javafx.model.MainFormStage;
import com.npn.javafx.ui.FileItemTableView;
import com.npn.javafx.ui.TableFileItem;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

/**
 * Контроллер для UISelectFilesForm.fxml
 */
public class UISelectFilesFormController extends UIMainChildAbstractController {

    @FXML
    private Button addDirToFileTable;

    @FXML
    private TableView<TableFileItem> fileTable;

    @FXML
    private Button addFileToFileTable;

    private FileItemTableView table;


    public UISelectFilesFormController(UIMainFormController mainController, ResourceBundle resourceBundle) throws IOException {
        super(mainController, resourceBundle);
    }


    /**
     * Возвращает путь к файлу FXML для закрузки элемента
     *
     * @return путь к файлу FXML
     */
    @Override
    public String getFXMLPath() {
        return "ui/UISelectFilesForm.fxml";
    }

    @Override
    Node loadNode(String resourcePath) throws IOException {
        Node node = super.loadNode(resourcePath);
        FileItemTableView table = new FileItemTableView(fileTable,resourceBundle);
        table.init();
        this.table = table;
        return node;
    }

    /**
     * Обновляет элемент в соотвествией со стадией программы
     */
    @Override
    public void update() {
        if (stage == MainFormStage.SELECT_FILE) {
            currentNode.setVisible(true);
        } else {
            currentNode.setVisible(false);
        }
    }

    public void selectDir() {
        UIOpenFileDialog openFileDialog = new UIOpenFileDialog(mainController.getMainWindows(),
                resourceBundle,
                "SELECT_DIR",
                mainController.getOpenFileDialogIniFolder().toString(),
                mainController.getOpenFileDialogIniFolder());

        File selectedDir = openFileDialog.showOpenDirDialog();
        if (selectedDir!=null) {
            mainController.setOpenFileDialogIniFolder(selectedDir);
            addNewFileToTable(selectedDir,table);
        }
    }

    public void selectFile() {
        UIOpenFileDialog openFileDialog = new UIOpenFileDialog(mainController.getMainWindows(),
                resourceBundle,
                "SELECT_FILE",
                mainController.getOpenFileDialogIniFolder().toString(),
                mainController.getOpenFileDialogIniFolder());

        File selectedDir = openFileDialog.showOpenFileDialog();
        if (selectedDir!=null) {
            mainController.setOpenFileDialogIniFolder(selectedDir.toPath().getParent().toFile());
            addNewFileToTable(selectedDir,table);
        }
    }

    private void addNewFileToTable(File file, FileItemTableView table) {
        if (file!=null) {
            TableFileItem fileItem = new TableFileItem(file.getPath());
            table.addFileItem(fileItem);
            try{
                Path path = Paths.get(mainController.getBasePath());
                String relativePath = path.relativize(file.toPath()).toString();
                fileItem.setRelativePath(relativePath);
            } catch (Exception ignored) {}
        }
    }

}
