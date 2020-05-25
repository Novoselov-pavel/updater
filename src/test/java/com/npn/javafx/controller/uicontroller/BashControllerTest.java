package com.npn.javafx.controller.uicontroller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BashControllerTest {
    @Test
    void executeTestFirstMode() {
        try {
            String[] input = new String[]{"/home/pavel/IdeaProjects/javafx/updater/src/test/versionsFolder/fileUpdateSetting.xml"};
            BashController controller = new BashController(input);
            String val = controller.execute();
            assertEquals(val,"02.05.00");

            String[] input2 = new String[]{"/home/pavel/IdeaProjects/javafx/updater/src/test/versionsFolder/fileUpdateSetting2.xml"};
            BashController controller2 = new BashController(input2);
            String val2 = controller2.execute();
            assertEquals(val2,BashController.NEW_VERSION_NOT_FOUND_ANSWER);


        } catch (Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }
}