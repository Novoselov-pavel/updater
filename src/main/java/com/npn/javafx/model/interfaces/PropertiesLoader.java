package com.npn.javafx.model.interfaces;

import java.util.Properties;

/**Интерфес для загрузки свойств из файла настроек
 *
 */
public interface PropertiesLoader {
    /**Загружает настройки из файла по адресу path
     *
     * @param path адрес откуда загружаются файлы
     * @return {@link Properties}
     * @throws Exception on error
     */
    Properties loadProperties(String path) throws Exception;
}
