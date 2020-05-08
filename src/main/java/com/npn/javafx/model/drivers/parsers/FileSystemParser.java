package com.npn.javafx.model.drivers.parsers;

import com.npn.javafx.model.interfaces.FilesParser;
import com.npn.javafx.model.interfaces.VersionsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public List<String> getFilesAddress(String path) throws Exception {
        String logFormat = "getFilesAddress from\t%s";
        logger.debug(String.format(logFormat,path));

        logFormat = "Start search files from\t%s";
        logger.info(String.format(logFormat,path));

        Path inputDir = Paths.get(path);
        if (!Files.exists(inputDir) || (Files.isDirectory(inputDir) || Files.isSymbolicLink(inputDir))) {
            logFormat = "Search files error from\t%s\tAddress isn't valid";
            logger.error(String.format(logFormat,path));
            throw new IllegalArgumentException("Address isn't valid");
        }

        ///TODO

        return null;
    }

    /**
     * Получает список версий по указанному адресу
     *
     * @param path адрес где располагается список версий
     * @return List со списком версий
     */
    @Override
    public List<String> getVersion(String path) throws Exception {
        return null;
    }

    /**
     * Получает адрес где размещается список версий
     *
     * @param version номер версии
     * @param path    адрес размещения всех версий
     * @return адрес расположения файлов с версиями
     * @throws Exception при ошибке
     */
    @Override
    public String getAddress(String version, String path) throws Exception {
        return null;
    }
}
