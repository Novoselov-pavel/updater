package com.npn.javafx.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


import java.nio.file.Path;
import java.util.List;

/**Класс работающий с ini файлом обновления
 *
 */

@XmlRootElement(name = "files")
@XmlAccessorType(XmlAccessType.FIELD)
public class IniClass {

    @XmlElement(name = "file")
    private List<FileItem> fileItems;

    private IniClass() {

    }

    public IniClass getInstance() {
        return new IniClass();
    }

    private static IniClass loadFromXmlFile(Path path) {
        //TODO остановился здесь
        return null;
    }

}
