package com.npn.javafx.model.drivers;

import com.npn.javafx.model.interfaces.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Properties;

/**Интерфес для загрузки свойств из файла настроек XML
 *
 */
public class PropertiesXmlLoader implements PropertiesLoader {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesXmlLoader.class);

    /**Загружает настройки из файла по адресу path
     *
     * @param path адрес откуда загружаются файлы
     * @return {@link Properties}
     * @throws Exception on error
     */
    @Override
    public Properties loadProperties(final String path) throws Exception {
        String logFormat = "loadProperties execute path = %s";
        logger.debug(String.format(logFormat,path));

        logFormat = "Start load properties from/t%s";
        logger.info(String.format(logFormat,path));

        try (FileInputStream inputStream = new FileInputStream(path)) {
            Properties properties = new Properties();
            properties.loadFromXML(inputStream);

            logFormat = "End load properties from/t%s";
            logger.info(String.format(logFormat,path));
            return properties;
        }
    }
}
