package com.npn.javafx.controller.uicontroller;

        import com.npn.javafx.Updater;
        import com.npn.javafx.ui.ArchiveItemTableView;
        import com.npn.javafx.ui.FileItemTableView;
        import com.npn.javafx.model.MainFormStage;
        import javafx.application.Platform;
        import javafx.event.ActionEvent;
        import javafx.event.EventHandler;
        import javafx.fxml.FXML;
        import javafx.scene.control.*;
        import javafx.scene.image.Image;
        import javafx.scene.image.ImageView;
        import javafx.scene.layout.AnchorPane;
        import javafx.stage.Stage;


        import java.io.File;
        import java.net.URL;
        import java.util.Locale;
        import java.util.ResourceBundle;

public class UIMainFormController {
    private File openFileDialogIniFolder = Updater.JAR_FILE_PATH.toFile();
    private MainFormStage stage = MainFormStage.SELECT_BASE_PATH;
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("ui.uimainlocale",
            Locale.getDefault());

    private String basePath = "";

    private Stage mainWindows;
    private FileItemTableView tableClass = null;
    private ArchiveItemTableView archiveTableClass = null;
    private volatile boolean isDataValid = false;

    public UIMainFormController() {
    }

    @FXML
    private Button buttonBack;

    @FXML
    private Button buttonNext;

    @FXML
    private Button buttonExit;

    @FXML
    private TableView<ArchiveItemTableView.ArchiveObject> packTable;

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

        buttonBack.setOnAction(new ButtonBackPress());
        buttonNext.setOnAction(new ButtonNextPress());

        ArchiveItemTableView archiveItemTable = new ArchiveItemTableView(packTable,resourceBundle);
        archiveItemTable.init();
        archiveTableClass = archiveItemTable;


    }
    public File getOpenFileDialogIniFolder() {
        return openFileDialogIniFolder;
    }

    public void setOpenFileDialogIniFolder(File openFileDialogIniFolder) {
        this.openFileDialogIniFolder = openFileDialogIniFolder;
    }


    public Stage getMainWindows() {
        return mainWindows;
    }

    public void setMainWindows(Stage mainWindows) {
        this.mainWindows = mainWindows;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
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
        currentStageImage.setVisible(false);
        packTable.setVisible(false);

    }

    private void secondStage() {
        currentStageImage.setVisible(false);
        packTable.setVisible(false);

    }


    private void thirdStage() {
        packTable.setVisible(false);

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
        currentStageImage.setVisible(false);


        packTable.setVisible(true);
        archiveTableClass.addAllArchiveObject(tableClass.getTableFileItems());
    }

    private void fifthStage() {
        currentStageImage.setVisible(false);

        packTable.setVisible(false);

        ///TODO
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
