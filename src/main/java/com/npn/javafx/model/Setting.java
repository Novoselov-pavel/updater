package com.npn.javafx.model;

import com.npn.javafx.controller.ParserClassController;
import com.npn.javafx.model.interfaces.PropertiesLoader;
import com.npn.javafx.model.interfaces.PropertiesSaver;
import com.npn.javafx.model.interfaces.PropertiesValidator;
import com.npn.javafx.model.interfaces.VersionsParser;

import java.util.Properties;

/**
 * Класс для хранения настроек работы программы.
 *
 */
public class Setting {
    private final Properties properties;

    public Setting(Properties properties) {
        this.properties = properties;
    }

    public static Setting loadProperties(final PropertiesLoader loader, final PropertiesValidator validator, final String path) throws Exception {
        Properties properties = loader.loadProperties(path);
        if (validator.isPropertiesValid(properties)) {
            Setting setting = new Setting(properties);
            return setting;
        } else {
            throw new IllegalArgumentException("Properties file is incorrect");
        }
    }


    /**
     * Сохраняет настройки в файле
     *
     * @param saver имплементация {@link PropertiesSaver}
     * @param path путь сохранения в файл
     * @throws Exception при ошибках
     */
    public void saveProperties(final PropertiesSaver saver, final String path) throws Exception {
        saver.saveProperties(properties,path);
    }


    /**
     * Получает версию из properties
     *
     * @return {@link Version} или null, если значение отсутствует или не распознается
     */
    public Version getVersion () {
        try {
            return new Version(getProperty(PropertiesEnum.VERSION));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Устанавливает версию в настройки программы
     * @param version
     */
    public void setVersion(final Version version) {
        String key = getKey(PropertiesEnum.VERSION);
        key = key==null? PropertiesEnum.VERSION.toString():key;
        properties.setProperty(key,version.toString());
    }

    /**
     * Получает экземпляр имплементирующий VersionsParser из properties
     *
     * @return {@link VersionsParser} или null, если оно не найдено
     */
    public VersionsParser getVersionParser() {
        String property = getProperty(PropertiesEnum.DIR_PARSER_NAME);
        if (property==null) return null;
        DirParserEnum parserEnum = DirParserEnum.getEnum(property);
        if (parserEnum==null) return null;
        return ParserClassController.getVersionParser(parserEnum);
    }

    /**
     * Устанавливает парсер для поиска файлов в настройки программы
     *
     * @param value DirParserEnum
     */
    public void setVersionParser(DirParserEnum value) {
        if (value==null) return;
        String key = getKey(PropertiesEnum.DIR_PARSER_NAME);
        key = key==null? PropertiesEnum.DIR_PARSER_NAME.toString():key;
        properties.setProperty(key,value.getXmlParserName());
    }



    ///TODO геттеры и сеттеры для всех свойств



    /**
     * Получает значение из properties
     *
     * @param property PropertiesEnum
     * @return строковое значение или null, если оно не определено
     */
    private String getProperty(final PropertiesEnum property) {

        String key = getKey(property);
        return key!=null?properties.getProperty(key):null;
    }

    /**
     * Получает ключ из properties
     *
     * @param property PropertiesEnum
     * @return строковое значение или null, если ключ не существует
     */
    private String getKey(final PropertiesEnum property) {
        String key = (String) properties
                .keySet()
                .stream()
                .filter(x->((String)x).equalsIgnoreCase(property.toString()))
                .findFirst()
                .orElse(null);
        return key;
    }



}
