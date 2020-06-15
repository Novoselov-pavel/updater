package com.npn.javafx.model.interfaces;

import com.npn.javafx.model.FileItem;


import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**Интерфейс предназначен для упаковки/распаковки архивов
 *
 */
public interface ArchiveDriver {

    /**Распаковывает файлы из архива
     *
     * @param zipFilePath  адрес распаковываемого архива
     * @param destinationFolder  адрес папки в которую производится распаковка
     * @param filesSystemCharset  кодировка консоли файловой системы (для Windows CP866)
     * @return список {@link FileItem}
     * @throws Exception при ошибках
     */
    List<FileItem> unPack(Path zipFilePath, Path destinationFolder, Charset filesSystemCharset) throws Exception;

    /** Упаковываем файлы в архивов
     *
     * @param files мапа где ключ - {@link FileItem} которые надо упаковать, значение - строка пути в Zip файле
     * @param zipFilePath адрес места хранения архива
     * @param filesSystemCharset  кодировка консоли файловой системы (для Windows CP866)
     * @return {@link FileItem} архива
     * @throws Exception при ошибках
     */
    FileItem pack(Map<FileItem,String> files, Path zipFilePath, Charset filesSystemCharset) throws Exception;

    /** Возвращает список расширений файлов для которых предназначена имплементация интрефейса
     *
     * @return список расширений в формате ".zip", ".rar".
     */
    List<String> getExtensions();


}
