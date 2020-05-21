package com.npn.javafx.controller;

import com.npn.javafx.model.Setting;
import com.npn.javafx.model.drivers.PropertiesXmlDriver;
import com.npn.javafx.model.interfaces.PropertiesLoader;
import com.npn.javafx.model.interfaces.PropertiesValidator;
import com.npn.javafx.model.validators.PropertiesValidatorByEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Контроллер работы от параметров командной строки
 */
public class BashController {
    private static final Logger logger = LoggerFactory.getLogger(BashController.class);
    private final String[] args;

    public BashController(String[] args) {
        this.args = args;
    }

    public void execute () throws Exception {
        logger.debug("execute");
        logger.info("Start executing with command line option");

        if (args==null && args.length<1) {
            throw new IllegalArgumentException("");
        }

        if (isFileName(args[1])) {
            //TODO
        }


    }


    private boolean isFileName(String s) {
        try {
            Path path = Paths.get(s);
            return Files.exists(path);
        } catch (Exception ignore) {
            return  false;
        }
    }

    /**Запускает файл на обновление
     *
     * @param path
     */
    private void executeInputFile(String path) throws Exception {
        PropertiesLoader loader = new PropertiesXmlDriver();
        PropertiesValidator validator = new PropertiesValidatorByEnum();
        Setting setting = Setting.loadProperties(loader,validator,path);





        ///TODO тут остановился

    }




}
