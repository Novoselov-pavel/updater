package com.npn.javafx;

import com.npn.javafx.controller.uicontroller.BashController;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Updater extends Application {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            launch(args);
        }
    }

    @Override
    public void start(Stage primaryStage) {

    }
}
