package com.npn.javafx;

import com.npn.javafx.controller.uicontroller.BashController;
import com.npn.javafx.controller.uicontroller.UIMainFormController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.ResourceBundle;

public class Updater extends Application {
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("uilocale", Locale.getDefault());

    private static final Logger logger = LoggerFactory.getLogger(Updater.class);
    public static final Path JAR_FILE_PATH;
    static {
        Path path = null;
        try {
            URI uri = Updater.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            path = Paths.get(uri);
        } catch (URISyntaxException e) {
           String s = Updater.class.getProtectionDomain().getCodeSource().getLocation().getPath();
           path = Paths.get(s);
        }
        logger.info("Base path set to {}",path.toString());
        JAR_FILE_PATH = path;
    }

    public static void main(String[] args) {
        if (args!=null && args.length!=0) {
            BashController controller = new BashController(args);
            try {
                controller.execute();
                System.exit(0);
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
                System.exit(1);
            }
        } else {
            launch(args);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        URL xmlUrl = getClass().getResource("/ui/UIMainForm.fxml");
        FXMLLoader loader = new FXMLLoader(xmlUrl,resourceBundle);
        try {
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root));
            UIMainFormController controller = loader.getController();
            controller.init(primaryStage);
            primaryStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
