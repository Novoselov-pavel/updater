package com.npn.javafx.model.interfaces;

import java.util.Properties;

/**
 * Интерфейс для сохранения свойств в файл настроек
 *
 */
public interface PropertiesSaver {


    /**
     * Сохраняет настройки в файл по адресу path
     * @param properties настройки
     * @param path адрес куда сохраняются файлы
     * @throws Exception on error
     */
    void saveProperties(Properties properties, String path) throws Exception;

}
