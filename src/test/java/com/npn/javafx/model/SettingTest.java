package com.npn.javafx.model;

import com.npn.javafx.model.drivers.PropertiesXmlDriver;
import com.npn.javafx.model.interfaces.PropertiesSaver;
import com.npn.javafx.model.validators.PropertiesValidatorByEnum;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class SettingTest {

    private Setting getNewSetting() {
        Properties properties = new Properties();
        Setting setting = new Setting(properties);
        setting.setVersion(new Version("10.0.1"));
        setting.setVersionParser(DirParserEnum.FILE_SYSTEM);
        setting.setFileParser(FilesParserEnum.FILE_SYSTEM);
        setting.setLocation("/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/iniFile.xml");
        setting.setIniFileName("iniFile.xml");
        setting.setExeFileName("test.test");
        return setting;
    }



    @Test
    void saveAndLoadProperties() {
        Setting setting = getNewSetting();
        PropertiesSaver saver = new PropertiesXmlDriver();
        try {
            String path = "/home/pavel/IdeaProjects/javafx/updater/src/test/fileUpdateSetting.xml";
            setting.saveSetting(saver,path);
            Setting loadedSetting = Setting.loadSetting(new PropertiesXmlDriver(),new PropertiesValidatorByEnum(),path);
            assertEquals(setting,loadedSetting);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


    @Test
    void testSetterAndGetters() {
        Setting setting = getNewSetting();
        assertTrue(FilesParserEnum.FILE_SYSTEM.getAssociatedClass().isInstance(setting.getFileParser()));
        assertTrue(DirParserEnum.FILE_SYSTEM.getAssociatedClass().isInstance(setting.getVersionParser()));
        assertEquals(new Version("10.0.1"),setting.getVersion());
    }
}