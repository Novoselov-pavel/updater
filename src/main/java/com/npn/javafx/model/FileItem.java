package com.npn.javafx.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Objects;

/**Класс предназначен для хранения объектов (файлов) с которыми работает программа.
 */
@XmlRootElement(name = "file")
@XmlAccessorType(XmlAccessType.FIELD)
public class FileItem {
    private static final Logger logger = LoggerFactory.getLogger(FileItem.class);
    private final Path path;
    private long CRC32;
    private boolean unpack;


    public FileItem(Path path) {
        this.path = path;
    }



    public String getFileName() {
        return path.getFileName().toString();
    }

    public long getCRC32() {
        return CRC32;
    }

    public Path getPath() {
        return path;
    }

    public boolean isUnpack() {
        return unpack;
    }

    public void setCRC32(long CRC32) {
        this.CRC32 = CRC32;
    }

    public void setUnpack(boolean unpack) {
        this.unpack = unpack;
    }



    public FileItem copyWithNewPath(final Path newPath) {

        String logFormat = "copyWithNewPath execute from\t%s";
        logger.debug(String.format(logFormat,newPath.toString()));

        logFormat = "Start copy FileItem with path\t%s";
        logger.info(String.format(logFormat,newPath.toString()));

        FileItem item = new FileItem(newPath);
        item.setCRC32(getCRC32());

        logFormat = "End copy FileItem with path\t%s";
        logger.info(String.format(logFormat,newPath.toString()));

        return item;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileItem item = (FileItem) o;
        return CRC32 == item.CRC32 &&
                Objects.equals(path, item.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, CRC32);
    }
}
