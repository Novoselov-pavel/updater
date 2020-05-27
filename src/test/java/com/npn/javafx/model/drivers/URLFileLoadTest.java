package com.npn.javafx.model.drivers;

import com.npn.javafx.model.CRC32Calculator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

//TODO доделать до автоматического режима тестов если найду сервер удобный для проверки и дописать проверку CRC
class URLFileLoadTest {

    @Test
    void uriFromHttp() {
        try {
            URLFileLoad<Path> urlFileLoad = new URLFileLoad<>("https://yadi.sk/i/_SPQS2I0T2-xiw");
            ExecutorService executorService = Executors.newFixedThreadPool(2);
            Future<Path> future = executorService.submit(urlFileLoad);
            Path path = future.get();
            executorService.shutdownNow();
            Files.deleteIfExists(path);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    void uriFromPath() {
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(2);

            String path1 = "/home/pavel/IdeaProjects/javafx/updater/src/test/versionsFolder/00.00.01/Human.java";
            String path2 = "/home/pavel/IdeaProjects/javafx/updater/src/test/versionsFolder/00.00.01/йййыйуцаеп/apache-tomcat-9.0.34-deployer.tar.gz";

            URLFileLoad<Path> urlFileLoad1 = new URLFileLoad<>(path1);
            URLFileLoad<Path> urlFileLoad2 = new URLFileLoad<>(path2);

            Future<Path> future1 = executorService.submit(urlFileLoad1);
            Future<Path> future2 = executorService.submit(urlFileLoad2);

            Path copyFile1 = future1.get();
            Path copyFile2 = future2.get();
            executorService.shutdownNow();

            CRC32Calculator crc32Calculator = new CRC32Calculator();

            assertEquals(crc32Calculator.getCRC32(Paths.get(path1)),
                    crc32Calculator.getCRC32(copyFile1));
            assertEquals(crc32Calculator.getCRC32(Paths.get(path2)),
                    crc32Calculator.getCRC32(copyFile2));

            Files.deleteIfExists(copyFile1);
            Files.deleteIfExists(copyFile2);


        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void loadFiles() {
        String path1 = "/home/pavel/IdeaProjects/javafx/updater/src/test/versionsFolder/00.00.01/Human.java";
        String path2 = "/home/pavel/IdeaProjects/javafx/updater/src/test/versionsFolder/00.00.01/йййыйуцаеп/apache-tomcat-9.0.34-deployer.tar.gz";
        List<String> inputList = new ArrayList<>();
        inputList.add(path1);
        inputList.add(path2);

        try {
            Map<Path,Path> map = URLFileLoad.loadFiles(inputList);
            CRC32Calculator crc32Calculator = new CRC32Calculator();

            assertEquals(map.size(),2);
            for (Map.Entry<Path, Path> entry : map.entrySet()) {
                assertEquals(crc32Calculator.getCRC32(entry.getKey()), crc32Calculator.getCRC32(entry.getValue()));
            }

            for (Map.Entry<Path, Path> entry : map.entrySet()) {
                try {
                    Files.deleteIfExists(entry.getValue());
                } catch (IOException ignore) {

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }


    }
}