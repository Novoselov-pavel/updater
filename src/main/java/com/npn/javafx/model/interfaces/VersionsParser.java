package com.npn.javafx.model.interfaces;

import java.util.List;


/**Интерфейс для получения списка версий
 *
 */
public interface VersionsParser {

    /** Получает список версий по указанному адресу
     *
     * @param path адрес где располагается список версий
     * @return List со списком версий
     */
    List<String> getVersion(String path) throws Exception;

    /** Получает адрес где размещается список версий
     *
     * @param version номер версии
     * @param path адрес размещения всех версий
     * @return адрес расположения файлов с версиями
     * @throws Exception при ошибке
     */
    String getAddress(String version, String path) throws Exception;
}
