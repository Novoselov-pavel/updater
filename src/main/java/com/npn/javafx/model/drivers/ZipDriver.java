package com.npn.javafx.model.drivers;

import com.npn.javafx.model.CRC32Calculator;
import com.npn.javafx.model.FileItem;
import com.npn.javafx.model.interfaces.ArchiveDriver;
import com.npn.javafx.ui.UIMessage;
import org.apache.tools.zip.Zip64Mode;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**Класс для работы с zip файлами
 *
 */
public class ZipDriver implements ArchiveDriver {
    private final CRC32Calculator crc32 = new CRC32Calculator();
    private static final Logger logger = LoggerFactory.getLogger(ZipDriver.class);
    private final int BUFFER_SIZE = 8192;
    private static final UIMessage uiMessage = UIMessage.getUiMessage();

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
    public List<FileItem> unPack(final Path zipFilePath, final Path destinationFolder, final Charset filesSystemCharset) throws Exception {
        String logFormat = "unPack zipFile\t%s\tto\t%s\tFileSystemCharset\t%s";
        logger.debug(String.format(logFormat,zipFilePath.toString(),destinationFolder.toString(), filesSystemCharset.toString()));

        logFormat = "Start unpack file\t%s\tto\t%s\tFileSystemCharset\t%s";
        logger.info(String.format(logFormat,zipFilePath.toString(),destinationFolder.toString(), filesSystemCharset.toString()));

        List<FileItem> filesList = new ArrayList<>();

        try (ZipFile zipFile = new ZipFile(zipFilePath.toString(), filesSystemCharset.toString())) {
            ArrayList<ZipEntry> inputList = Collections.list(zipFile.getEntries());
            for (ZipEntry entry : inputList) {
                filesList.add(unPackZipEntry(zipFile,entry,destinationFolder));
            }
        }
        logFormat = "End unpack file\t%s\tto\t%s\tFileSystemCharset\t%s";
        logger.info(String.format(logFormat,zipFilePath.toString(),destinationFolder.toString(), filesSystemCharset.toString()));
        return filesList;
    }




