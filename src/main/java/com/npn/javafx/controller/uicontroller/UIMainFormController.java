package com.npn.javafx.controller.uicontroller;

        import com.npn.javafx.model.MainFormStage;
        import javafx.collections.ObservableList;
        import javafx.fxml.FXML;
        import javafx.scene.control.Button;
        import javafx.scene.control.ButtonBar;
        import javafx.scene.control.ListView;
        import javafx.scene.control.TextArea;
        import javafx.scene.paint.Color;
        import javafx.scene.text.Font;
        import javafx.scene.text.FontPosture;
        import javafx.scene.text.Text;
        import javafx.scene.text.TextFlow;
        import javafx.stage.FileChooser;
        import javafx.stage.Stage;

        import java.net.URL;
        import java.net.URLClassLoader;
        import java.util.Locale;
        import java.util.ResourceBundle;


public class UIMainFormController {
    private MainFormStage stage = MainFormStage.SELECT_BASE_PATH;
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("ui.uimainlocale",
            Locale.getDefault());
    private Stage mainWindows;

    public UIMainFormController() {
    }

    @FXML
    private TextFlow textFlow;

    @FXML
    private Button selectPathButton;

    @FXML
    private TextArea textPathArray;

    @FXML
    private ListView<?> listView;

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

    public void init(Stage mainWindows) {
        this.mainWindows = mainWindows;
        changeStage(MainFormStage.SELECT_BASE_PATH);
    }

    public void changeStage(MainFormStage stage) {
        if (stage==null) {
            throw new IllegalArgumentException();
        }
        if (stage==MainFormStage.SELECT_BASE_PATH) {
            firstStageView();
        }
    }

    private void firstStageView() {
        Text text = new Text(resourceBundle.getString("SELECT_BASE_PATH"));
        text.setFill(Color.BLACK);
        text.setFont(Font.font("sans-serif", FontPosture.ITALIC,15));
        ObservableList list = textFlow.getChildren();
        list.add(text);
        selectPathButton.setVisible(true);
        textPathArray.setVisible(true);
        listView.setVisible(false);
    }

    public void selectBasePath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(resourceBundle.getString("SELECT_BASE_PATH"));
        fileChooser.showOpenDialog(mainWindows);
    }







}
