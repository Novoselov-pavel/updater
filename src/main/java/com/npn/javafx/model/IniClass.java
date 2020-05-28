package com.npn.javafx.model;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
     * @param tempFolder адрес временной папки
     * @throws Exception
     */
    public static void proceedIniFile(Path iniFile, Map<Path, Path> map, Path basePath, Path tempFolder) throws Exception {
        IniClass iniClass = IniClass.loadFromXmlFile(iniFile);

        List<Path> sourceFiles = new ArrayList<>();

        iniClass.getFileItems().forEach(x->{
            if (!x.isUnpack()) {

            }



        });

        ///TODO остановился тут
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
