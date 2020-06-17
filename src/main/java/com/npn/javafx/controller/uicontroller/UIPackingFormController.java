package com.npn.javafx.controller.uicontroller;


import com.npn.javafx.model.*;
import com.npn.javafx.model.drivers.ZipDriver;
import com.npn.javafx.ui.ArchiveItemTableView;
import com.npn.javafx.ui.TableFileItem;
import com.npn.javafx.ui.UIMessage;
import com.sun.javafx.tk.Toolkit;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;

public class UIPackingFormController extends UIMainChildAbstractController {
    private List<ArchiveItemTableView.ArchiveObject> archiveObjects = null;
    private volatile boolean valid = false;
    private List<FileItem> zipFilesList = null;
    private Path distrPath = null;
    private Version version = null;
    private final DoubleProperty progress = new SimpleDoubleProperty(0d);
    private final StringProperty text = new SimpleStringProperty("");
    private  javafx.collections.ObservableList<javafx.scene.Node> textStatusNode;
    private final UIMessage uiMessage = UIMessage.getUiMessage();

    private final CountDownLatch sync = new CountDownLatch(2);

    @FXML
    ProgressBar progressBar;

    @FXML
    Label label;


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
            progressBar.progressProperty().unbind();
            progressBar.progressProperty().bind(progress);


            version = mainController.getVersion();
            distrPath = mainController.getDistrDir();
            currentNode.setVisible(true);
            textFlowUpdate(sync);
            archiveObjects = mainController.getArchiveItemsList();
            Thread thread = new Thread(new Work());
            thread.start();

            ////TODO добавить отмену по кнопке esc
        } else {
            currentNode.setVisible(false);
            ///добавить передачу данных в главный контроллер при успешном завершении
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
     * обновление Label
     */
    private void textFlowUpdate(CountDownLatch sync) {
//        Runnable runnable = () -> {
//            sync.countDown();
//            while (!valid) {
//                try {
//                    sync.await();
//                    String s =  uiMessage.getMessage();
//                    text.setValue(s);
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    return;
//                }
//            }
//        };

//        Platform.runLater(runnable);
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                sync.countDown();
                updateMessage("");
                while (!valid) {
                    try {
                        sync.await();
                        String s =  uiMessage.getMessage();
                        updateMessage(s);
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        return null;
                    }
                }
                return null;
            }
        };


        label.textProperty().unbind();
        label.textProperty().bind(task.messageProperty());
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Класс запуска архивирования и обновления ProgressBar и TextFlow
     *
     */
    private class Work implements Runnable {


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
                Thread.sleep(1000);

                if (archiveObjects == null) throw new IllegalArgumentException("List<ArchiveItemTableView.ArchiveObject> is null");

                /// обвязка для вывода информации в TextFlow;
                sync.countDown();
                sync.await();
                uiMessage.setWork(true);


                progress.setValue(0.2d);
                double maxNumber = archiveObjects.size();

                double oneStep = 1d/maxNumber;
                //выполнение упаковки архивов
                for (ArchiveItemTableView.ArchiveObject archiveObject : archiveObjects) {

                    Path zipFilePath = createZipFilePath(archiveObject);
                    ZipDriver zipDriver = new ZipDriver();
                    Map<FileItem, String> map = FileItem.getMapToPackFromArchiveObject(archiveObject);
                    FileItem zipFile = zipDriver.pack(map,zipFilePath, Setting.getConsoleCharset());
                    zipList.add(zipFile);
                    progress.setValue(progress.getValue() + oneStep);
                }
            } catch (InterruptedException e) {
                deleteFiles(zipList);
                valid = false;
            } catch (Exception e) {
                try {
                    uiMessage.sendMessage(e.getMessage());
                } catch (InterruptedException ignore) {}
                deleteFiles(zipList);
                valid = false;
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

        private Path createZipFilePath(ArchiveItemTableView.ArchiveObject archiveObject) {
            return distrPath.resolve(version.toString()).resolve(archiveObject.getName());
        }


    }

}
