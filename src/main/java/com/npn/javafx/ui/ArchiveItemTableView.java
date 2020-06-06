package com.npn.javafx.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class ArchiveItemTableView {
    private final TableView<ArchiveObject> table;
    private final ResourceBundle resourceBundle;
    private final ObservableList<ArchiveObject> fileDate = FXCollections.observableArrayList();

    public ArchiveItemTableView(final TableView<ArchiveObject> table, final ResourceBundle resourceBundle) {
        this.table = table;
        this.resourceBundle = resourceBundle;
    }

    public void init() {
        table.setEditable(true);
        TableColumn<ArchiveObject, String> name = new TableColumn<>(resourceBundle.getString("FILE_NAME_IN_DISTR"));
        TableColumn<ArchiveObject, String> pathToUnpack = new TableColumn<>(resourceBundle.getString("RELATIVE_PATH_OF_UNPACK"));
        TableColumn<ArchiveObject, String> filesName = new TableColumn<>(resourceBundle.getString("FILES_PATHS_TO_PACK"));
        //размеры
        name.setMinWidth(220);
        pathToUnpack.setMinWidth(400);
        filesName.setMinWidth(400);
        //доп свойства
        filesName.setEditable(false);
        //подключение источников данных к ячейкам
        name.setCellValueFactory(x->x.getValue().nameProperty());
        pathToUnpack.setCellValueFactory(x->x.getValue().pathToUnpackProperty());
        filesName.setCellValueFactory(x->new SimpleStringProperty(x.getValue().getFilesPaths()));
        // подключение обработчика окончания редактирования ячейки
        name.setOnEditCommit(event -> {
            TablePosition<ArchiveObject,String> position = event.getTablePosition();
            String newName = event.getNewValue();
            ArchiveObject item = event.getTableView().getItems().get(position.getRow());
            item.setName(newName);
        });
        pathToUnpack.setOnEditCommit(event -> {
            TablePosition<ArchiveObject,String> position = event.getTablePosition();
            String newPath = event.getNewValue();
            ArchiveObject item = event.getTableView().getItems().get(position.getRow());
            item.setPathToUnpack(newPath);
        });



        //подключение колонок и источника
        table.getColumns().addAll(name,pathToUnpack,filesName);
        table.setItems(fileDate);

        //добавление удаления строк по кнопке delete
        table.addEventHandler(KeyEvent.KEY_RELEASED, x-> {
            if (x.getCode()== KeyCode.DELETE) {
                ObservableList<ArchiveObject> selectedItems =  table.getSelectionModel().getSelectedItems();
                table.getItems().removeAll(selectedItems);
            }
        });
    }

    public void addAllArchiveObject(TableFileItem[] items) {
        ///для объектов которые не требуют упаковки
        Arrays.stream(items).filter(x->!x.isNeedPack()).forEach(x->{
            ArchiveObject archiveObject = new ArchiveObject(x.getRelativePath(),false);
            archiveObject.addTableFileItem(x);
        });
        ///для объектов которые требуют упаковки
        //TODO остановился здесь

    }



    public static class ArchiveObject {
        private StringProperty name = new SimpleStringProperty(new BigInteger(10,new SecureRandom()).toString());
        private StringProperty pathToUnpack = new SimpleStringProperty("");
        private boolean needPack;

        private final Set<TableFileItem> fileItems = new HashSet<>();

        public ArchiveObject(String pathToUnpack, boolean needPack) {
            this.pathToUnpack.setValue(pathToUnpack);
            this.needPack = needPack;
        }

        public void addTableFileItem(final TableFileItem item) {
            if (!needPack) {
                if (!item.getRelativePath().equals(pathToUnpack)) {
                    throw new IllegalArgumentException("If ArchiveObject don't need pack, only element with same relativePath can included");
                } else {
                    fileItems.add(item);
                    name.setValue(Paths.get(item.getPath()).getFileName().toString());
                }
            } else {
                Path base = Paths.get(pathToUnpack.getValue());
                TableFileItem newItem = item.cloneWithNewRelativePath(base.relativize(Paths.get(item.getRelativePath())).toString());
                fileItems.add(newItem);
            }

        }

        public String getFilesPaths() {
            StringBuilder builder = new StringBuilder();
            fileItems.forEach(x->builder.append(x.getPath()).append("\n"));
            return builder.toString();
        }

        public String getPathToUnpack() {
            return pathToUnpack.getValue();
        }

        public StringProperty pathToUnpackProperty() {
            return pathToUnpack;
        }

        public void setPathToUnpack(String pathToUnpack) {
            this.pathToUnpack.set(pathToUnpack);
        }

        public boolean isNeedPack() {
            return needPack;
        }

        public String getName() {
            return name.get();
        }

        public StringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }
    }
}
