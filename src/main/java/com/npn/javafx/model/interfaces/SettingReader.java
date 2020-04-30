package com.npn.javafx.model.interfaces;

import com.npn.javafx.model.Setting;

/**Интерфейс для получения настроек
 *
 */
public interface SettingReader {

    /**Возвращает объект {@link Setting} из массива строк
     * @param strings - TODO
     * @return
     * @throws IllegalArgumentException
     */
    Setting getSetting(String[] strings);
}
