package com.npn.javafx.model.drivers;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

//TODO доделать до автоматического режима тестов если найду сервер удобный для проверки и дописать проверку CRC
class URLFileLoadTest {

    @Test
    void call() {
        try {
            URLFileLoad<Path> urlFileLoad = new URLFileLoad<>("https://yadi.sk/i/_SPQS2I0T2-xiw");
            ExecutorService executorService = Executors.newFixedThreadPool(2);
            Future<Path> future = executorService.submit(urlFileLoad);
            Path path = future.get();
            executorService.shutdownNow();
            System.out.println(path.toString());
        } catch (Exception e) {
            fail();
        }

    }
}