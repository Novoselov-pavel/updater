package com.npn.javafx.controller.uicontroller;

        import com.npn.javafx.Updater;
        import com.npn.javafx.ui.ArchiveItemTableView;
        import com.npn.javafx.ui.FileItemTableView;
        import com.npn.javafx.ui.TableFileItem;
        import com.npn.javafx.ui.checkers.IsStringDirPath;
        import com.npn.javafx.ui.eventhandler.TextAreaCheck;
        import com.npn.javafx.model.MainFormStage;
        import javafx.application.Platform;
        import javafx.collections.ObservableList;
        import javafx.event.ActionEvent;
        import javafx.event.EventHandler;
        import javafx.fxml.FXML;
        import javafx.scene.Node;
        import javafx.scene.control.*;
        import javafx.scene.image.Image;
        import javafx.scene.image.ImageView;
        import javafx.scene.input.KeyEvent;
        import javafx.scene.layout.AnchorPane;
        import javafx.scene.paint.Color;
        import javafx.scene.text.Font;
        import javafx.scene.text.FontPosture;
        import javafx.scene.text.Text;
        import javafx.scene.text.TextFlow;
        import javafx.stage.DirectoryChooser;
        import javafx.stage.FileChooser;
        import javafx.stage.Stage;


        import java.io.File;
        import java.net.URL;
        import java.nio.file.Path;
        import java.nio.file.Paths;
        import java.util.Locale;
        import java.util.ResourceBundle;

public class UIMainFormController {
    private File openFileDialogIniFolder = Updater.JAR_FILE_PATH.toFile();
    private MainFormStage stage = MainFormStage.SELECT_BASE_PATH;
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("ui.uimainlocale",
            Locale.getDefault());
    private Stage mainWindows;
    private FileItemTableView tableClass = null;
    private ArchiveItemTableView archiveTableClass = null;
    private volatile boolean isDataValid = false;

    public UIMainFormController() {
    }

    @FXML
    private TextFlow textFlow;

    @FXML
    private SplitPane mainSplitPanel;

    @FXML
    private Button selectPathButton;

    @FXML
    private Button selectDirButton;

    @FXML
    private TextArea dirPathArray;

    @FXML
    private TextArea textPathArray;

    @FXML
    private ButtonBar buttonBar;

    @FXML
    private Button buttonHelp;

    @FXML
    private Button buttonBack;

    @FXML
    private Button buttonNext;

    @FXML
    private Button buttonExit;

    @FXML
    private TableView<TableFileItem> fileTable;

    @FXML
    private TableView<ArchiveItemTableView.ArchiveObject> packTable;

    @FXML
    private Button addDirToFileTable;

    @FXML
    private Button addFileToFileTable;

    @FXML
    private AnchorPane mainAnchorPanel;

    @FXML
    private ImageView currentStageImage;

    /**
     * Инициализация таблицы
     * @param mainWindows ссылка на основное окно
     */
    public void init(Stage mainWindows) {
        this.mainWindows = mainWindows;
        changeStage(MainFormStage.SELECT_BASE_PATH);
        buttonExit.setOnAction(e-> Platform.exit());
        FileItemTableView table = new FileItemTableView(fileTable,resourceBundle);
        table.init();
        tableClass = table;
        buttonBack.setOnAction(new ButtonBackPress());
        buttonNext.setOnAction(new ButtonNextPress());

        addDirToFileTable.setOnAction(x->{
            File file = chooseDir("SELECT_DIR",openFileDialogIniFolder.getPath());
            addNewFileToTable(file,table);
        });

        addFileToFileTable.setOnAction(x->{
            File file = chooseFile("SELECT_FILE",openFileDialogIniFolder.getPath());
            addNewFileToTable(file,table);
        });

        ArchiveItemTableView archiveItemTable = new ArchiveItemTableView(packTable,resourceBundle);
        archiveItemTable.init();
        archiveTableClass = archiveItemTable;

        textPathArray.addEventHandler(KeyEvent.KEY_RELEASED,new TextAreaCheck(IsStringDirPath::test));
    }

    public void changeStage(MainFormStage stage) {
        if (stage==null) {
            throw new IllegalArgumentException();
        }
        if (stage==MainFormStage.SELECT_BASE_PATH) {
            firstStageView();
        } else if (stage == MainFormStage.SELECT_FILE) {
            secondStage();
        } else if (stage == MainFormStage.CHECK_INPUT) {
            thirdStage();
        } else if (stage == MainFormStage.DISTR_VIEW) {
            fourthStage();
        } else  if (stage == MainFormStage.SELECT_DESTINATION_DIR) {
            fifthStage();
        }
    }

    private void firstStageView() {
        fileTable.setVisible(false);
        addDirToFileTable.setVisible(false);
        addFileToFileTable.setVisible(false);
        currentStageImage.setVisible(false);
        packTable.setVisible(false);
        selectDirButton.setVisible(false);
        dirPathArray.setVisible(false);


        selectPathButton.setVisible(true);
        textPathArray.setVisible(true);

    }

    private void secondStage() {
        selectPathButton.setVisible(false);
        textPathArray.setVisible(false);
        currentStageImage.setVisible(false);
        packTable.setVisible(false);
        selectDirButton.setVisible(false);
        dirPathArray.setVisible(false);

        fileTable.setVisible(true);
        addDirToFileTable.setVisible(true);
        addFileToFileTable.setVisible(true);
    }


