package com.npn.javafx.model;

import com.npn.javafx.model.drivers.PropertiesXmlDriver;
import com.npn.javafx.model.validators.PropertiesValidatorByEnum;
import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class IniClassTest {
    private static final Path iniFilePath = Paths.get("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/iniFile.xml");
    private static final Path firstItem = Paths.get("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/Carlson.java");
    private static final Path secondItem = Paths.get("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/Human.java");



    @Test
    void loadFromXmlFile() {
        try {
            IniClass iniClass = IniClass.loadFromXmlFile(iniFilePath);
        } catch (JAXBException e) {
            fail();
        }


    }

    @Test
    void saveToXMLFile() {
        try {
            saveTestClass(iniFilePath);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }


    private IniClass saveTestClass(Path path) throws IOException, JAXBException {
        CRC32Calculator crc32Calculator = new CRC32Calculator();
        IniClass iniClass = new IniClass();
        FileItem fileItem1 = new FileItem(firstItem);
        fileItem1.setCRC32(crc32Calculator.getCRC32(firstItem));
        fileItem1.setUnpack(false);
        fileItem1.setUnpackPath(Paths.get("Carlson.java"));
        fileItem1 = fileItem1.copyWithNewPath(firstItem.getFileName());


        FileItem fileItem2 = new FileItem(secondItem);
        fileItem2.setCRC32(crc32Calculator.getCRC32(secondItem));
        fileItem2.setUnpack(false);
        fileItem2.setUnpackPath(Paths.get("/abb/Human.java"));
        fileItem2 = fileItem2.copyWithNewPath(secondItem.getFileName());

        iniClass.addFileItem(fileItem1);
        iniClass.addFileItem(fileItem2);

        iniClass.saveToXMLFile(path);
        return iniClass;
    }

    @Test
    void saveAndLoadXMLFile() {
        try {
            IniClass savedClass = saveTestClass(iniFilePath);
            IniClass loadedClass= IniClass.loadFromXmlFile(iniFilePath);
            assertEquals(savedClass,loadedClass);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getIniFile() {
        String path ="/home/pavel/IdeaProjects/javafx/updater/src/test/versionsFolder/fileUpdateSetting_getIniFileTest.xml";
        Map<Path, Path> map = new HashMap<>();
        Path key1 = Paths.get("/home/pavel/IdeaProjects/javafx/updater/src/test/versionsFolder/00.00.01/Carlson.java");
        Path key2 = Paths.get("/home/pavel/IdeaProjects/javafx/updater/src/test/versionsFolder/00.00.01/iniFile.xml");
        Path value1 = Paths.get("00.tmp");
        Path value2 = Paths.get("01.tmp");

        map.put(key1,value1);
        map.put(key2,value2);


        try {
            Setting setting = Setting.loadSetting(new PropertiesXmlDriver(),new PropertiesValidatorByEnum(),path);
            assertEquals(value2,IniClass.getIniFile(setting,map));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }


    }

    @Test
    void checkCRCStage() {
        try {
            IniClass savedClass = saveTestClass(iniFilePath);
            IniClass loadedClass= IniClass.loadFromXmlFile(iniFilePath);

            Map<Path, Path> map = new HashMap<>();
            Path key1 = firstItem;
            Path key2 = secondItem;
            Path value1 = Paths.get("/home/pavel/IdeaProjects/javafx/updater/src/test/versionsFolder/00.00.01/Carlson.java");
            Path value2 = Paths.get("/home/pavel/IdeaProjects/javafx/updater/src/test/versionsFolder/00.00.01/Human.java");
            map.put(key1,value1);
            map.put(key2,value2);

            List<String> list = IniClass.checkCRCStage(loadedClass,map);
            assertEquals(0,list.size());

            IniClass failedClass= IniClass.loadFromXmlFile(Paths.get("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/FailediniFile.xml"));
            List<String> failedList = IniClass.checkCRCStage(failedClass,map);
            assertEquals(1, failedList.size());
            assertEquals("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/Carlson.java", failedList.get(0));

        } catch (IOException e) {
            e.printStackTrace();
            fail();
        } catch (JAXBException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void proceedIniFile() {
        Map<Path, Path> map = new HashMap<>();
        String serverPath = "/home/pavel/IdeaProjects/javafx/updater/src/test/proceedIniFileTest/Server/";
        Path key1 = Paths.get(serverPath,"apache-tomcat-9.0.34-deployer.tar.zip");
        Path key2 = Paths.get(serverPath,"Carlson.zip");
        Path key3 = Paths.get(serverPath,"Human.java");
        Path key4 = Paths.get(serverPath,"iniFile.xml");
        Path key5 = Paths.get(serverPath,"Smoker.zip");

        String downloadPath = "/home/pavel/IdeaProjects/javafx/updater/src/test/proceedIniFileTest/DownloadFile/";
        Path value1 = Paths.get(downloadPath,"apache-tomcat-9.0.34-deployer.tar.zip.asdsaf");
        Path value2 = Paths.get(downloadPath,"Carlson.zip.qweqwe");
        Path value3 = Paths.get(downloadPath,"Human.java.qweqwr");
        Path value4 = Paths.get(downloadPath,"iniFile.xml.asdsafg");
        Path value5 = Paths.get(downloadPath,"Smoker.zip.qweqwrwqr");

        map.put(key1,value1);
        map.put(key2,value2);
        map.put(key3,value3);
        map.put(key4,value4);
        map.put(key5,value5);

        Path outputPath = Paths.get("/home/pavel/IdeaProjects/javafx/updater/src/test/proceedIniFileTest/OutputDir/");

        try {
            IniClass.proceedIniFile(value4,map,outputPath);
            String outputDir ="/home/pavel/IdeaProjects/javafx/updater/src/test/proceedIniFileTest/OutputDir/";
            String file1 ="apache-tomcat-9.0.34-deployer.tar.gz";
            String file2 ="001/Carlson.java";
            String file3 ="abb/Human.java";
            String file4 ="Smoker.java";
            assertTrue(Files.exists(Paths.get(outputDir,file1)));
            assertTrue(Files.exists(Paths.get(outputDir,file2)));
            assertTrue(Files.exists(Paths.get(outputDir,file3)));
            assertTrue(Files.exists(Paths.get(outputDir,file4)));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}