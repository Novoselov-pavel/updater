package com.npn.javafx.model;

import jakarta.xml.bind.ValidationEventHandler;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**Класс предназначен для хранения объектов (файлов) с которыми работает программа.
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class FileItem {
    private static final Logger logger = LoggerFactory.getLogger(FileItem.class);

    /**
     * в ini файле обновления имя файла
     */
    @XmlElement
    @XmlJavaTypeAdapter(PathAdapter.class)
    private final Path path;
    @XmlElement
    private long CRC32;
    @XmlElement
    private boolean unpack;

    /**
     * в ini файле обновления относительный адрекс, куда должен распаковываться файл
     */
    @XmlElement
    @XmlJavaTypeAdapter(PathAdapter.class)
    private Path unpackPath;

    private FileItem() {
        path = null;
    }


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

    public boolean needUnpack() {
        return unpack;
    }

    public void setCRC32(long CRC32) {
        this.CRC32 = CRC32;
    }

    public void setUnpack(boolean unpack) {
        this.unpack = unpack;
    }

    public Path getUnpackPath() {
        return unpackPath;
    }

    public void setUnpackPath(Path unpackPath) {
        this.unpackPath = unpackPath;
    }

    public FileItem copyWithNewPath(final Path newPath) {

        String logFormat = "copyWithNewPath execute from\t%s";
        logger.debug(String.format(logFormat,newPath.toString()));

        logFormat = "Start copy FileItem with path\t%s";
        logger.debug(String.format(logFormat,newPath.toString()));

        FileItem item = new FileItem(newPath);
        item.setCRC32(getCRC32());
        item.setUnpack(needUnpack());
        item.setUnpackPath(getUnpackPath());

        logFormat = "End copy FileItem with path\t%s";
        logger.debug(String.format(logFormat,newPath.toString()));

        return item;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileItem item = (FileItem) o;
        return CRC32 == item.CRC32 &&
                unpack == item.unpack &&
                Objects.equals(path, item.path) &&
                Objects.equals(unpackPath, item.unpackPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, CRC32, unpack, unpackPath);
    }

    private static class PathAdapter extends XmlAdapter<String, Object> {


        /**
         * Convert a value type to a bound type.
         *
         * @param v The value to be converted. Can be null.
         * @throws Exception if there's an error during the conversion. The caller is responsible for
         *                   reporting the error to the user through {@link ValidationEventHandler}.
         */
        @Override
        public Object unmarshal(String v) throws Exception {
            return Paths.get(v);
        }

        /**
         * Convert a bound type to a value type.
         *
         * @param v The value to be convereted. Can be null.
         * @throws Exception if there's an error during the conversion. The caller is responsible for
         *                   reporting the error to the user through {@link ValidationEventHandler}.
         */
        @Override
        public String marshal(Object v) throws Exception {
            if (v instanceof Path) {
                return v.toString();
            } else {
                throw new IllegalArgumentException("Illegal object type. Expected Path");
            }
        }
    }

}
