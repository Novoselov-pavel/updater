package com.npn.javafx.ui.checkers;

import com.npn.javafx.ui.TableFileItem;
import javafx.collections.ObservableList;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IsStringDirPath {




    public static boolean test(String s) {
        if (s==null || s.isBlank()) return false;
        try {
            Path path = Paths.get(s);
            return Files.exists(path) && Files.isDirectory(path);

        } catch (Exception e) {
            return false;
        }
    }
}
