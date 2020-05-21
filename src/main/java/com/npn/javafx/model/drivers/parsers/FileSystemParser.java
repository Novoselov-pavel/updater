package com.npn.javafx.model.drivers.parsers;

import com.npn.javafx.model.interfaces.FilesParser;
import com.npn.javafx.model.interfaces.VersionsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**Класс парсер версий и файлов из файловой системы
 *
 */
public class FileSystemParser implements FilesParser, VersionsParser {
    private static final Logger logger = LoggerFactory.getLogger(FileSystemParser.class);

    /**
     * Получает список адресов файлов которые требуется скачать из папки с версией
     *
     * @param path адрес папки с версией
     * @return список адресов
     * @throws Exception
     */
    @Override
    public List<String> getFilesAddress(final String path) throws Exception {
        String logFormat = "getFilesAddress from\t%s";
        logger.debug(String.format(logFormat,path));

        List<String> list = new ArrayList<>();

        logFormat = "Start search files from\t%s";
        logger.info(String.format(logFormat,path));

        Path inputDir = Paths.get(path);
        if (!Files.exists(inputDir) || !(Files.isDirectory(inputDir) || Files.isSymbolicLink(inputDir))) {
            logFormat = "Search files error from\t%s\tAddress isn't valid";
            logger.error(String.format(logFormat,path));
            throw new IllegalArgumentException("Address isn't valid");
        }

        Files.walk(inputDir).forEach(x->{
            if (!inputDir.equals(x))
                list.add(x.toAbsolutePath().toString());
        });


        logFormat = "End search files from\t%s";
        logger.info(String.format(logFormat,path));
        return list;
    }

    /**
     * Получает список версий по указанному адресу (сканирует и выдает список всех папок в папке)
     *
     * @param path адрес где располагается список версий (папка)
     * @return List со списком версий
     */
    @Override
    public List<String> getVersion(final String path) throws Exception {
        String logFormat = "getVersion from\t%s";
        logger.debug(String.format(logFormat,path));

        List<String> list = new ArrayList<>();

        logFormat = "Start get version folder from\t%s";
        logger.info(String.format(logFormat,path));

        Path inputDir = Paths.get(path);
        if (!Files.exists(inputDir) || !(Files.isDirectory(inputDir) || Files.isSymbolicLink(inputDir))) {
            logFormat = "Version's directory search error from\t%s\tAddress isn't valid";
            logger.error(String.format(logFormat,path));
            throw new IllegalArgumentException("Address isn't valid");
        }

        for (File files : inputDir.toFile().listFiles(x-> Files.isDirectory(x.toPath())||Files.isSymbolicLink(x.toPath()))) {
            list.add(files.toPath().toString());
        }

        logFormat = "End get version folder from\t%s";
        logger.info(String.format(logFormat,path));
        return list;
    }

    /**
     * Получает адрес где размещается указанная версия
     *
     * @param version номер версии
     * @param path    адрес размещения всех версий
     * @return адрес расположения файлов с версиями
     * @throws Exception при ошибке
     */
    @Override
    public String getAddress(final String version, final String path) throws Exception {
        String logFormat = "getAddress version\t%s\tfrom\t%s";
        logger.debug(String.format(logFormat,version,path));


        logFormat = "Start get folder to version\t%s\tfrom\t%s";
        logger.info(String.format(logFormat,version,path));

        Path retVal = Paths.get(path).resolve(version);
        if (Files.isDirectory(retVal) || Files.isSymbolicLink(retVal)) {
            logFormat = "End get folder to version\t%s\tfrom\t%s";
            logger.info(String.format(logFormat,version,path));
            return retVal.toAbsolutePath().toString();
        } else {
            logFormat = "Directory search for version\t%s\terror in dir\t%s\tAddress not found";
            logger.error(String.format(logFormat,version,path));
            throw new IllegalArgumentException("Address not found");
        }


    }
}
