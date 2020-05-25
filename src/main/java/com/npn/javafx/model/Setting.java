package com.npn.javafx.model;

import com.npn.javafx.controller.ParserClassController;
import com.npn.javafx.model.interfaces.*;

import java.util.Objects;
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

    /**
     * Загружает настройки из файла
     *
     * @param loader
     * @param validator
     * @param path
     * @return
     * @throws Exception
     */
    public static Setting loadSetting(final PropertiesLoader loader, final PropertiesValidator validator, final String path) throws Exception {
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
    public void saveSetting(final PropertiesSaver saver, final String path) throws Exception {
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
     * Устанавливает парсер для поиска папки обновления (корневого каталога) в настройки программы
     *
     * @param value DirParserEnum
     */
    public void setVersionParser(DirParserEnum value) {
        if (value==null) return;
        String key = getKey(PropertiesEnum.DIR_PARSER_NAME);
        key = key==null? PropertiesEnum.DIR_PARSER_NAME.toString():key;
        properties.setProperty(key,value.getXmlParserName());
    }

    /**
     * Получает экземпляр имплементирующий FilesParser из properties
     *
     * @return {@link FilesParser} или null, если оно не найдено
     */
    public FilesParser getFileParser() {
        String property = getProperty(PropertiesEnum.FILE_PARSER_NAME);
        if (property==null) return null;
        FilesParserEnum parserEnum = FilesParserEnum.getEnum(property);
        if (parserEnum==null) return null;
        return ParserClassController.getFileParser(parserEnum);
    }


    /**
     * Устанавливает парсер для поиска файлов в папке обновления в настройки программы
     *
     * @param value DirParserEnum
     */
    public void setFileParser(FilesParserEnum value) {
        if (value==null) return;
        String key = getKey(PropertiesEnum.FILE_PARSER_NAME);
        key = key==null? PropertiesEnum.FILE_PARSER_NAME.toString():key;
        properties.setProperty(key,value.getXmlParserName());
    }

    /**
     * Получает расположение директории с версиями программы
     *
     * @return путь или null, если данный параметр не задан
     */
    public String getLocation() {
        return  getProperty(PropertiesEnum.UPDATE_LOCATION);
    }

    /**
     * Устанавливает расположение директории с версиями программы в настройки программы
     *
     * @param location
     */
    public void setLocation(String location) {
        if (location==null) return;
        String key = getKey(PropertiesEnum.UPDATE_LOCATION);
        key = key==null? PropertiesEnum.UPDATE_LOCATION.toString():key;
        properties.setProperty(key,location);
    }

    /**
     * Получает имя INI файла обновления
     *
     * @return имя INI файла
     */
    public String getIniFileName() {
        return getProperty(PropertiesEnum.INI_FILE_NAME);
    }

    /**
     * Устанавливает имя INI файла обновления в настройки программы
     *
     * @param iniFileName имя INI файла обновления
     */
    public void setIniFileName(String iniFileName) {
        if (iniFileName==null) return;
        String key = getKey(PropertiesEnum.INI_FILE_NAME);
        key = key==null? PropertiesEnum.INI_FILE_NAME.toString():key;
        properties.setProperty(key,iniFileName);
    }

    /**
     * Получает имя запускаемого после обновления файла
     *
     * @return имя запускаемого файла
     */
    public String getExeFileName() {
        return getProperty(PropertiesEnum.EXE_FILE_NAME);
    }

    /**
     * Устанавливает имя запускаемого после обновления файла
     *
     * @param exeFileName имя запускаемого файла
     */
    public void setExeFileName(String exeFileName) {
        if (exeFileName==null) return;
        String key = getKey(PropertiesEnum.EXE_FILE_NAME);
        key = key==null? PropertiesEnum.EXE_FILE_NAME.toString():key;
        properties.setProperty(key,exeFileName);
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Setting setting = (Setting) o;
        return Objects.equals(properties, setting.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(properties);
    }
}
