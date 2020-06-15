package com.npn.javafx.controller.uicontroller;

        import com.npn.javafx.Updater;
        import com.npn.javafx.model.MainFormStage;
        import com.npn.javafx.ui.ArchiveItemTableView;
        import com.npn.javafx.ui.TableFileItem;
        import javafx.application.Platform;
        import javafx.event.ActionEvent;
        import javafx.event.EventHandler;
        import javafx.fxml.FXML;
        import javafx.scene.control.*;
        import javafx.scene.layout.AnchorPane;
        import javafx.stage.Stage;


        import java.io.File;
        import java.io.IOException;
        import java.util.List;
        import java.util.Locale;
        import java.util.ResourceBundle;
        import java.util.stream.Collectors;

public class UIMainFormController {
    private File openFileDialogIniFolder = Updater.JAR_FILE_PATH.toFile();
    private MainFormStage stage = MainFormStage.SELECT_BASE_PATH;
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("ui.uimainlocale",
            Locale.getDefault());

    private String basePath = "";

    private Stage mainWindows;

    private boolean filesListIsValid = false;

    private volatile boolean isDataValid = false;

    private TableFileItem[] tableItems = new TableFileItem[0];

    private UIHeaderController headerController;

    private List<UIMainChildAbstractController> childControllers;

    private List<ArchiveItemTableView.ArchiveObject> archiveItemsList = null;

    public UIMainFormController() {
    }

    @FXML
    private Button buttonBack;

    @FXML
    private Button buttonNext;

    @FXML
    private Button buttonExit;

    @FXML
    private AnchorPane mainAnchorPanel;

    @FXML
    private AnchorPane headerPane;

    /**
     * Возвращает путь к файлу FXML для закрузки элемента
     *
     * @return путь к файлу FXML
     */
    public static String getFXMLPath() {
        return "/ui/UIMainForm.fxml";
    }


    /**
     * Инициализация mainWindows
     * @param mainWindows ссылка на основное окно
     */
    public void init(Stage mainWindows, UIHeaderController headerController, List<UIMainChildAbstractController> childControllers) throws IOException {

        this.headerController = headerController;
        this.childControllers = childControllers;




        this.mainWindows = mainWindows;
        changeStage(MainFormStage.SELECT_BASE_PATH);
        buttonExit.setOnAction(e-> Platform.exit());

        buttonBack.setOnAction(new ButtonBackPress());
        buttonNext.setOnAction(new ButtonNextPress());

        headerPane.getChildren().add(headerController.getNode());

        mainAnchorPanel.getChildren().addAll(childControllers.stream().map(UIMainChildAbstractController::getNode).collect(Collectors.toList()));
        changeStage(stage);

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

    public boolean isDataValid() {
        return isDataValid;
    }

    public void setDataValid(boolean dataValid) {
        isDataValid = dataValid;
    }

    public boolean isFilesListIsValid() {
        return filesListIsValid;
    }

    public void setFilesListIsValid(boolean filesListIsValid) {
        this.filesListIsValid = filesListIsValid;
    }

    public List<ArchiveItemTableView.ArchiveObject> getArchiveItemsList() {
        return archiveItemsList;
    }

    public void setArchiveItemsList(List<ArchiveItemTableView.ArchiveObject> archiveItemsList) {
        this.archiveItemsList = archiveItemsList;
    }

    public TableFileItem[] getTableItems() {
        return tableItems;
    }

    public void setTableItems(TableFileItem[] tableItems) {
        this.tableItems = tableItems;
    }

    public void changeStage(MainFormStage stage) {
        if (stage==null) {
            throw new IllegalArgumentException();
        }
        headerController.setStage(stage);
        childControllers.forEach(x->x.setStage(stage));
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
                if (childControllers.stream().filter(x->x.getFormStage().ordinal()<=stage.ordinal()).allMatch(UIMainChildAbstractController::isValid)) {
                    stage = MainFormStage.values()[stage.ordinal() + 1];
                    changeStage(stage);
                }
            }
        }
    }















}
