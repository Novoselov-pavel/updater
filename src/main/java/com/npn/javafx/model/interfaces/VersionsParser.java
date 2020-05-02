package com.npn.javafx.model.interfaces;

import java.util.List;


/**Интерфейс для получения списка версий
 *
 */
public interface VersionsParser {

    /** возвращает список версий по указанному адресу
     *
     * @param path адрес где располагается список версий
     * @return List со списком версий
     */
    List<String> getVersion(String path) throws Exception;
}
