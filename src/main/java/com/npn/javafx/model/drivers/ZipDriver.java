package com.npn.javafx.model.drivers;

import com.npn.javafx.model.CRC32Calculator;
import com.npn.javafx.model.FileItem;
import com.npn.javafx.model.interfaces.ArchiveDriver;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**Класс для работы с zip файлами
 * TODO тесты
 */
public class ZipDriver implements ArchiveDriver {
    private final CRC32Calculator crc32 = new CRC32Calculator();
    private static final Logger logger = LoggerFactory.getLogger(ZipDriver.class);
    private final int BUFFER_SIZE = 8192;

    /**
     * Распаковывает файлы из архива
     *
     * @param zipFilePath         адрес распаковываемого архива
     * @param destinationFolder   адрес папки в которую производится распаковка
     * @param filesSystemCharset кодировка консоли файловой системы (для Windows CP866)
     * @return список {@link FileItem}
     * @throws Exception при ошибках
     */
    @Override
    public List<FileItem> unPack(final Path zipFilePath, final Path destinationFolder, final StandardCharsets filesSystemCharset) throws Exception {
        String logFormat = "unPack zipFile\t%s\t%to\t%s\tFileSystemCharset\t%s";
        logger.debug(String.format(logFormat,zipFilePath.toString(),destinationFolder.toString(), filesSystemCharset.toString()));

        logFormat = "Start unpack file\t%s\t%to\t%s\tFileSystemCharset\t%s";
        logger.info(String.format(logFormat,zipFilePath.toString(),destinationFolder.toString(), filesSystemCharset.toString()));

        List<FileItem> filesList = new ArrayList<>();

        try (ZipFile zipFile = new ZipFile(zipFilePath.toString(), filesSystemCharset.toString())) {
            ArrayList<ZipEntry> inputList = Collections.list(zipFile.getEntries());
            for (ZipEntry entry : inputList) {
                filesList.add(unPackZipEntry(zipFile,entry,destinationFolder));
            }
        }
        logFormat = "End unpack file\t%s\t%to\t%s\tFileSystemCharset\t%s";
        logger.info(String.format(logFormat,zipFilePath.toString(),destinationFolder.toString(), filesSystemCharset.toString()));
        return filesList;
    }




    /**
     * Упаковываем файлы в архив
     *
     * @param files               список {@link FileItem} которые надо упаковать
     * @param basePath            адрес папки относительно которой рассчитываются относительные пути в архиве
     * @param zipFilePath         адрес архива
     * @param filesSystemCharset кодировка консоли файловой системы (для Windows CP866)
     * @return {@link FileItem} архива
     * @throws Exception при ошибках
     */
    @Override
    public FileItem pack(final List<FileItem> files,final Path basePath,final Path zipFilePath,final StandardCharsets filesSystemCharset) throws Exception {
        //TODO
        return null;
    }


    /**
     * Возвращаем список расширений файлов для которых предназначена имплементация интрефейса
     *
     * @return список расширений в формате ".zip", ".rar".
     */
    @Override
    public List<String> getExtensions() {
        ArrayList<String> list = new ArrayList<>();
        list.add(".zip");
        return Collections.unmodifiableList(list);
    }

    /**Распаковывает {@link ZipEntry}
     *
     * @param zipFile
     * @param entry
     * @param destinationFolder
     * @return
     * @throws Exception
     */
    private FileItem unPackZipEntry(ZipFile zipFile, ZipEntry entry,  Path destinationFolder) throws Exception {
        String logFormat = "unPackZipEntry zipFile\t%s\tZipEntry\t%s";
        logger.debug(String.format(logFormat,zipFile.toString(),entry.toString()));

        Path path = destinationFolder.resolve(getPathOfZipEntry(entry));

        logFormat = "Start unpack file\t%s\tto\t%s";
        logger.info(String.format(logFormat,zipFile.toString(),entry.toString()));
        try {
            if (isDirectory(entry))
                Files.createDirectories(path);
            else
                Files.createDirectories(path.getParent());
        } catch (FileAlreadyExistsException ignored) {}

        if (isDirectory(entry)) {
            FileItem item = new FileItem(path);
            item.setCRC32(0);

            logFormat = "End unpack file\t%s\tto\t%s";
            logger.info(String.format(logFormat,zipFile.toString(),entry.toString()));
            return item;
        } else {
            try(OutputStream outputStream = Files.newOutputStream(path);
                InputStream inputStream = zipFile.getInputStream(entry)){
                writeInputStreamToOutputStream(inputStream,outputStream);

                FileItem item = new FileItem(path);
                item.setCRC32(crc32.getCRC32(path));

                logFormat = "End unpack file\t%s\tto\t%s";
                logger.info(String.format(logFormat,zipFile.toString(),entry.toString()));
                return item;
            }
        }

    }



    /**Возвращает true если {@link ZipEntry} является папкой (имя заканчивается на "/")
     *
     * @param entry {@link ZipEntry}
     * @return true если entry является папкой.
     */
    private boolean isDirectory (ZipEntry entry) {
        return entry.getName().endsWith("/");
    }

    /**Возвращает относительный путь {@link ZipEntry} с замененным последним символом "/" на символ системного
     * свойства  file.separator
     *
     * @param entry {@link ZipEntry}
     * @return {@link Path}
     */
    private Path getPathOfZipEntry (ZipEntry entry) {
        if (isDirectory(entry)) {
            return Paths.get(entry.getName().substring(0,entry.getName().length()-1)+System.getProperty("file.separator"));
        } else {
            return Paths.get(entry.getName());
        }
    }



    /**Modified path to string with end symbol "/" in accordance with requirement  org.apache.tools.zip.ZipEntry;
     * @param path input directory path
     * @return string
     */
    private String getZipDirectory (Path path) {
        String s = path.toString();
        String endString = System.getProperty("file.separator");
        if (s.endsWith(endString)) {
            s = s.substring(0,s.length()-endString.length());
        }
        return s+"/";
    }

    /**Записывает поток InputStream в OutputStream
     *
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    private void writeInputStreamToOutputStream (InputStream inputStream, OutputStream outputStream) throws IOException {
        byte dataBuffer[] = new byte[BUFFER_SIZE];
        int byteRead;
        while ((byteRead = inputStream.read(dataBuffer)) != -1) {
            outputStream.write(dataBuffer, 0, byteRead);
            outputStream.flush();
        }
    }

}
