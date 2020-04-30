package com.npn.javafx.model;

import java.nio.file.Path;
import java.util.List;

/**Класс хранит настройки по которым распаковывается и заменяются файлы
 *
 */
public class IniFileClass {
    private String consoleEncode;
    private Path zipFilePath;
    private String version;
    private List<FileItem> fileItems;

}
