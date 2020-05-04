package com.npn.javafx.model.drivers.parsers;

import org.junit.jupiter.api.Test;

import java.sql.ClientInfoStatus;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


//TODO доделать до автоматического режима тестов если найду сервер удобный для проверки
class HTMLContentsAVersionsParserTest {

    @Test
    void getVersion() {
        HTMLContentsAVersionsParser parser = new HTMLContentsAVersionsParser();
        try {
            List<String> strings = parser.getVersion("https://repo1.maven.org/maven2/io/micronaut/micronaut-runtime/");
            System.out.println("");
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void getAddress() {
        HTMLContentsAVersionsParser parser = new HTMLContentsAVersionsParser();
        try {
            String s = parser.getAddress("1.0.5", "https://repo1.maven.org/maven2/io/micronaut/micronaut-runtime/");
            System.out.println("");
        } catch (Exception e) {
            fail();
        }

    }

    @Test
    void getFilesAddress() {
        HTMLContentsAVersionsParser parser = new HTMLContentsAVersionsParser();
        try {
            List<String> list = parser.getFilesAddress("https://repo1.maven.org/maven2/io/micronaut/micronaut-runtime/1.0.3/");
            System.out.println("");
        } catch (Exception e) {
            fail();
        }

    }
}
