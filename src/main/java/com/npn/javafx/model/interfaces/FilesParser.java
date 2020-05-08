package com.npn.javafx.model.interfaces;

import java.util.List;

/**Интерфейс для получения адресов файлов которые требуется скачать из папки с версией
 *
 */
public interface FilesParser {

    /**Получает список адресов файлов  которые требуется скачать из папки с версией
     *
     * @param path адрес папки с версией
     * @return список адресов
     * @throws Exception
     */
    List<String> getFilesAddress(String path) throws Exception;
}
