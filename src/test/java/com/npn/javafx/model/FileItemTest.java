package com.npn.javafx.model;

import com.npn.javafx.ui.ArchiveItemTableView;
import com.npn.javafx.ui.TableFileItem;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FileItemTest {

    @Test
    void getMapToPackFromArchiveObject() {
        ArchiveItemTableView.ArchiveObject first = getFirstObject();
        ArchiveItemTableView.ArchiveObject second = getSecondObject();
        ArchiveItemTableView.ArchiveObject third = getThirdObject();

        try {
            Map<FileItem, String> map =  FileItem.getMapToPackFromArchiveObject(first);
            assertEquals(1, map.size());
            assertTrue(map.values().contains("Carlson.java"));
            assertTrue(((FileItem)map.keySet().toArray()[0]).getPath().toString().equals("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/Carlson.java"));
        } catch (Exception ex) {
            ex.printStackTrace();
            fail();
        }

        try {
            Map<FileItem, String> map =  FileItem.getMapToPackFromArchiveObject(second);
            assertEquals(1, map.size());
            assertTrue(map.values().contains("001/Smoker.java"));
            assertTrue(((FileItem)map.keySet().toArray()[0]).getPath().toString().equals("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/Smoker.java"));
        } catch (Exception ex) {
            ex.printStackTrace();
            fail();
        }
        try {
            Map<FileItem, String> map =  FileItem.getMapToPackFromArchiveObject(third);
            System.out.println("");
            assertEquals(5, map.size());
            assertTrue(map.values().contains("qw/002/уйфуафпа"));
            assertTrue(map.values().contains("qw/002/фыв"));
            assertTrue(map.values().contains("qw/002/фыв/Human.java"));
            assertTrue(map.values().contains("qw/002/фыв/Carlson.java"));
            assertTrue(map.values().contains("qw/002"));
            assertTrue(((FileItem)map.keySet().toArray()[0]).getPath().toString().equals("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/qw/уйфуафпа"));
            assertTrue(((FileItem)map.keySet().toArray()[1]).getPath().toString().equals("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/qw/фыв"));
            assertTrue(((FileItem)map.keySet().toArray()[2]).getPath().toString().equals("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/qw/фыв/Human.java"));
            assertTrue(((FileItem)map.keySet().toArray()[3]).getPath().toString().equals("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/qw/фыв/Carlson.java"));
            assertTrue(((FileItem)map.keySet().toArray()[4]).getPath().toString().equals("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/qw"));
        } catch (Exception ex) {
            ex.printStackTrace();
            fail();
        }

    }

    private ArchiveItemTableView.ArchiveObject getFirstObject() {
        ArchiveItemTableView.ArchiveObject object = new ArchiveItemTableView.ArchiveObject("Carlson.java", true);
        object.setName("archive1");
        TableFileItem tableFileItem = new TableFileItem("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/Carlson.java",
                "",
                true);
        object.addTableFileItem(tableFileItem);
        return object;
    }

    private ArchiveItemTableView.ArchiveObject getSecondObject() {
        ArchiveItemTableView.ArchiveObject object = new ArchiveItemTableView.ArchiveObject("001/Smoker.java", true);
        object.setName("archive2");
        TableFileItem tableFileItem = new TableFileItem("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/Smoker.java",
                "",
                true);
        object.addTableFileItem(tableFileItem);
        return object;
    }

    private ArchiveItemTableView.ArchiveObject getThirdObject() {
        ArchiveItemTableView.ArchiveObject object = new ArchiveItemTableView.ArchiveObject("qw", true);
        object.setName("archive3");
        TableFileItem tableFileItem = new TableFileItem("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/qw/фыв/Carlson.java",
                "фыв/Carlson.java",
                true);
        object.addTableFileItem(tableFileItem);
        TableFileItem tableFileItem2 = new TableFileItem("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/qw",
                "002",
                true);
        object.addTableFileItem(tableFileItem2);
        return object;
    }

    private ArchiveItemTableView.ArchiveObject getFourthObject() {
        ArchiveItemTableView.ArchiveObject object = new ArchiveItemTableView.ArchiveObject("йййыйуцаеп/001", true);
        object.setName("archive4");
        TableFileItem tableFileItem = new TableFileItem("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles/йййыйуцаеп",
                "",
                true);
        object.addTableFileItem(tableFileItem);
        return object;
    }

}