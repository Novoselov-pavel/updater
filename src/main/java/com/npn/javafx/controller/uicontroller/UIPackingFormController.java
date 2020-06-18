package com.npn.javafx.controller.uicontroller;


import com.npn.javafx.model.*;
import com.npn.javafx.model.drivers.SafeCopyFiles;
import com.npn.javafx.model.drivers.ZipDriver;
import com.npn.javafx.model.drivers.parsers.FileSystemParser;
import com.npn.javafx.model.interfaces.FilesParser;
import com.npn.javafx.ui.ArchiveItemTableView;
import com.npn.javafx.ui.TableFileItem;
import com.npn.javafx.ui.UIMessage;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class UIPackingFormController extends UIMainChildAbstractController {
    private static final String INI_FILE_NAME = "update.ini";
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
        } else {
            currentNode.setVisible(false);
            if (isValid()) {
                mainController.setZipFilesList(zipFilesList);
            }
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

                List<ArchiveItemTableView.ArchiveObject> copyList = archiveObjects.stream().filter(x->!x.isNeedPack()).collect(Collectors.toList());
                List<ArchiveItemTableView.ArchiveObject> archiveObList = archiveObjects.stream().filter(ArchiveItemTableView.ArchiveObject::isNeedPack).collect(Collectors.toList());
                List<FileItem> copyFileItem = copyFiles(copyList);
                zipList.addAll(copyFileItem);

                progress.setValue(0.2d);
                double maxNumber = archiveObjects.size();

                double oneStep = 1d/maxNumber;
                //выполнение упаковки архивов
                for (ArchiveItemTableView.ArchiveObject archiveObject : archiveObList) {

                    Path zipFilePath = createZipFilePath(archiveObject);
                    ZipDriver zipDriver = new ZipDriver();
                    Map<FileItem, String> map = FileItem.getMapToPackFromArchiveObject(archiveObject);
                    FileItem zipFile = zipDriver.pack(map,zipFilePath, Setting.getConsoleCharset());
                    zipFile.setUnpack(true);
                    zipFile.setUnpackPath(Paths.get(""));
                    zipList.add(zipFile);
                    progress.setValue(progress.getValue() + oneStep);
                }
                IniClass iniClass = new IniClass();
                iniClass.addAllFileItem(zipList);
                iniClass.saveToXMLFile(distrPath.resolve(version.toString()).resolve(INI_FILE_NAME));

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
            progress.setValue(1d);
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

        /**
         * Копируем файлы в папку версии
         * @param copyList
         * @throws Exception
         */
        private List<FileItem> copyFiles(List<ArchiveItemTableView.ArchiveObject> copyList) throws Exception {
            List<TableFileItem> list = copyList.stream().flatMap(x->x.getFileItems().stream()).collect(Collectors.toList());
            Path dir =  distrPath.resolve(version.toString());
            List <FileItem> retVal = new ArrayList<>();

            for (TableFileItem fileItem : list) {
                Path rootEl = Paths.get(fileItem.getPath());
                Map<Path,Path> copyPathList = new HashMap<>();
                if(Files.isRegularFile(rootEl)) {
                    Path destination = dir.resolve(rootEl.getFileName());
                    copyPathList.put(rootEl,destination);

                    SafeCopyFiles safeCopyFiles = new SafeCopyFiles(copyPathList);
                    safeCopyFiles.run(); /// используется в однопоточном режиме специально

                    FileItem item = new FileItem(rootEl.getFileName());
                    item.setCRC32(new CRC32Calculator().getCRC32(destination));
                    item.setUnpack(false);
                    item.setUnpackPath(Paths.get(fileItem.getRelativePath()));
                    retVal.add(item);
                } else {
                    FilesParser parser = new FileSystemParser();
                    List<String> files = parser.getFilesAddress(rootEl.toString());
                    for (String file : files) {
                        Path fPath = Paths.get(file);
                        copyPathList.put(fPath,dir.resolve(fPath.getFileName()));
                    }
                    SafeCopyFiles safeCopyFiles = new SafeCopyFiles(copyPathList);
                    safeCopyFiles.run(); /// используется в однопоточном режиме специально
                    for (Map.Entry<Path, Path> entry : copyPathList.entrySet()) {

                        FileItem item = new FileItem(entry.getValue().getFileName());
                        item.setCRC32(new CRC32Calculator().getCRC32(entry.getValue()));
                        item.setUnpack(false);
                        Path relativeEntryPath = rootEl.relativize(entry.getKey());

                        item.setUnpackPath(Paths.get(fileItem.getRelativePath()).resolve(relativeEntryPath));
                        retVal.add(item);
                    }
                }
            }

            return retVal;

        }


    }

}
