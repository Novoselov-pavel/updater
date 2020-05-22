package com.npn.javafx.controller;

import com.npn.javafx.model.DirParserEnum;
import com.npn.javafx.model.interfaces.VersionsParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserClassControllerTest {
    @Test
    void getVersionParserTest() {
        try {
            VersionsParser versionsParser1 = ParserClassController.getVersionParser(DirParserEnum.HTML_PARSER);
            VersionsParser versionsParser2 = ParserClassController.getVersionParser(DirParserEnum.FILE_SYSTEM);

            assertTrue(versionsParser1.getClass().isAssignableFrom(DirParserEnum.HTML_PARSER.getAssociatedClass()));
            assertTrue(versionsParser2.getClass().isAssignableFrom(DirParserEnum.FILE_SYSTEM.getAssociatedClass()));
        } catch (Exception ex) {
            ex.printStackTrace();
            fail();
        }

    }
}