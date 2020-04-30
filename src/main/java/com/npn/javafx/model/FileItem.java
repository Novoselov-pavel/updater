package com.npn.javafx.model;

import java.nio.file.Path;

/**Класс предназначен для хранения объектов (файлов) с которыми работает программа.
 */
public class FileItem {
    private final Path path;
    private int CRC32;


    public FileItem(Path path) {
        this.path = path;
    }





    public String getFileName() {
        return path.getFileName().toString();
    }

    public int getCRC32() {
        return CRC32;
    }

    public void setCRC32(int CRC32) {
        this.CRC32 = CRC32;
    }

    public Path getPath() {
        return path;
    }

    public FileItem copyWithNewPath(Path newPath) {
        FileItem item = new FileItem(newPath);
        item.setCRC32(getCRC32());
        return item;
    }

}
