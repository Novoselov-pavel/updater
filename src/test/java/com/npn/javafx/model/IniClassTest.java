package com.npn.javafx.model;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class IniClassTest {
    private static final Path iniFilePath = Paths.get("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/iniFile.xml");
    private static final Path firstItem = Paths.get("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/Carlson.java");
    private static final Path secondItem = Paths.get("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/Human.java");

    @Test
    void loadFromXmlFile() {
        ///TODO остановился здесь


    }

    @Test
    void saveToXMLFile() {
        try {
            CRC32Calculator crc32Calculator = new CRC32Calculator();
            IniClass iniClass = new IniClass();
            FileItem fileItem1 = new FileItem(firstItem);
            fileItem1.setCRC32(crc32Calculator.getCRC32(firstItem));
            fileItem1.setUnpack(false);

            FileItem fileItem2 = new FileItem(secondItem);
            fileItem2.setCRC32(crc32Calculator.getCRC32(secondItem));
            fileItem2.setUnpack(false);

            iniClass.addFileItem(fileItem1);
            iniClass.addFileItem(fileItem2);

            iniClass.saveToXMLFile(iniFilePath);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }
}