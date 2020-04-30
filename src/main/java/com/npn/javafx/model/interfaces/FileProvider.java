package com.npn.javafx.model.interfaces;

import java.nio.file.Path;

/**Интерфейс предназначен для получения архива из адреса, переданого в строке
 *
 */
public interface FileProvider {

    Path getFile(String inputPath);
}
