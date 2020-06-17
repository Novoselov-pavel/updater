package com.npn.javafx.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

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
        name.setEditable(true);
        pathToUnpack.setEditable(true);
        filesName.setEditable(false);
        // установка обработчиков редактирования ячейки
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        pathToUnpack.setCellFactory(TextFieldTableCell.forTableColumn());
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
        table.setEditable(true);

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
        if (!isSomeItems(items)) {
            fileDate.clear();
            Collection<ArchiveObject> collection = transformToArchiveObject(items);
            fileDate.addAll(collection);
        }
    }

    /**
     * Проверяет было ли изменение элементов
     *
     * @param items
     * @return
     */
    private boolean isSomeItems (TableFileItem[] items) {
        Collection<ArchiveObject> collection = transformToArchiveObject(items);
        if (collection.size()!=fileDate.size()) return false;
        for (ArchiveObject archiveObject : fileDate) {
            if (!collection.contains(archiveObject)) return false;
        }
        return true;
    }

    public static List<ArchiveObject> transformToArchiveObject (TableFileItem[] items) {
        ArrayList<ArchiveObject> retArray = new ArrayList<>();

        Arrays.stream(items).filter(x->!x.isNeedPack()).forEach(x->{
            ArchiveObject archiveObject = new ArchiveObject(x.getRelativePath(),false);
            archiveObject.addTableFileItem(x);
            retArray.add(archiveObject);
        });
        ///для объектов которые требуют упаковки
        List<TableFileItem> list = Arrays.stream(items).filter(TableFileItem::isNeedPack).collect(Collectors.toList());
        while (list.size()>0) {
            ArchiveObject archiveObject = new ArchiveObject(list.get(0).getRelativePath(),true);
            archiveObject.addTableFileItem(list.get(0).cloneWithNewRelativePath(""));
            for (int i = 1; i <list.size(); i++) {
                if (isRelativeToBasePath(list.get(0).getRelativePath(),list.get(i).getRelativePath())) {
                    Path newPath = Paths.get(list.get(0).getRelativePath()).relativize(Paths.get(list.get(i).getRelativePath()));
                    archiveObject.addTableFileItem(list.get(i).cloneWithNewRelativePath(newPath.toString()));
                    list.remove(i);
                    i--;
                }
            }
            retArray.add(archiveObject);
            list.remove(0);
        }
        return retArray;
    }

    public List<ArchiveObject> getArchiveObjectList() {
        return fileDate.stream().collect(Collectors.toUnmodifiableList());
    }

    /**
     * Проверят является ли переданный путь отностельным относительно базовго пути
     *
     * @param basePath базовый путь
     * @param checkingPath проверяемый путь
     * @return true/false
     */
    private static boolean isRelativeToBasePath(String basePath, String checkingPath) {
        if (!checkingPath.startsWith(basePath)) {
            return false;
        }

        Path newPath = Paths.get(basePath).relativize(Paths.get(checkingPath));
        return newPath.toString().length()<=checkingPath.length();
    }

    public static class ArchiveObject {
        // имя файла архива
        private StringProperty name = new SimpleStringProperty(new BigInteger(34,new SecureRandom()).toString(32));
        // путь для распаковки архива
        private StringProperty pathToUnpack = new SimpleStringProperty("");
        private boolean needPack;

        private final Set<TableFileItem> fileItems = new HashSet<>();

        public ArchiveObject(String pathToUnpack, boolean needPack) {
            this.pathToUnpack.setValue(pathToUnpack);
            this.needPack = needPack;
        }

        public void addTableFileItem(final TableFileItem item) {
            if (!needPack) {
                if (!item.getRelativePath().equals(pathToUnpack.getValue())) {
                    throw new IllegalArgumentException("If ArchiveObject don't need pack, only element with same relativePath can included");
                } else {
                    fileItems.add(item);
                    name.setValue(Paths.get(item.getPath()).getFileName().toString());
                }
            } else {
                fileItems.add(item);
            }

        }

        public Set<TableFileItem> getFileItems() {
            return fileItems;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ArchiveObject that = (ArchiveObject) o;
            return needPack == that.needPack &&
                    Objects.equals(pathToUnpack.getValue(), that.pathToUnpack.getValue()) &&
                    Objects.equals(fileItems, that.fileItems);
        }

        @Override
        public int hashCode() {
            return Objects.hash(pathToUnpack.getValue(), needPack, fileItems);
        }
    }
}
