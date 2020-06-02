package com.npn.javafx.ui;

import java.nio.file.Path;

/**
 * Класс объектов для работы с FileItemTableView
 */
public class TableFileItem {

    /**
     * Путь к файлу/папке
     */
    private Path path;

    /**
     * Относительный путь куда распаковывается файл/папка
     */
    private String relativePath;

    /**
     * Требуется архивание файла/папки или нет
     */
    private boolean needArchive;

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public boolean isNeedArchive() {
        return needArchive;
    }

    public void setNeedArchive(boolean needArchive) {
        this.needArchive = needArchive;
    }
}
