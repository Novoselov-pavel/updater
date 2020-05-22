package com.npn.javafx.model;

import com.npn.javafx.model.drivers.parsers.FileSystemParser;
import com.npn.javafx.model.drivers.parsers.HTMLContentsAVersionsParser;
import com.npn.javafx.model.interfaces.FilesParser;

/**
 * Содержит все классы которые могут применяться в качестве парсера для поиска для поиска файлов в директории версии
 */
public enum  FilesParserEnum {
    FILE_SYSTEM(FileSystemParser.class,"FILE"),
    HTML_PARSER(HTMLContentsAVersionsParser.class,"HTML_CONTENT");

    /**
     * Соответствие значения enum и соответсвующего класса
     */
    private final Class<? extends FilesParser> aClass;
    /**
     * Имя парсера в файле настроек
     */
    private final String xmlParserName;


    FilesParserEnum(Class<? extends FilesParser> aClass, String xmlParserName) {
        this.aClass = aClass;
        this.xmlParserName = xmlParserName;
    }

    public Class<? extends FilesParser> getAssociatedClass() {
        return aClass;
    }

    public String getXmlParserName() {
        return xmlParserName;
    }

    /**
     * Возвращает парсер соответствующий переданному параметру
     * @param xmlParserName имя парсера в XML файле
     * @return VersionsParser или null если класс не был найден
     */
    public static Class<? extends FilesParser> getClass(final String xmlParserName) {
        return getEnum(xmlParserName)==null? null: getEnum(xmlParserName).getAssociatedClass();
    }

    /**
     * Возвращает DirParserEnum соответствующий переданному параметру
     * @param xmlParserName имя парсера в XML файле
     * @return DirParserEnum или null если enum не был найден
     */
    public static FilesParserEnum getEnum(final String xmlParserName) {
        for (FilesParserEnum value : FilesParserEnum.values()) {
            if (value.getXmlParserName().equalsIgnoreCase(xmlParserName)) {
                return value;
            }
        }
        return null;
    }
}
