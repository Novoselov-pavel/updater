package com.npn.javafx.model;


import com.npn.javafx.model.drivers.SafeCopyFiles;
import com.npn.javafx.model.drivers.ZipDriver;
import com.npn.javafx.model.exception.FailUpdateFiles;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**Класс работающий с ini файлом обновления
 *
 */

@XmlType(name = "files")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class IniClass {
    private static final Logger logger = LoggerFactory.getLogger(IniClass.class);

    @XmlElement(name = "file")
    private List<FileItem> fileItems = new ArrayList<>();

    public IniClass() {

    }

    /**
     * Получение ini файла который определен в
     *
     * @param setting настройки программы
     * @param map мапа из адресов исходных файлов и скаченных временных
     * @return Path скаченного временного файла или null если не найден.
     */
    public static Path getIniFile (Setting setting, Map<Path, Path> map) {
        logger.debug("getIniFile");
        logger.info("Get iniFile from map");
        String iniFileName = setting.getIniFileName();
        Map.Entry<Path,Path> entry = map.entrySet().stream()
                .filter(x-> x.getKey().getFileName().equals(Paths.get(iniFileName)))
                .findFirst().orElse(null);

        if (entry==null) return null;
        return entry.getValue();
    }


    /**
     * функция обновления по ini файлу
     *
     * @param iniFile адрес скачанного ini файла
     * @param map мапа со скачанными файлами, ключ - адрес файла на сервере/исходной папке, значение - адрес временного файла
     * @param basePath путь куда распаковывается содержимое согласно Ini файлу
     * @throws Exception
     */
    public static void proceedIniFile(Path iniFile, Map<Path, Path> map, Path basePath) throws Exception {
        IniClass iniClass = IniClass.loadFromXmlFile(iniFile);

        List<Path> sourceFiles = new ArrayList<>();

        Map<Path,Path> copyMap = new HashMap<>();

        iniClass.getFileItems().forEach(x->{
            if (x.needUnpack()) {
                try {
                    Path tempDir = Files.createTempDirectory("updater");
                    Path file = getTempPathFromMap(x.getPath(),map);
                    ZipDriver zipDriver =  new ZipDriver();
                    List<FileItem> files = zipDriver.unPack(file,tempDir,Setting.getConsoleCharset());
                    addUnpackFileToMap(copyMap, files, x,tempDir);
                } catch (IOException e) {
                    throw new FailUpdateFiles("Can't create temporary directory", e);
                } catch (Exception e) {
                    throw new FailUpdateFiles("Can't unpack file", e);
                }
            } else  {
                copyMap.put(getTempPathFromMap(x.getPath(),map),x.getUnpackPath());
            }
        });

        SafeCopyFiles safeCopyFiles = new SafeCopyFiles(copyMap);
    }



    /**
     * Выполняет проверку целостности скаченного по ini файлу
     *     I этап - проверка целостности скаченного
     *     найти файл в мапе по его пути на сервере и значению path из FileItem
     *     проверить CRC скаченного файла, если оно ошибочное в лог WARN
     *     добавить в список перезакачки
     *
     * @param iniClass IniClass
     * @param map мапа со скачанными файлами, ключ - адрес файла на сервере/исходной папке, значение - адрес временного файла
     * @return список с файлами корые требует перезакачки, или пустой список
     */
    public static List<String> checkCRCStage(IniClass iniClass, Map<Path, Path> map) {
        logger.info("Start checking CRC");
        List<Path> retList = new ArrayList<>();
        CRC32Calculator crc32Calculator = new CRC32Calculator();
        iniClass.getFileItems().forEach(x->{
            Path tempPath = getTempPathFromMap(x.getPath(),map);
            if (tempPath == null) throw new FailUpdateFiles("File not found:\t"+x.getPath().toString()+"",null);
            try {
                long crc = crc32Calculator.getCRC32(tempPath);
                if (crc != x.getCRC32()) {
                    retList.add(x.getPath());
                }
            } catch (IOException e) {
                retList.add(x.getPath());
            }
        });

        return getFileAddressList(retList,map);
    }

    private static Map<Path,Path> addUnpackFileToMap(Map<Path,Path> map, List<FileItem> files, FileItem baseFileItem, Path zipUnpackFolder) {
        for (FileItem item : files) {
            Path destinationPath =baseFileItem.getUnpackPath().resolve(zipUnpackFolder.relativize(item.getPath()));
            map.put(item.getPath(), destinationPath);
        }
        return map;
    }

    private static Path getTempPathFromMap(Path filename, Map<Path, Path> map){
        for (Map.Entry<Path, Path> entry : map.entrySet()) {
            if (entry.getKey().getFileName().equals(filename)) return entry.getValue();
        }
        return null;
    }

    private static List<String> getFileAddressList(List<Path> paths, Map<Path, Path> map) {
        List<String> strings = new ArrayList<>();
        paths.forEach(x->strings.add(getInputFileNameTempPathFromMap(x,map).toString()));
        return strings;
    }

    private static Path getInputFileNameTempPathFromMap(Path filename, Map<Path, Path> map){
        for (Map.Entry<Path, Path> entry : map.entrySet()) {
            if (entry.getKey().getFileName().equals(filename)) return entry.getKey();
        }
        throw new FailUpdateFiles("Error in getInputFileNameTempPathFromMap", null);
    }




    /**Загружает IniClass из XML файла
     *
     * @param path путь к файлу
     * @return IniClass
     * @throws JAXBException
     */
    public static IniClass loadFromXmlFile(final Path path) throws JAXBException {
        logger.debug("loadFromXmlFile");
        logger.info("Start loading IniClass from\t{}",path.toString());
        JAXBContext context = JAXBContext.newInstance(IniClass.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        IniClass readClass = (IniClass) unmarshaller.unmarshal(path.toFile());
        logger.info("End loading IniClass from\t{}",path.toString());
        return readClass;
    }

    /**Сохраняет IniClass в XML файле
     *
     * @param path путь к файлу
     * @throws JAXBException
     */
    public void saveToXMLFile(final Path path) throws JAXBException {
        logger.debug("saveToXMLFile");
        logger.info("Start save IniClass to\t{}",path.toString());
        JAXBContext context = JAXBContext.newInstance(IniClass.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(this,path.toFile());
        logger.info("End save IniClass to\t{}",path.toString());
    }

    public List<FileItem> getFileItems() {
        return Collections.unmodifiableList(fileItems);
    }

    /** Добавляет FileItem в список
     *
     * @param fileItem FileItem
     */
    public void addFileItem(final FileItem fileItem) {
        fileItems.add(fileItem);
    }

    /** Удаляет FileItem из списка
     *
     * @param fileItem FileItem
     */
    public boolean removeFileItem (final  FileItem fileItem) {
        return fileItems.remove(fileItem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IniClass iniClass = (IniClass) o;
        return Objects.equals(fileItems, iniClass.fileItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileItems);
    }
}
