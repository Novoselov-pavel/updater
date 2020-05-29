package com.npn.javafx.controller.uicontroller;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.npn.javafx.model.IniClass;
import com.npn.javafx.model.Setting;
import com.npn.javafx.model.Version;
import com.npn.javafx.model.drivers.PropertiesXmlDriver;
import com.npn.javafx.model.drivers.URLFileLoad;
import com.npn.javafx.model.exception.FailUpdateFiles;
import com.npn.javafx.model.interfaces.FilesParser;
import com.npn.javafx.model.interfaces.PropertiesLoader;
import com.npn.javafx.model.interfaces.PropertiesValidator;
import com.npn.javafx.model.interfaces.VersionsParser;
import com.npn.javafx.model.validators.PropertiesValidatorByEnum;
import com.npn.javafx.ui.UiConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;

/**
 * Контроллер работы от параметров командной строки
 */
public class BashController {
    private static final Logger logger = LoggerFactory.getLogger(BashController.class);
    protected static final String NEW_VERSION_NOT_FOUND_ANSWER = "no";
    protected static final String AUTO_MODE_CONSOLE_PARAMETER = "no";


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

        List<Version> versionsList = Version.getVersionsFromSettings(setting);


        if (versionsList.size()>0) {
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
    private void updateVersion(final String version, final String path) throws Exception {
        String utfVersion = convertConsoleString(version);
        if (AUTO_MODE_CONSOLE_PARAMETER.equalsIgnoreCase(utfVersion)) {
            utfVersion = search(path);
        }
        Version neededVersion = new Version(utfVersion);

        PropertiesLoader loader = new PropertiesXmlDriver();
        PropertiesValidator validator = new PropertiesValidatorByEnum();
        Setting setting = Setting.loadSetting(loader,validator,path);

        FilesParser filesParser = setting.getFileParser();
        List<String> filesAddress = filesParser.getFilesAddress(setting.getVersionParser().getAddress(neededVersion.toString(),
                setting.getLocation()));

        Map<Path, Path> files = URLFileLoad.loadFiles(filesAddress);

        Path iniFile = IniClass.getIniFile(setting,files);

        if (iniFile == null) {
            throw new FailUpdateFiles("Ini file not found", null);
        }

        IniClass iniClass = IniClass.loadFromXmlFile(iniFile);

        List<String> failedCRCFiles = IniClass.checkCRCStage(iniClass,files);

        if (failedCRCFiles.size()>0) {
            Map<Path, Path> reDownloadFiles = URLFileLoad.loadFiles(failedCRCFiles);
            files.putAll(reDownloadFiles);
            failedCRCFiles = IniClass.checkCRCStage(iniClass,files);
            if (failedCRCFiles.size()>0) {
                throw new  FailUpdateFiles("Files check is failed", null);
            }
        }






        ///TODO подключить функцию IniClass.proceedIniFile



    }









    /**
     * Перекодирует строку из консольной кодировки в UTF-8
     * Примечание: в связи с тем что Windows упрямо выдает неверную кодировку своей консоли, метод не гарантирует правильность
     * работы на конкретной системе, требуется тестирование под операционнуб систему
     *
     * @param string исходная строка
     * @return строка в кодировке UTF-8
     */
    private String convertConsoleString(final String string) throws UnsupportedEncodingException {
        String retVal = new String(string.getBytes(Setting.getConsoleCharset()), StandardCharsets.UTF_8);
        return retVal;
    }





}
