package com.npn.javafx.model.interfaces;

import com.npn.javafx.model.FileItem;


import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

/**Интерфейс предназначен для упаковки/распаковки архивов
 *
 */
public interface ArchiveDriver {

    /**Распаковывает файлы из архива
     *
     * @param zipFilePath  адрес распаковываемого архива
     * @param destinationFolder  адрес папки в которую производится распаковка
     * @param filesSystemCharsets  кодировка консоли файловой системы (для Windows CP866)
     * @return список {@link FileItem}
     * @throws Exception при ошибках
     */
    List<FileItem> unPack(Path zipFilePath, Path destinationFolder, StandardCharsets filesSystemCharsets) throws Exception;

    /** Упаковываем файлы в архивов
     *
     * @param files список {@link FileItem} которые надо упаковать
     * @param basePath адрес папки относительно которой рассчитываются относительные пути в архиве
     * @param zipFilePath адрес архива
     * @param filesSystemCharsets  кодировка консоли файловой системы (для Windows CP866)
     * @return {@link FileItem} архива
     * @throws Exception при ошибках
     */
    FileItem pack(List<FileItem> files, Path basePath, Path zipFilePath, StandardCharsets filesSystemCharsets) throws Exception;

    /** Возвращает список расширений файлов для которых предназначена имплементация интрефейса
     *
     * @return список расширений в формате ".zip", ".rar".
     */
    List<String> getExtensions();
}
