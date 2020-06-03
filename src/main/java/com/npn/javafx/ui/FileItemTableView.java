package com.npn.javafx.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.util.ResourceBundle;


/**
 * Класс для определения TableView для второго режима
 */
public class FileItemTableView {
    private final TableView<TableFileItem> table;
    private final ResourceBundle resourceBundle;
    private ObservableList<TableFileItem> fileDate = FXCollections.observableArrayList();


    public FileItemTableView(final TableView<TableFileItem> table, ResourceBundle resourceBundle) {
        this.table = table;
        this.resourceBundle = resourceBundle;
    }

    public void init() {
        table.setEditable(true);
        // Добавление колонок
        TableColumn<TableFileItem, String> path = new TableColumn<>(resourceBundle.getString("PATH_OF_FILE"));
        TableColumn<TableFileItem, String> relativePath = new TableColumn<>(resourceBundle.getString("RELATIVE_PATH_OF_UNPACK"));
        TableColumn<TableFileItem, Boolean> needArchive = new TableColumn<>(resourceBundle.getString("NEED_PACK"));
        //размеры
        path.setMinWidth(550);
        relativePath.setMinWidth(350);
        needArchive.setMinWidth(100);
        needArchive.setMaxWidth(100);
        //создание отображения ячеек в колонках
        setColumnCellFactory(path,relativePath,needArchive);
        //подключение источников данных к ячейкам
        path.setCellValueFactory(x->x.getValue().pathProperty());
        relativePath.setCellValueFactory(x->x.getValue().relativePathProperty());
        needArchive.setCellValueFactory(x->x.getValue().needPackProperty());
        // подключение обработчика окончания редактирования ячейки
        setColumnCellCommit(path,relativePath);

//        temp(fileDate);
        //подключение колонок и источника
        table.getColumns().addAll(path,relativePath,needArchive);
        table.setItems(fileDate);
        //добавление удаления строк по кнопке delete
        table.addEventHandler(KeyEvent.KEY_RELEASED, x-> {
            if (x.getCode()== KeyCode.DELETE) {
                ObservableList<TableFileItem> selectedItems =  table.getSelectionModel().getSelectedItems();
                table.getItems().removeAll(selectedItems);
            }
        });
    }

    public void addFileItem(final TableFileItem tableFileItem) {
        fileDate.add(tableFileItem);
    }






//    private void temp(ObservableList<TableFileItem> fileDate) {
//        TableFileItem item1 = new TableFileItem();
//        item1.setRelativePath("a/b/c");
//        item1.setPath("home/");
//        item1.setNeedPack(true);
//
//        fileDate.add(item1);
//    }

    /**
     * Создание отображения ячеек в колонках, включая изменение отображения при ошибке
     *
     * @param path TableColumn
     * @param relativePath TableColumn
     * @param needArchive TableColumn
     */
    private void setColumnCellFactory(TableColumn<TableFileItem, String> path,
                                         TableColumn<TableFileItem, String> relativePath,
                                         TableColumn<TableFileItem, Boolean> needArchive) {

        /*
         * Устанавливает c проверкой значения и отрисовкой красным цветом шрифта если не правильное.
         */
        path.setCellFactory(col -> {
                    TextFieldTableCell<TableFileItem, String> cell = new TextFieldTableCell<>() {
                        //Переопределяем отрисовку(главный метод)
                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                setText(null);
                                setGraphic(null);
                            } else {
                                if (TableFileItem.checkInputPath(item)) {
                                    setTextFill(Color.BLACK);
                                } else {
                                    setTextFill(Color.RED);
                                }
                            }
                        }
                    };
                    //устанавливаем конвертер значений в строку
                    cell.setConverter(new StringConverter<String>() {
                        @Override
                        public String toString(String object) {
                            return object.toString();
                        }

                        @Override
                        public String fromString(String string) {
                            return string;
                        }
                    });
                    return cell;
                }
        );

        relativePath.setCellFactory(TextFieldTableCell.forTableColumn());

        //В результате привязки  needArchive.setCellValueFactory(x->x.getValue().needPackProperty());
        //и привязки ниже обработчик событий для CheckBox не нужен
        needArchive.setCellFactory(tableFileItemBooleanTableColumn -> {
            CheckBoxTableCell<TableFileItem, Boolean> cell = new CheckBoxTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });

    }


    /**
     * Подключение обработчика окончания редактирования ячейки
     *
     * @param path TableColumn
     * @param relativePath TableColumn
     */
    private void setColumnCellCommit(TableColumn<TableFileItem, String> path,
                                      TableColumn<TableFileItem, String> relativePath) {

        path.setOnEditCommit(event -> {
            TablePosition<TableFileItem,String> position = event.getTablePosition();
            String newPath = event.getNewValue();
            TableFileItem item = event.getTableView().getItems().get(position.getRow());
            item.setPath(newPath);
        });

        relativePath.setOnEditCommit(event -> {
            TablePosition<TableFileItem,String> position = event.getTablePosition();
            String newPath = event.getNewValue();
            TableFileItem item = event.getTableView().getItems().get(position.getRow());
            item.setRelativePath(newPath);
        });
    }


}
