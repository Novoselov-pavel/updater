package com.npn.javafx.model.drivers;

import com.npn.javafx.model.exception.FailUpdateFiles;
import com.npn.javafx.model.exception.SafeCopyFilesException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**Безопасное копирование файлов согласно Map ключ - откуда копировать файл, значение - куда копировать файл
 *
 */
public class SafeCopyFiles extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(SafeCopyFiles.class);

    private final Map<Path,Path> files;

    ///Задержка при попытке неудачного копирования (мс)
    private static final int DELAY = 20000;

    ///Максимальное количество попыток
    private static final int MAX_ITERATION = 5;

    /**Конструктор безопасного копирования файлов из адресов исходного списка в адреса итогового списка.
     *
     * @param files Map ключ - откуда копировать файл, значение - куда копировать файл
     */
    public SafeCopyFiles(final Map<Path,Path> files) {
        if (files == null) {
            throw new IllegalArgumentException("SafeCopy's input map is null");
        }
        this.files = files;
    }

    /** Безопасное копирование файлов из адресов исходного списка в адреса итогового списка.
     *
     * @throws SafeCopyFilesException,  при ошибке копирования     *
     */
    @Override
    public void run() {
        String logFormat = "SafeCopyFiles start";
        logger.debug(logFormat);
        Map<Path,Path> tempMap = null;

        logFormat = "Start copy files";
        logger.info(logFormat);

        try{
            for (int i = 0; i <MAX_ITERATION; i++) {
                if (i!=0)
                    Thread.sleep(DELAY);

                try{
                    tempMap = createTempMap(files);
                    copyIfExist(tempMap);

                } catch (IOException e) {
                    if (i==MAX_ITERATION-1) {
                        throw new FailUpdateFiles("Error at creating temp files", e);
                    }
                    continue;
                }

                if (isInterrupted())
                    throw new InterruptedException();

                try {
                    copyFiles(files);
                } catch (IOException e) {
                    if (i==MAX_ITERATION-1) {
                        try {
                            copyIfExist(tempMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue,Map.Entry::getKey)));
                        } catch (IOException rollbackException) {
                            throw new SafeCopyFilesException("Fatal Error on safe copy files\nOriginal files:\n" + pathMapToString(tempMap),rollbackException);
                        }
                        throw new FailUpdateFiles("Error at copy temp files", e);
                    }
                    continue;
                }

                logFormat = "End copy files";
                logger.info(logFormat);
                logFormat = "SafeCopyFiles ended";
                logger.debug(logFormat);
                break;

            }

        } catch (InterruptedException e) {
            deleteFiles(tempMap.values());
            logFormat = "SafeCopyFiles interrupted";
            logger.debug(logFormat);
            return;
        }

        logFormat = "SafeCopyFiles end";
        logger.debug(logFormat);
    }

    /**Создает список путей для временных файлов
     *
     * @param map Map для создания копии заменяемых файлов
     * @return Map где ключ - исходные файлы, значение - временные файлы
     * @throws IOException
     * @throws InterruptedException
     */
    private Map<Path,Path> createTempMap(final Map<Path, Path> map) throws IOException, InterruptedException {
        String logFormat = "createTempList start";
        logger.debug(logFormat);
        if (!isInterrupted()) {
            Map<Path,Path> retMap = new HashMap<>();

                for (Path sourceFile : map.values()) {
                    Path path = Files.createTempFile(sourceFile.getFileName().toString(),null);
                    retMap.put(sourceFile,path);
                }
             logFormat = "createTempList end";
             logger.debug(logFormat);
             return retMap;
        }
        throw new InterruptedException();
    }


    /**Копирует файлы из адресов исходного списка в адреса итогового списка.
     *
     * @param files Map ключ - откуда копировать файл, значение - куда копировать файл
     * @return true если копирование прошло успешно.
     */
    private void copyFiles(final Map<Path,Path> files) throws IOException {
        String logFormat = "copyFiles start";
        logger.debug(logFormat);

        if (files == null) {
            throw new IllegalArgumentException();
        }

        for (Map.Entry<Path, Path> entry : files.entrySet()) {
            copyFile(entry.getKey(),entry.getValue());
        }

        logFormat = "copyFiles end";
        logger.debug(logFormat);
    }

    /**Копирует один файл
     */
    private void copyFile(Path source, Path destination) throws IOException {
        String logFormat = "Copy file\t%s\tto\t%s";
        logger.info(String.format(logFormat,source.toString(), destination.toString()));
        FileUtils.copyFile(source.toFile(),destination.toFile());
    }


    /**Переводит список путей в строку
     *
     * @param map files Map ключ - откуда копировать файл, значение - куда копировать файл
     * @return
     */
    private String pathMapToString(final Map<Path,Path> map) {
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<Path, Path> entry : map.entrySet()) {
            builder.append("old file: ").append(entry.getKey()).append("\t").append("saved copy: ").append(entry.getValue()).append("\n");
        }
        return builder.toString();
    }

    /**Удаляет все файлы по списку     *
     */
    private void deleteFiles(Collection<Path> list) {
        if (list==null) return;

        for (Path path : list) {
            FileUtils.deleteQuietly(path.toFile());
        }
    }

    private void copyIfExist(final Map<Path,Path> files) throws IOException {

        Map<Path,Path> map = files.entrySet().stream().filter(x->Files.exists(x.getKey())).collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
        if (map.size() ==0) return;

        copyFiles(map);
    }

}