    private void thirdStage() {
        selectPathButton.setVisible(false);
        textPathArray.setVisible(false);
        fileTable.setVisible(false);
        addDirToFileTable.setVisible(false);
        addFileToFileTable.setVisible(false);
        packTable.setVisible(false);
        selectDirButton.setVisible(false);
        dirPathArray.setVisible(false);

        currentStageImage.setVisible(true);

        URL checking =  this.getClass().getResource("/ui/pics/checking.png");
        try {
            Image image = new Image(checking.toString());
            currentStageImage.setImage(image);
        } catch (Exception ignored) {
        }
        Image image = new Image(checking.toString());
        currentStageImage.setImage(image);
        checkTableClassData(currentStageImage, "/ui/pics/ok.png","/ui/pics/fail.png");
    }

    private void fourthStage() {
        selectPathButton.setVisible(false);
        textPathArray.setVisible(false);
        fileTable.setVisible(false);
        addDirToFileTable.setVisible(false);
        addFileToFileTable.setVisible(false);
        currentStageImage.setVisible(false);
        selectDirButton.setVisible(false);
        dirPathArray.setVisible(false);


        packTable.setVisible(true);
        archiveTableClass.addAllArchiveObject(tableClass.getTableFileItems());
    }

    private void fifthStage() {
        selectPathButton.setVisible(false);
        textPathArray.setVisible(false);
        fileTable.setVisible(false);
        addDirToFileTable.setVisible(false);
        addFileToFileTable.setVisible(false);
        currentStageImage.setVisible(false);

        packTable.setVisible(false);

        selectDirButton.setVisible(true);
        dirPathArray.setVisible(true);

        ///TODO
    }


    /**
     * Обработка нажатия на кнопку выбора базового пути
     */
    public void selectBasePath() {
        File selectedDir = chooseDir("SELECT_BASE_PATH",textPathArray.getText());

        if (selectedDir!=null) {
            textPathArray.setText(selectedDir.toPath().toString());
        }
    }

    /**
     * Обработка нажатия на кнопку выбора пути хранения
     */
    public void selectDirPath() {
        File selectedDir = chooseDir("SELECT_BASE_PATH",textPathArray.getText());

        if (selectedDir!=null) {
            dirPathArray.setText(selectedDir.toPath().toString());
        }
    }


    /**
     * Открывает меню выбора папок
     *
     * @param resourceBundleKey ключ для локализации заголовка
     * @param startPath начальный путь
     * @return File или null если отменен
     */
    private File chooseDir(String resourceBundleKey, String startPath) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(resourceBundle.getString(resourceBundleKey));
        directoryChooser.setInitialDirectory(IsStringDirPath.test(startPath)? Paths.get(startPath).toFile(): openFileDialogIniFolder);
        File selectedDir = directoryChooser.showDialog(mainWindows);
        if (selectedDir!=null) {
            openFileDialogIniFolder = selectedDir;
        }
        return selectedDir;
    }

    /**
     * Открывает меню выбора файлов
     *
     * @param resourceBundleKey ключ для локализации заголовка
     * @param startPath начальный путь
     * @return File или null если отменен
     */
    private File chooseFile(String resourceBundleKey, String startPath) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(resourceBundle.getString(resourceBundleKey));
        fileChooser.setInitialDirectory(IsStringDirPath.test(startPath)? Paths.get(startPath).toFile(): openFileDialogIniFolder);
        File selectedFile = fileChooser.showOpenDialog(mainWindows);
        if (selectedFile!=null) {
            openFileDialogIniFolder = selectedFile.toPath().getParent().toFile();
        }
        return selectedFile;
    }

    private void addNewFileToTable(File file, FileItemTableView table) {
        if (file!=null) {
            TableFileItem fileItem = new TableFileItem(file.getPath());
            table.addFileItem(fileItem);
            try{
                Path path = Paths.get(textPathArray.getText());
                String relativePath = path.relativize(file.toPath()).toString();
                fileItem.setRelativePath(relativePath);
            } catch (Exception ignored) {}
        }
    }

    /**
     * Проверяет значения исходных данных на правильность и выводит в указанный ImageView
     * картинки положительного и отрицательного результата
     * результат также записывается в переменную {@link this#isDataValid}
     *
     * @param view вид куда бюудет подключена картинка
     * @param positive адрес картинки при удачной проверке
     * @param negativePath адрес картинки при ошибочной проверке
     */
    private void checkTableClassData(ImageView view, String positive, String negativePath){
        new Thread(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }
            URL checking;

            if (tableClass.isDataValid()) {
                checking =  this.getClass().getResource(positive);
                isDataValid = true;
            } else {
                checking =  this.getClass().getResource(negativePath);
                isDataValid = false;
            }
            Image image = new Image(checking.toString());
            view.setImage(image);
        }).start();
    }


    private class ButtonBackPress implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            if (stage.ordinal()==0) {
                return;
            } else {
                stage = MainFormStage.values()[stage.ordinal()-1];
                changeStage(stage);
            }
        }
    }

    private class ButtonNextPress implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            if (stage.ordinal() >= MainFormStage.values().length - 1) {
                ///TODO обработчик запуска в работу
                return;
            } else if (stage == MainFormStage.CHECK_INPUT) {
                if (!isDataValid) {
                    stage = MainFormStage.CHECK_INPUT;
                } else {
                    stage = MainFormStage.values()[stage.ordinal() + 1];
                    changeStage(stage);
                }
            } else {
                stage = MainFormStage.values()[stage.ordinal() + 1];
                changeStage(stage);
            }
        }
    }















}
