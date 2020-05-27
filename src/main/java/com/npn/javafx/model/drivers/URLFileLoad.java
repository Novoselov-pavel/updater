package com.npn.javafx.model.drivers;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**Загружает файл из URL
 *
 * @param <T> - должен наследоваться от Path
 */
public class URLFileLoad<T extends Path> implements Callable<Path> {
    private static final Logger logger = LoggerFactory.getLogger(URLFileLoad.class);
    private final URL url;

    /**
     * Значение времени в мсек через которое проверяется закончилась ли закачка файлов в методе completeAllFutures
     */
    private static final long SLEEP_TIME_MS = 500;


    /**
     * Закачивает файлы из списка и возвращает Map где ключ - путь к исходному файлу, значение - путь к закаченному файлу.
     *
     * @param files список файлов
     * @return Map<Path,Path>
     * @throws Exception при ошибке
     */
    public static Map<Path,Path> loadFiles(List<String> files) throws Exception {
        int maxExecutorProcess = Math.min(Math.max(files.size() / 15, 4),15);
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("fileLoading with URLFileLoad-%d")
                .setDaemon(false)
                .build();
        ExecutorService executorService = Executors.newFixedThreadPool(maxExecutorProcess,threadFactory);

        Map<String, Future<Path>> futureMap = new HashMap<>();

        for (String file : files) {
            futureMap.put(file,executorService.submit(new URLFileLoad<>(file)));
        }
        completeAllFutures(futureMap.values());
        executorService.shutdown();

        Map<Path, Path> map = new HashMap<>();
        for (Map.Entry<String, Future<Path>> entry : futureMap.entrySet()) {
            Path serverPath = Paths.get(entry.getKey());
            Path localPath = entry.getValue().get();
            map.put(serverPath,localPath);
        }
        return map;
    }

    /**
     * Проверяет закончена ли закачка файлов, а также прерывает выполнение всех потоков если остановлен текущий поток
     *
     * @param futures список Future
     * @throws InterruptedException при прерывании основного потока
     */
    private static void completeAllFutures(Collection<Future<Path>> futures) throws InterruptedException {
        while (true) {
            boolean isComplete = true;
            for (Future future : futures) {
                if (!future.isDone()) {
                    isComplete = false;
                }
            }

            if (isComplete) {
                return;
            } else {
                try {
                    Thread.sleep(SLEEP_TIME_MS);
                } catch (InterruptedException e) {
                    futures.forEach(Future::isCancelled);
                    throw e;
                }
            }


        }

    }



    /**Конструктор, принимает строку адреса из которого загружается файл
     *
     * @param url адрес URL
     * @throws MalformedURLException в случае ошибки адреса
     */
    public URLFileLoad(final String url) throws MalformedURLException {
        URL url1;
        try {
            url1 = new URL(url);
        } catch (MalformedURLException e) {
            url1 = Paths.get(url).toUri().toURL();
        }
        this.url = url1;
    }

    private Path loadFile() throws Exception {

        if (checkInterruptAndClose()) return null;

        String logFormat = "loadFile File from\t%s";
        logger.debug(String.format(logFormat,url.toString()));
        Path tempFile = Files.createTempFile(null,".urifileload");


        logFormat = "loadFile create temp file\t%s";
        logger.info(String.format(logFormat,tempFile));

        tempFile.toFile().deleteOnExit();

        logFormat = "Start loading file from \t%s\tto\t%s";
        logger.info(String.format(logFormat,url.toString(),tempFile.getFileName().toString()));

        if (checkInterruptAndClose()) return null;

        try(BufferedInputStream inputStream = new BufferedInputStream(url.openStream());
            OutputStream outputStream = Files.newOutputStream(tempFile)) {
            byte dataBuffer[] = new byte[1024];
            int byteRead;
            while ((byteRead = inputStream.read(dataBuffer))!=-1) {
                outputStream.write(dataBuffer,0,byteRead);
                outputStream.flush();
            }
        }

        if (checkInterruptAndClose(tempFile)) return null;

        logFormat = "End loading file from \t%s\tto\t%s";
        logger.info(String.format(logFormat,url.toString(),tempFile.getFileName().toString()));
        return tempFile;
    }


    private boolean checkInterruptAndClose(Path... allPaths) {
        if (Thread.interrupted()) {
            logger.info("Thread {} is interrupted", Thread.currentThread().getName());

            if (allPaths == null) return true;

            for (Path path : allPaths) {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException ignored) {
                }
            }
            return true;
        }
        return false;
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
