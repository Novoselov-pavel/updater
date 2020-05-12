package com.npn.javafx.model.drivers;

import com.npn.javafx.model.exception.FailUpdateFiles;
import com.npn.javafx.model.exception.SafeCopyFilesException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**Безопасное копирование файлов из адресов исходного списка в адреса итогового списка.
 * ИНДЕКСЫ СООТВЕСТВУЮЩИХ ФАЙЛОВ ДОЛЖНЫ СОВПАДАТЬ В ПЕРЕДАННЫХ В КОНСТРУКТОРЕ СПИСКАХ
 *
 */
public class SafeCopyFiles extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(SafeCopyFiles.class);

    private final List<Path> sourceFiles;
    private final List<Path> destinationFiles;

    ///Задержка при попытке неудачного копирования (мс)
    private static final int DELAY = 20000;

    ///Максимальное количество попыток
    private static final int MAX_ITERATION = 5;

    /**Конструктор безопасного копирования файлов из адресов исходного списка в адреса итогового списка.
     *
     * @param sourceFiles исходный список, индексы соответсвующих файлов исходного списка и итогового должны совпадать.
     * @param destinationFiles итоговый список файлов.
     */
    public SafeCopyFiles(final List<Path> sourceFiles,final List<Path> destinationFiles) {
        if (sourceFiles == null || destinationFiles == null || sourceFiles.size()!=destinationFiles.size()) {
            throw new IllegalArgumentException("SafeCopy's input lists is null or different size");
        }
        this.sourceFiles = sourceFiles;
        this.destinationFiles = destinationFiles;
    }

    /** Безопасное копирование файлов из адресов исходного списка в адреса итогового списка.
     *
     * @throws SafeCopyFilesException,  при ошибке копирования
     * @see #start()
     * @see #stop()
     */
    @Override
    public void run() {
        String logFormat = "SafeCopyFiles start";
        logger.debug(logFormat);
        List<Path> tempList = null;

        logFormat = "Start copy files";
        logger.info(logFormat);

        try{
            for (int i = 0; i <MAX_ITERATION; i++) {
                if (i!=0)
                    Thread.sleep(DELAY);

                try{
                    tempList = createTempList(destinationFiles);
                    copyFiles(destinationFiles,tempList);
                } catch (IOException e) {
                    if (i==MAX_ITERATION-1) {
                        throw new FailUpdateFiles("Error at creating temp files", e);
                    }
                    continue;
                }

                if (isInterrupted())
                    throw new InterruptedException();

                try {
                    copyFiles(sourceFiles,destinationFiles);
                } catch (IOException e) {
                    if (i==MAX_ITERATION-1) {
                        try {
                            copyFiles(tempList,destinationFiles);
                        } catch (IOException rollbackException) {
                            throw new SafeCopyFilesException("Fatal Error on safe copy files\nOriginal files:\n" + pathListToString(tempList),rollbackException);
                        }
                        throw new FailUpdateFiles("Error at copy temp files", e);
                    }
                    continue;
                }

                logFormat = "End copy files";
                logger.info(logFormat);
                logFormat = "SafeCopyFiles ended";
                logger.debug(logFormat);

            }

        } catch (InterruptedException e) {
            deleteFiles(tempList);
            logFormat = "SafeCopyFiles interrupted";
            logger.debug(logFormat);
            return;
        }

        logFormat = "SafeCopyFiles end";
        logger.debug(logFormat);
    }

    /**Создает список путей для временных файлов
     *
     * @param sourceFiles список исходных файлов
     * @return список
     * @throws IOException
     * @throws InterruptedException
     */
    private List<Path> createTempList(final List<Path> sourceFiles) throws IOException, InterruptedException {
        String logFormat = "createTempList start";
        logger.debug(logFormat);
        if (!isInterrupted()) {
            List<Path> returnList = new ArrayList<>();
                for (Path sourceFile : sourceFiles) {
                    Files.createTempFile(sourceFile.getFileName().toString(),null);
                }
             logFormat = "createTempList end";
             logger.debug(logFormat);
             return returnList;
        }
        throw new InterruptedException();
    }


    /**Копирует файлы из адресов исходного списка в адреса итогового списка.
     *
     * @param sourceFiles исходный список, индексы соответсвующих файлов исходного списка и итогового должны совпадапть.
     * @param destinationFiles итоговый список файлов.
     * @return true сли копирование прошло успешно.
     */
    private void copyFiles(final List<Path> sourceFiles,final List<Path> destinationFiles) throws IOException {
        String logFormat = "copyFiles start";
        logger.debug(logFormat);

        if (sourceFiles == null || (destinationFiles!=null && sourceFiles.size()!=destinationFiles.size())) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < sourceFiles.size(); i++) {
            logFormat = "Copy file\t%s\tto\t%s";
            logger.info(String.format(logFormat,sourceFiles.get(i).toString(), destinationFiles.get(i).toString()));
            Files.copy(sourceFiles.get(i), destinationFiles.get(i), StandardCopyOption.REPLACE_EXISTING);
        }
        logFormat = "copyFiles end";
        logger.debug(logFormat);
    }


    /**Переводит список путей в строку
     *
     * @param sourceFiles
     * @return
     */
    private String pathListToString(final List<Path> sourceFiles) {
        StringBuilder builder = new StringBuilder();
        for (Path sourceFile : sourceFiles) {
            builder.append(sourceFile.toString()).append("\n");
        }
        return builder.toString();
    }

    /**Удаляет все файлы по списку
     *
     * @param list
     */
    private void deleteFiles(List<Path> list) {
        if (list==null) return;

        for (Path path : list) {
            FileUtils.deleteQuietly(path.toFile());
        }
    }
}
