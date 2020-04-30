package com.npn.javafx.model.drivers;

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
    private final URL url;

    /**Конструктор, принимает строку адреса из которого загружается файл
     *
     * @param url адрес URL
     * @throws MalformedURLException в случае ошибки адреса
     */
    public URLFileLoad(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    private Path loadFile() throws Exception {
        Path tempFile = Files.createTempFile(null,".urifileload");
        tempFile.toFile().deleteOnExit();
        try(BufferedInputStream inputStream = new BufferedInputStream(url.openStream());
            OutputStream outputStream = Files.newOutputStream(tempFile)) {
            byte dataBuffer[] = new byte[1024];
            int byteRead;
            while ((byteRead = inputStream.read(dataBuffer))!=-1) {
                outputStream.write(dataBuffer,0,byteRead);
                outputStream.flush();
            }
        }
        return tempFile;
    }

    //*TODO test

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
