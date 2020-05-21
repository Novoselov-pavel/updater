package com.npn.javafx.model.drivers;

import com.npn.javafx.model.interfaces.PropertiesLoader;
import com.npn.javafx.model.interfaces.PropertiesSaver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**Интерфес для загрузки свойств из файла настроек XML
 *
 */
public class PropertiesXmlDriver implements PropertiesLoader, PropertiesSaver {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesXmlDriver.class);

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

    /**
     * Сохраняет настройки в файл по адресу path
     *
     * @param properties настройки
     * @param path       адрес куда сохраняются файлы
     * @throws Exception on error
     */
    @Override
    public void saveProperties(Properties properties, String path) throws Exception {
        logger.debug("saveProperties{}",path);
        logger.info("Start save properties to\t{}",path);
        properties.storeToXML(Files.newOutputStream(Paths.get(path)),"updater xml file", StandardCharsets.UTF_8);
        logger.info("End save properties to\t{}",path);
    }
}
