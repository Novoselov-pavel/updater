package com.npn.javafx.model.drivers.parsers;

import com.npn.javafx.model.interfaces.FilesParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**Класс для получения всех файлов и подпапок из папки
 *
 */
public class FileSystemDirParse implements FilesParser {
    private static final Logger logger = LoggerFactory.getLogger(FileSystemDirParse.class);


    /**
     * Получает список путей файлов (вкючая подпапки и файлы из них) из папки
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

        logFormat = "Start search files in dir\t%s";
        logger.info(String.format(logFormat,path));

        Path inputPath = Paths.get(path);
        Files.walk(inputPath).forEach(x->{
            if (!x.equals(inputPath)) {
                list.add(x.toString());
            }
        });

        logFormat = "End search files in dir\t%s";
        logger.info(String.format(logFormat,path));
        return list;
    }
}
