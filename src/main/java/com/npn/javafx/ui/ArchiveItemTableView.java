package com.npn.javafx.ui;

import javafx.scene.control.TableView;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class ArchiveItemTableView {
    private final TableView<ArchiveObject> tableView;
    private final ResourceBundle resourceBundle;

    public ArchiveItemTableView(final TableView<ArchiveObject> tableView,final ResourceBundle resourceBundle) {
        this.tableView = tableView;
        this.resourceBundle = resourceBundle;
    }


    public static class ArchiveObject {
        private String basePath;
        private boolean needPack;

        private Set<TableFileItem> fileItems = new HashSet<>();

        public ArchiveObject(String basePath, boolean needPack) {
            this.basePath = basePath;
            this.needPack = needPack;
        }

        public void addTableFileItem(final TableFileItem item) {
            if (!needPack) {
                if (!item.getRelativePath().equals(basePath)) {
                    throw new IllegalArgumentException("If ArchiveObject don't need pack, only element with same relativePath can included");
                } else {
                    fileItems.add(item);
                }
            } else {
                ///TODO
            }

        }


    }
}
