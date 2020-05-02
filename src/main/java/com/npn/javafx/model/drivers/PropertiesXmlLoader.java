package com.npn.javafx.model.drivers;

import com.npn.javafx.model.interfaces.PropertiesLoader;

import java.io.FileInputStream;
import java.util.Properties;

/**Интерфес для загрузки свойств из файла настроек XML
 *
 */
public class PropertiesXmlLoader implements PropertiesLoader {

    /**Загружает настройки из файла по адресу path
     *
     * @param path адрес откуда загружаются файлы
     * @return {@link Properties}
     * @throws Exception on error
     */
    @Override
    public Properties loadProperties(String path) throws Exception {
        try (FileInputStream inputStream = new FileInputStream(path)) {
            Properties properties = new Properties();
            properties.loadFromXML(inputStream);
            return properties;
        }
    }
}
