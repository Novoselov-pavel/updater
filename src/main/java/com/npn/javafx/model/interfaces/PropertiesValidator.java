package com.npn.javafx.model.interfaces;

import java.util.Properties;


/**Интерфейс для проверки правильности файла настроек
 *
 */
public interface PropertiesValidator {

    /**Проверяет правильность файла настроек
     *
     * @param properties настройки
     * @return true если настроки правильные false если неверные
     */
    boolean isPropertiesValid(Properties properties);

}
