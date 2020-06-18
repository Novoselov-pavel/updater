package com.npn.javafx.controller.uicontroller;

import com.npn.javafx.ui.checkers.IsStringDirPath;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;
import java.util.ResourceBundle;

/**
 * Диалог открытия файла/ папки
 */
public class UIOpenFileDialog {
    private final ResourceBundle resourceBundle;
    private final String  resourceBundleKey;
    private final String startPath;
    private final File defaultFolder;
    private final Stage mainWindows;

    public UIOpenFileDialog(Stage mainWindows,ResourceBundle resourceBundle,
                            String resourceBundleKey, String startPath, File defaultFolder) {
        this.resourceBundle = resourceBundle;
        this.resourceBundleKey = resourceBundleKey;
        this.startPath = startPath;
        this.defaultFolder = defaultFolder;
        this.mainWindows = mainWindows;
    }

    /**
     * Shows a new file open dialog.
     *
     * @return the selected file or null if no file has been selected
     */
    public File showOpenFileDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(resourceBundle.getString(resourceBundleKey));
        fileChooser.setInitialDirectory(IsStringDirPath.test(startPath)? Paths.get(startPath).toFile(): defaultFolder);
        return fileChooser.showOpenDialog(mainWindows);

    }

    /**
     * Shows a new directory open dialog.
     *
     * @return   the selected directory or null if no directory has been selected
     */
    public File showOpenDirDialog() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(resourceBundle.getString(resourceBundleKey));
        directoryChooser.setInitialDirectory(IsStringDirPath.test(startPath)? Paths.get(startPath).toFile(): defaultFolder);
        return directoryChooser.showDialog(mainWindows);
    }



}
