package com.npn.javafx.controller.uicontroller;


import com.npn.javafx.model.CRC32Calculator;
import com.npn.javafx.model.FileItem;
import com.npn.javafx.model.MainFormStage;
import com.npn.javafx.model.Setting;
import com.npn.javafx.model.drivers.ZipDriver;
import com.npn.javafx.ui.ArchiveItemTableView;
import com.npn.javafx.ui.TableFileItem;
import com.npn.javafx.ui.UIMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class UIPackingFormController extends UIMainChildAbstractController {
    private List<ArchiveItemTableView.ArchiveObject> archiveObjects = null;
    private volatile boolean valid = false;
    private List<FileItem> zipFilesList = null;

    @FXML
    ProgressBar progressBar;

    @FXML
    TextFlow textFlow;


    /**
     * Инициализация переменных и объектов контроллера
     *
     * @param currentNode
     * @param mainController
     * @throws IOException
     */
    @Override
    public void init(Node currentNode, UIMainFormController mainController) throws IOException {
        super.init(currentNode, mainController);

    }

    /**
     * Обновляет элемент в соотвествии со стадией программы
     */
    @Override
    public void update() {
        if (stage == getFormStage()) {
            progressBar.setProgress(0);
            currentNode.setVisible(true);
            archiveObjects = mainController.getArchiveItemsList();
            Thread thread = new Thread(new Work());
            thread.setDaemon(true);
            thread.start();
        } else {
            currentNode.setVisible(false);
        }
    }

    public static String getFXMLPath() {
        return "/ui/UIPackingForm.fxml";
    }

    /**
     * Выводит правильные ли данные на форме
     *
     * @return
     */
    @Override
    public boolean isValid() {
        return valid;
    }

    /**
     * Возвращает стадию к которой относится данная форма
     *
     * @return stage
     */
    @Override
    public MainFormStage getFormStage() {
        return MainFormStage.PACK_DISTR;
    }

    public List<FileItem> getZipFilesList() {
        return zipFilesList;
    }

    /**
     * Класс запуска архивирования и обновления ProgressBar и TextFlow
     *
     */
    private class Work implements Runnable {
        private final UIMessage uiMessage = UIMessage.getUiMessage();

        /**
         * Computes a result, or throws an exception if unable to do so.
         *
         * @return computed result
         * @throws Exception if unable to compute a result
         */
        @Override
        public void run() {
            List<FileItem> zipList = new ArrayList<>();
            try {
                valid = false;
                /// обвязка для вывода информации в TextFlow;
                progressBar.setProgress(0);
                if (archiveObjects == null) throw new IllegalArgumentException("List<ArchiveItemTableView.ArchiveObject> is null");
                Platform.runLater(()->{
                    while (true) {
                        try {
                            String s =  uiMessage.getMessage();
                            Text text = new Text(s);
                            text.setFill(Color.BLACK);
                            textFlow.getChildren().clear();
                            textFlow.getChildren().add(text);
                        } catch (InterruptedException e) {
                            textFlow.getChildren().clear();
                            return;
                        }
                    }
                });
                Thread.sleep(800);
                uiMessage.setWork(true);

                long maxNumber = archiveObjects.size();

                long oneStep = 1L/maxNumber;
                //выполнение упаковки архивов
                for (ArchiveItemTableView.ArchiveObject archiveObject : archiveObjects) {
                    Thread.sleep(10);
                    ZipDriver zipDriver = new ZipDriver();
                    Map<FileItem, String> map = new HashMap<>();

                    for (TableFileItem fileItem : archiveObject.getFileItems()) {
                        Path filePath = Paths.get(fileItem.getPath());
                        FileItem item = new FileItem(filePath);
                        item.setCRC32(new CRC32Calculator().getCRC32(filePath));
                        String relativePath = Paths.get(fileItem.getRelativePath()).resolve(filePath.getFileName()).toString();
                        map.put(item,relativePath);
                        Thread.sleep(1);
                    }
                    FileItem zipFile = zipDriver.pack(map,archiveObject.getZipFilePath(), Setting.getConsoleCharset());
                    zipList.add(zipFile);
                    progressBar.setProgress(progressBar.getProgress() + oneStep);
                }
            } catch (InterruptedException e) {
                deleteFiles(zipList);
                valid = false;
            } catch (Exception e) {
                deleteFiles(zipList);
                valid = false;
                ///TODO добавить вызов окна с ошибкой
            }finally {
                uiMessage.setWork(false);
            }

            zipFilesList = zipList;
            valid = true;
        }

        /**
         * Удаление объектов при отмене
         * @param zipList
         */
        private void deleteFiles(List<FileItem> zipList) {
            zipList.forEach(zip->zip.getPath().toFile().delete());
        }

    }

}
