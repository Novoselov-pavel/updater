package com.npn.javafx.model.drivers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

/**Загружает файл из URL
 *
 * @param <T> - должен наследоваться от Path
 */
public class URLFileLoad<T extends Path> implements Callable<Path> {
    private static final Logger logger = LoggerFactory.getLogger(URLFileLoad.class);
    private final URL url;

    /**Конструктор, принимает строку адреса из которого загружается файл
     *
     * @param url адрес URL
     * @throws MalformedURLException в случае ошибки адреса
     */
    public URLFileLoad(final String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    private Path loadFile() throws Exception {
        String logFormat = "loadFile File from\t%s";
        logger.debug(String.format(logFormat,url.toString()));
        Path tempFile = Files.createTempFile(null,".urifileload");

        logFormat = "loadFile create temp file\t%s";
        logger.info(String.format(logFormat,tempFile));

        tempFile.toFile().deleteOnExit();

        logFormat = "Start loading file from \t%s\tto\t%s";
        logger.info(String.format(logFormat,url.toString(),tempFile.getFileName().toString()));

        try(BufferedInputStream inputStream = new BufferedInputStream(url.openStream());
            OutputStream outputStream = Files.newOutputStream(tempFile)) {
            byte dataBuffer[] = new byte[1024];
            int byteRead;
            while ((byteRead = inputStream.read(dataBuffer))!=-1) {
                outputStream.write(dataBuffer,0,byteRead);
                outputStream.flush();
            }
        }

        logFormat = "End loading file from \t%s\tto\t%s";
        logger.info(String.format(logFormat,url.toString(),tempFile.getFileName().toString()));
        return tempFile;
    }

    /**Загружает файл и возвращает путь на временный файл
     *
     * @return
     * @throws Exception
     */
    @Override
    public Path call() throws Exception {
        return loadFile();
    }
}
