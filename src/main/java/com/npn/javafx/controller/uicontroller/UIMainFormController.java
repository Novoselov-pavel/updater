package com.npn.javafx.controller.uicontroller;

        import com.npn.javafx.Updater;
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
        import java.util.concurrent.Callable;
        import java.util.concurrent.ExecutionException;
        import java.util.concurrent.FutureTask;


public class UIMainFormController {
    private File openFileDialogIniFolder = Updater.JAR_FILE_PATH.toFile();
    private MainFormStage stage = MainFormStage.SELECT_BASE_PATH;
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("ui.uimainlocale",
            Locale.getDefault());
    private Stage mainWindows;
    private FileItemTableView tableClass = null;

    public UIMainFormController() {
    }

    @FXML
    private TextFlow textFlow;

    @FXML
    private Button selectPathButton;

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
    private Button buttonOk;

    @FXML
    private Button buttonExit;

    @FXML
    private TableView<TableFileItem> fileTable;

    @FXML
    private Button addDirToFileTable;

    @FXML
    private Button addFileToFileTable;

    @FXML
    private AnchorPane mainAnchorPanel;

    @FXML
    private ImageView currentStageImage;

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


        textPathArray.addEventHandler(KeyEvent.KEY_RELEASED,new TextAreaCheck(IsStringDirPath::test));

        ///TODO Добавить обработку событий для кнопок addDirToFileTable addFileToFileTable
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
        }
    }

    private void firstStageView() {
        fileTable.setVisible(false);
        addDirToFileTable.setVisible(false);
        addFileToFileTable.setVisible(false);
        currentStageImage.setVisible(false);

        Text text = new Text(resourceBundle.getString("SELECT_BASE_PATH"));
        text.setFill(Color.BLACK);
        text.setFont(Font.font("sans-serif", FontPosture.ITALIC,15));
        ObservableList<Node> list = textFlow.getChildren();
        list.clear();
        list.add(text);
        selectPathButton.setVisible(true);
        textPathArray.setVisible(true);

    }

    private void secondStage() {
        selectPathButton.setVisible(false);
        textPathArray.setVisible(false);
        currentStageImage.setVisible(false);

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
        currentStageImage.setVisible(true);

        URL checking =  this.getClass().getResource("/ui/pics/checking.png");
        try {
            Image image = new Image(checking.toString());
            currentStageImage.setImage(image);
        } catch (Exception ignored) {
        }
        boolean isCorrectData= false;
        try {
            isCorrectData = checkTableClassData();
        } catch (InterruptedException | ExecutionException ignored){}

        if (isCorrectData) {
            checking =  this.getClass().getResource("/ui/pics/ok.png");
        } else {
            checking =  this.getClass().getResource("/ui/pics/fail.png");
        }
        Image image = new Image(checking.toString());
        currentStageImage.setImage(image);

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

    private boolean checkTableClassData() throws InterruptedException, ExecutionException {
        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ///TODO переделать это для асинхронного выполнения
                return tableClass.isDataValid();
            }
        };
        FutureTask<Boolean> futureTask = new FutureTask<>(callable);
        new Thread(futureTask).start();
        Thread.sleep(1000);
        return futureTask.get();
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
            } else {
                stage = MainFormStage.values()[stage.ordinal() + 1];
                changeStage(stage);
            }
        }
    }















}
