package com.npn.javafx.model;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
