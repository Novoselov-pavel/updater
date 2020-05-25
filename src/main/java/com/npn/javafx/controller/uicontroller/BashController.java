package com.npn.javafx.controller.uicontroller;

import com.npn.javafx.model.Setting;
import com.npn.javafx.model.Version;
import com.npn.javafx.model.drivers.PropertiesXmlDriver;
import com.npn.javafx.model.interfaces.PropertiesLoader;
import com.npn.javafx.model.interfaces.PropertiesValidator;
import com.npn.javafx.model.interfaces.VersionsParser;
import com.npn.javafx.model.validators.PropertiesValidatorByEnum;
import com.npn.javafx.ui.UiConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

/**
 * Контроллер работы от параметров командной строки
 */
public class BashController {
    private static final Logger logger = LoggerFactory.getLogger(BashController.class);
    protected static final String NEW_VERSION_NOT_FOUND_ANSWER = "no";


    private final String[] args;

    public BashController(String[] args) {
        this.args = args;
    }

    public String execute () throws Exception {
        logger.debug("execute");
        logger.info("Start executing with command line option");

        if (args==null && args.length<1) {
            throw new IllegalArgumentException("BashController input parameter error");
        }

        if (isFileName(args[0])) {
            return search(args[0]);
        } else {
            ///TODO
        }

        return null;


    }


    private boolean isFileName(String s) {
        try {
            Path path = Paths.get(s);
            return Files.exists(path);
        } catch (Exception ignore) {
            return  false;
        }
    }

    /**Запускает поиск последней версии (I режим работы программы)
     *
     * @param path путь к файлу с настройками
     * @return номер версии или {@link BashController#NEW_VERSION_NOT_FOUND_ANSWER}
     */
    private String search(String path) throws Exception {
        logger.debug("search");
        logger.info("Start search last version from file\t{}",path);

        PropertiesLoader loader = new PropertiesXmlDriver();
        PropertiesValidator validator = new PropertiesValidatorByEnum();
        Setting setting = Setting.loadSetting(loader,validator,path);

        VersionsParser parser = setting.getVersionParser();
        List<String> versionsStringList = parser.getVersion(setting.getLocation());
        List<Version> versionsList = Version.getVersionsListFromStrings(versionsStringList);


        if (versionsList.size()>0) {
            versionsList.sort(Comparator.naturalOrder());
            Version lastVersion = versionsList.get(versionsList.size()-1);
            int compare = setting.getVersion().compareTo(lastVersion);

            if (compare<0) {

                logger.info("Last version \t{}",lastVersion.toString());
                UiConsole.println(lastVersion.toString());
                return lastVersion.toString();
            } else {
                logger.info("There isn't new version in destination");

                UiConsole.println(NEW_VERSION_NOT_FOUND_ANSWER);
                return NEW_VERSION_NOT_FOUND_ANSWER;
            }
        }
        logger.info("End search last version from file\t{}",path);
        return null;
    }


    /**
     * Запускает обновление до последней версии (II режим работы программы)
     * @param version номер версии до которой обновляемся или "auto" для автоопределения
     * @param path путь к файлу настроек
     */
    private void updateVersion(String version, String path) {
        ///TODO не забыть везде написать перевод в правильную кодировку из консоли!!!!
    }



}
