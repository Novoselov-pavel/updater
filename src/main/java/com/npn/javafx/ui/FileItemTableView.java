package com.npn.javafx.ui;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.nio.file.Path;
import java.util.ResourceBundle;


/**
 * Класс для определения TableView для второго режима
 */
public class FileItemTableView {
    private final TableView<TableFileItem> table;
    private ResourceBundle resourceBundle;

    public FileItemTableView(final TableView<TableFileItem> table, ResourceBundle resourceBundle) {
        this.table = table;
        this.resourceBundle = resourceBundle;
    }

    public void init() {
        table.setEditable(true);
        TableColumn<TableFileItem, Path> path = new TableColumn<>(resourceBundle.getString("PATH_OF_FILE"));
        ///TODO
    }



}
