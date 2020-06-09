package com.npn.javafx.ui;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArchiveItemTableViewTest {

    @Test
    void transformToArchiveObject() {
        TableFileItem[] array = new TableFileItem[8];
        array[0] = getTableFileItem("",false,
                "/home/pavel/IdeaProjects/javafx/updater/src/test/proceedIniFileTest/");
        array[1] = getTableFileItem("Carlson.zip",false,
                "/home/pavel/IdeaProjects/javafx/updater/src/test/proceedIniFileTest/DownloadFile/Carlson.zip.qweqwe");
        array[2] = getTableFileItem("dirzip",true,
                "/home/pavel/IdeaProjects/javafx/updater/src/test/proceedIniFileTest/DownloadFile/Carlson.zip.qweqwe");

        array[3] = getTableFileItem("001",true,
                "/home/pavel/IdeaProjects/javafx/updater/src/test/proceedIniFileTest/DownloadFile/");
        array[4] = getTableFileItem("001/002",true,
                "/home/pavel/IdeaProjects/javafx/updater/src/test/proceedIniFileTest/DownloadFile/Carlson.zip.qweqwe");

        array[5] = getTableFileItem("002/003/004",true,
                "/home/pavel/IdeaProjects/javafx/updater/src/test/proceedIniFileTest/DownloadFile/");
        array[6] = getTableFileItem("002/003/004/005/006",true,
                "/home/pavel/IdeaProjects/javafx/updater/src/test/proceedIniFileTest/DownloadFile/Carlson.zip.qweqwe");
        array[7] = getTableFileItem("002/003/004/009/010",true,
                "/home/pavel/IdeaProjects/javafx/updater/src/test/proceedIniFileTest/DownloadFile/Carlson.zip.qweqwe");

        List<ArchiveItemTableView.ArchiveObject> collection = ArchiveItemTableView.transformToArchiveObject(array);

        assertTrue(checkCollection(collection));

    }

    private boolean checkCollection(List<ArchiveItemTableView.ArchiveObject> list) {
        if (list.size()!=5) return false;
        ArchiveItemTableView.ArchiveObject item0 = list.get(0);
        if (!item0.getPathToUnpack().equals("")) return  false;
        if (item0.getFileItems().size()!=1) return  false;
        if (item0.isNeedPack()) return  false;

        ArchiveItemTableView.ArchiveObject item1 = list.get(1);
        if (!item1.getPathToUnpack().equals("Carlson.zip")) return  false;
        if (item1.getFileItems().size()!=1) return  false;

        ArchiveItemTableView.ArchiveObject item2 = list.get(2);
        if (!item2.getPathToUnpack().equals("dirzip")) return  false;
        if (item2.getFileItems().size()!=1) return  false;
        if (!item2.isNeedPack()) return  false;

        ArchiveItemTableView.ArchiveObject item3 = list.get(3);
        if (!item3.getPathToUnpack().equals("001")) return  false;
        if (item3.getFileItems().size()!=2) return  false;

        ArchiveItemTableView.ArchiveObject item4 = list.get(4);
        if (!item4.getPathToUnpack().equals("002/003/004")) return  false;
        if (item4.getFileItems().size()!=3) return  false;

        return true;
    }


    private TableFileItem getTableFileItem(String relativePath, boolean needPack, String path) {
        TableFileItem fileItem = new TableFileItem();
        fileItem.setRelativePath(relativePath);
        fileItem.setNeedPack(needPack);
        fileItem.setPath(path);
        return fileItem;
    }
}