    /** Упаковываем файлы в архивов
     *
     * @param files мапа где ключ - {@link FileItem} которые надо упаковать, значение - строка пути в Zip файле
     * @param zipFilePath адрес места хранения архива
     * @param filesSystemCharset  кодировка консоли файловой системы (для Windows CP866)
     * @return {@link FileItem} архива
     * @throws Exception при ошибках
     */
    @Override
    public FileItem pack(final Map<FileItem,String> files, final Path zipFilePath, final Charset filesSystemCharset) throws Exception {
        String logFormat = "Pack files to\t%s\tFileSystemCharset\t%s";
        logger.debug(String.format(logFormat,zipFilePath.toString(), filesSystemCharset.toString()));

        logFormat = "Start pack files to\t%s\tFileSystemCharset\t%s";
        logger.info(String.format(logFormat,zipFilePath.toString(), filesSystemCharset.toString()));
        uiMessage.sendMessage(logFormat,zipFilePath.toString(),filesSystemCharset.toString());

        try {
          Files.createDirectories(zipFilePath.getParent());
        } catch (FileAlreadyExistsException ignored) {}


        try(ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFilePath))) {
            zipOutputStream.setEncoding(filesSystemCharset.toString());
            zipOutputStream.setUseZip64(Zip64Mode.Always);
            for (Map.Entry<FileItem, String> entry : files.entrySet()) {
                long crc = packFileItem(zipOutputStream,entry.getKey(),entry.getValue(),filesSystemCharset);
                if (crc!=entry.getKey().getCRC32()) {
                    logFormat = "Wrong CRC at file\t%s\texpected: %d\tcalculated: %d";
                    logger.warn(String.format(logFormat,entry.getKey().getPath().toString(), entry.getKey().getCRC32(), crc));
                }
            }

        }

        FileItem packFile = new FileItem(zipFilePath);
        CRC32Calculator crc32 = new CRC32Calculator();
        packFile.setCRC32(crc32.getCRC32(zipFilePath));
        logFormat = "End pack files to\t%s\tFileSystemCharset\t%s";
        logger.info(String.format(logFormat,zipFilePath.toString(), filesSystemCharset.toString()));
        uiMessage.sendMessage(logFormat,zipFilePath.toString(),filesSystemCharset.toString());
        return packFile;
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
        return getZipDirectory(s);
    }

    /**Modified path to string with end symbol "/" in accordance with requirement  org.apache.tools.zip.ZipEntry;
     * @param path input directory path
     * @return string
     */
    private String getZipDirectory (String path) {
        String s = path;
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
     * @return CRC32
     * @throws IOException
     */
    private long writeInputStreamToOutputStream (InputStream inputStream, OutputStream outputStream) throws IOException {
        CRC32Calculator crc32 = new CRC32Calculator();
        String logFormat = "writeInputStreamToOutputStream begin";
        logger.debug(logFormat);
        byte dataBuffer[] = new byte[BUFFER_SIZE];
        int byteRead;
        while ((byteRead = inputStream.read(dataBuffer)) != -1) {
            crc32.update(dataBuffer,0,byteRead);
            outputStream.write(dataBuffer, 0, byteRead);
            outputStream.flush();
        }
        return crc32.getCRC32();
    }

    /**Запаковывает файл с получением CRC32 во время упаковки
     *
     * @param outputStream поток вывода
     * @param file файл который упаковывается
     * @param destinationPath  путь назначения с помощью которого определяется относительный адрес в архиве
     * @param filesSystemCharset кодировка консоли файловой системы (для Windows CP866)
     * @return CRC32 для упакованного файла
     * @throws Exception
     */
    private long packFileItem(final ZipOutputStream outputStream, final FileItem file, final String destinationPath, final Charset filesSystemCharset) throws Exception {

        String logFormat = "Start pack file\t%s\tto\t%s\tFileSystemCharset\t%s";
        logger.info(String.format(logFormat,file.getPath().toString(), destinationPath, filesSystemCharset.toString()));
        uiMessage.sendMessage(logFormat,file.getPath().toString(),destinationPath, filesSystemCharset.toString());

        if (Files.isDirectory(file.getPath()) || Files.isSymbolicLink(file.getPath())) {
            String zipEntryPathString = new String(getZipDirectory(destinationPath).getBytes(filesSystemCharset.toString()),filesSystemCharset.toString());
            ZipEntry entry = new ZipEntry(zipEntryPathString);
            outputStream.putNextEntry(entry);
            outputStream.closeEntry();
            logFormat = "End pack file\t%s\tto\t%s\tFileSystemCharset\t%s";
            logger.info(String.format(logFormat,file.getPath().toString(), destinationPath, filesSystemCharset.toString()));
            uiMessage.sendMessage(logFormat,file.getPath().toString(),destinationPath, filesSystemCharset.toString());
            return 0L;
        } else if (Files.isRegularFile(file.getPath())) {
            String zipEntryPathString = new String(destinationPath.getBytes(filesSystemCharset.toString()),filesSystemCharset.toString());
            ZipEntry entry = new ZipEntry(zipEntryPathString);
            try(InputStream stream = Files.newInputStream(file.getPath())) {
                outputStream.putNextEntry(entry);
                long crc = writeInputStreamToOutputStream(stream,outputStream);
                outputStream.closeEntry();
                logFormat = "End pack file\t%s\tto\t%s\tFileSystemCharset\t%s";
                logger.info(String.format(logFormat,file.getPath().toString(), destinationPath, filesSystemCharset.toString()));
                uiMessage.sendMessage(logFormat,file.getPath().toString(),destinationPath, filesSystemCharset.toString());
                return crc;
            }

        }
        logFormat = "File type not supported for \t%s";
        logger.warn(String.format(logFormat,file.getPath().toString()));
        uiMessage.sendMessage(logFormat,file.getPath().toString());
        return 0L;
    }
}
