package com.npn.javafx.model;

import com.npn.javafx.model.drivers.parsers.FileSystemParser;
import com.npn.javafx.model.drivers.parsers.HTMLContentsAVersionsParser;
import com.npn.javafx.model.interfaces.VersionsParser;

/**
 * Содержит все классы которые могут применяться в качестве парсера для поиска директории версии
 */
public enum DirParserEnum {

    FILE_SYSTEM(FileSystemParser.class,"FILE"),
    HTML_PARSER(HTMLContentsAVersionsParser.class, "HTML_CONTENT");

    /**
     * Соответствие значения enum и соответсвующего класса
     */
    private final Class<? extends VersionsParser> aClass;
    /**
     * Имя парсера в файле настроек
     */
    private final String xmlParserName;

    DirParserEnum(Class<? extends VersionsParser> aClass, String xmlParserName) {
        this.aClass = aClass;
        this.xmlParserName = xmlParserName;
    }

    public Class<? extends VersionsParser> getAssociatedClass() {
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
    public static Class<? extends VersionsParser> getClass(final String xmlParserName) {
        return getEnum(xmlParserName)==null? null: getEnum(xmlParserName).getAssociatedClass();
    }

    /**
     * Возвращает DirParserEnum соответствующий переданному параметру
     * @param xmlParserName имя парсера в XML файле
     * @return DirParserEnum или null если enum не был найден
     */
    public static DirParserEnum getEnum(final String xmlParserName) {
        for (DirParserEnum value : DirParserEnum.values()) {
            if (value.getXmlParserName().equalsIgnoreCase(xmlParserName)) {
                return value;
            }
        }
        return null;
    }

}
