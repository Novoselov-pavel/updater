package com.npn.javafx.controller;


import com.npn.javafx.model.DirParserEnum;
import com.npn.javafx.model.FilesParserEnum;
import com.npn.javafx.model.interfaces.FilesParser;
import com.npn.javafx.model.interfaces.VersionsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Контроллер для получения парсеров для поиска директории версии и для поиска файлов для сккачивания
 */
public class ParserClassController {
    private static final Logger logger = LoggerFactory.getLogger(ParserClassController.class);

    public static VersionsParser getVersionParser(DirParserEnum parserEnum) {
        if (parserEnum ==null){
            logger.error("getVersionParser error: argument is null");
            throw new IllegalArgumentException("getVersionParser parameter can not be null");
        }

        try {
            return parserEnum.getAssociatedClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
           logger.error("getVersionParser error", e);
        }
        return null;
    }

    public static FilesParser getFileParser(FilesParserEnum parserEnum) {

        if (parserEnum ==null){
            logger.error("getFileParser error: argument is null");
            throw new IllegalArgumentException("getFileParser parameter can not be null");
        }

        try {
            return parserEnum.getAssociatedClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            logger.error("getVersionParser error", e);
        }
        return null;

    }
}
