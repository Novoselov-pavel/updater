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
        import javafx.scene.control.*;
        import javafx.scene.input.KeyEvent;
        import javafx.scene.paint.Color;
        import javafx.scene.text.Font;
        import javafx.scene.text.FontPosture;
        import javafx.scene.text.Text;
        import javafx.scene.text.TextFlow;
        import javafx.stage.DirectoryChooser;
        import javafx.stage.Stage;


        import java.io.File;
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
    private Path basePath;
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



    public void init(Stage mainWindows) {
        this.mainWindows = mainWindows;
        changeStage(MainFormStage.SELECT_BASE_PATH);
        buttonExit.setOnAction(e-> Platform.exit());
        FileItemTableView table = new FileItemTableView(fileTable,resourceBundle);
        table.init();
        tableClass = table;
        buttonBack.setOnAction(new ButtonBackPress());
        buttonNext.setOnAction(new ButtonNextPress());
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
        }
    }

    private void firstStageView() {
        fileTable.setVisible(false);
        addDirToFileTable.setVisible(false);
        addFileToFileTable.setVisible(false);

        Text text = new Text(resourceBundle.getString("SELECT_BASE_PATH"));
        text.setFill(Color.BLACK);
        text.setFont(Font.font("sans-serif", FontPosture.ITALIC,15));
        ObservableList list = textFlow.getChildren();
        list.clear();
        list.add(text);
        selectPathButton.setVisible(true);
        textPathArray.setVisible(true);
        textPathArray.addEventHandler(KeyEvent.KEY_RELEASED,new TextAreaCheck(IsStringDirPath::test));
    }

    private void secondStage() {
        selectPathButton.setVisible(false);
        textPathArray.setVisible(false);


        fileTable.setVisible(true);
        addDirToFileTable.setVisible(true);
        addFileToFileTable.setVisible(true);
    }

    public void selectBasePath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(resourceBundle.getString("SELECT_BASE_PATH"));
        directoryChooser.setInitialDirectory(IsStringDirPath.test(textPathArray.getText())? Paths.get(textPathArray.getText()).toFile(): openFileDialogIniFolder);
        File selectedDir = directoryChooser.showDialog(mainWindows);
        if (selectedDir!=null) {
            basePath = selectedDir.toPath();
            textPathArray.setText(basePath.toString());
        }
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
            if (stage.ordinal()>=MainFormStage.values().length-1) {
                return;
            } else {
                stage = MainFormStage.values()[stage.ordinal()+1];
                changeStage(stage);
            }
        }
    }















}
