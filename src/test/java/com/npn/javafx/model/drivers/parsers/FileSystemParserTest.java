package com.npn.javafx.model.drivers.parsers;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemParserTest {
    private static final String inputFiles = "/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles";
    private static final List<String> filesList = new ArrayList<>();
    private static final List<String> dirList = new ArrayList<>();
    private static final String version = "afasf";
    private static final String filePath = "/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/afasf";

    static {
        filesList.add("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/йййыйуцаеп");
        filesList.add("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/йййыйуцаеп/apache-tomcat-9.0.34-deployer.tar.gz");
        filesList.add("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/jdk-14_linux-x64_bin.deb");
        filesList.add("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/Human.java");
        filesList.add("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/Smoker.java");
        filesList.add("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/qw");
        filesList.add("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/qw/фыв");
        filesList.add("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/qw/фыв/Human.java");
        filesList.add("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/qw/фыв/Carlson.java");
        filesList.add("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/qw/уйфуафпа");
        filesList.add("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/Carlson.java");
        filesList.add("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/afasf");
        filesList.add("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/afasf/cav-linux_x64.deb");

        dirList.add("йййыйуцаеп");
        dirList.add("qw");
        dirList.add("afasf");
    }

    @Test
    void getFilesAddress() {
        FileSystemParser parser = new FileSystemParser();
        try {
            List<String> list = parser.getFilesAddress(inputFiles);
            list.removeAll(filesList);
            assertTrue(list.size()==0);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void getVersion() {
        FileSystemParser parser = new FileSystemParser();
        try {
            List<String> list = parser.getVersion(inputFiles);
            list.removeAll(dirList);
            assertTrue(list.size()==0);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void getAddress() {
        FileSystemParser parser = new FileSystemParser();
        try {
            String address = parser.getAddress(version,inputFiles);
            assertEquals(address,filePath);
        } catch (Exception e) {
            fail();
        }
    }
}