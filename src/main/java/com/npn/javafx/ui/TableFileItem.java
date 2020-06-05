package com.npn.javafx.ui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Класс объектов для работы с FileItemTableView
 * используется обертка Property для соединения с таблицей
 */
public class TableFileItem {

    /**
     * Путь к файлу/папке
     */
    private final StringProperty path = new SimpleStringProperty();

    /**
     * Относительный путь куда распаковывается файл/папка
     */
    private final StringProperty relativePath = new SimpleStringProperty("");

    /**
     * Требуется архивание файла/папки или нет
     */
    private final BooleanProperty needPack = new SimpleBooleanProperty(true);

    public TableFileItem() {
        path.setValue("");
        relativePath.setValue("");
        needPack.setValue(true);
    }

    public TableFileItem(String path, String relativePath, boolean needPack) {
        this.path.setValue(path);
        this.relativePath.setValue(relativePath);
        this.needPack.setValue(needPack);
    }

    public TableFileItem(String path) {
        this.path.setValue(path);
    }

    public String getPath() {
        return path.get();
    }

    public void setPath(String path) {
        this.path.setValue(path);
    }

    public String getRelativePath() {
        return relativePath.get();
    }

    public void setRelativePath(String relativePath) {
        this.relativePath.setValue(relativePath);
    }

    public boolean isNeedPack() {
        return needPack.get();
    }

    public void setNeedPack(boolean needPack) {
        this.needPack.setValue(needPack);
    }

    public StringProperty pathProperty() {
        return path;
    }

    public StringProperty relativePathProperty() {
        return relativePath;
    }

    public BooleanProperty needPackProperty() {
        return needPack;
    }

    /**
     * Проверяет значения и корректирует их при незначительных ошибках
     * @return true если значения верные, false если нет
     */
    public boolean checkValue() {
        if (isValidInputPath(path.getValue())) {
            if (relativePath.getValue().equals("/") || relativePath.getValue().equals("\\")) {
                relativePath.set("");
                return true;
            }

            if (relativePath.getValue().startsWith("/")||relativePath.getValue().startsWith("\\")) {
                relativePath.setValue(relativePath.getValue().substring(1));
                return true;
            }
            try {
                if (!relativePath.getValue().isBlank()) {
                    Path path = Paths.get(relativePath.getValue());
                }
            } catch (InvalidPathException e) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * Клонирует объект с новым относительным путем
     *
     * @param relativePath
     * @return
     */
    public TableFileItem cloneWithNewRelativePath(String relativePath) {
        return new TableFileItem(this.getPath(),relativePath,this.isNeedPack());
    }

    /**
     * Проверяет правильность и существование файло
     * @param path
     * @return
     */
    public static boolean isValidInputPath(String path) {
        if (path == null || path.isBlank()) return false;
        try {
            Path cuPath = Paths.get(path);
            return Files.exists(cuPath);
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableFileItem fileItem = (TableFileItem) o;
        return Objects.equals(path, fileItem.path) &&
                Objects.equals(relativePath, fileItem.relativePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, relativePath);
    }


}
