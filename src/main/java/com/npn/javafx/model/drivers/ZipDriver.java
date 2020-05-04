package com.npn.javafx.model.drivers;

import com.npn.javafx.model.FileItem;
import com.npn.javafx.model.interfaces.ArchiveDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.CRC32;

/**Класс для работы с zip файлами
 *
 */
public class ZipDriver implements ArchiveDriver {
    private final CRC32 crc32 = new CRC32();
    private static final Logger logger = LoggerFactory.getLogger(ZipDriver.class);

    /**
     * Распаковывает файлы из архива
     *
     * @param zipFilePath         адрес распаковываемого архива
     * @param destinationFolder   адрес папки в которую производится распаковка
     * @param filesSystemCharsets кодировка консоли файловой системы (для Windows CP866)
     * @return список {@link FileItem}
     * @throws Exception при ошибках
     */
    @Override
    public List<FileItem> unPack(Path zipFilePath, Path destinationFolder, StandardCharsets filesSystemCharsets) throws Exception {
        String logFormat = "unPack zipFile\t%s\t%to\t%s\tFileSystemCharset\t%s";
        logger.debug(String.format(logFormat,zipFilePath.toString(),destinationFolder.toString(),filesSystemCharsets.toString()));
        ///TODO закончил здесь
        return null;
    }

    /**
     * Упаковываем файлы в архивов
     *
     * @param files               список {@link FileItem} которые надо упаковать
     * @param basePath            адрес папки относительно которой рассчитываются относительные пути в архиве
     * @param zipFilePath         адрес архива
     * @param filesSystemCharsets кодировка консоли файловой системы (для Windows CP866)
     * @return {@link FileItem} архива
     * @throws Exception при ошибках
     */
    @Override
    public FileItem pack(List<FileItem> files, Path basePath, Path zipFilePath, StandardCharsets filesSystemCharsets) throws Exception {
        return null;
    }


    /**
     * Возвращает список расширений файлов для которых предназначена имплементация интрефейса
     *
     * @return список расширений в формате ".zip", ".rar".
     */
    @Override
    public List<String> getExtensions() {
        ArrayList<String> list = new ArrayList<>();
        list.add(".zip");
        return Collections.unmodifiableList(list);
    }


}
