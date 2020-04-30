package com.npn.javafx.model.drivers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**Безопасное копирование файлов из адресов исходного списка в адреса итогового списка.
 * ИНДЕКСЫ СООТВЕСТВУЮЩИХ ФАЙЛОВ ДОЛЖНЫ СОВПАДАТЬ
 *
 */
public class SafeCopyFiles {
    private final List<Path> sourceFiles;
    private final List<Path> destinationFiles;

    /**Конструктор .
     *
     * @param sourceFiles исходный список, индексы соответсвующих файлов исходного списка и итогового должны совпадапть.
     * @param destinationFiles итоговый список файлов.
     */
    public SafeCopyFiles(List<Path> sourceFiles, List<Path> destinationFiles) {
        this.sourceFiles = sourceFiles;
        this.destinationFiles = destinationFiles;
    }

    public void copyFiles() {
        //TODO не забыть сделать несколько попыток

    }


    private List<Path> createTempList(List<Path> sourceFiles) throws IOException {
        List<Path> returnList = new ArrayList<>();
            for (Path sourceFile : sourceFiles) {
                Files.createTempFile(sourceFile.getFileName().toString(),null);
            }
         return returnList;
    }



    /**Копирует файлы из адресов исходного списка в адреса итогового списка.
     *
     * @param sourceFiles исходный список, индексы соответсвующих файлов исходного списка и итогового должны совпадапть.
     * @param destinationFiles итоговый список файлов.
     * @return true сли копирование прошло успешно.
     */
    private boolean copyFiles(List<Path> sourceFiles, List<Path> destinationFiles) {
        if (sourceFiles == null || (destinationFiles!=null && sourceFiles.size()!=destinationFiles.size())) {
            throw new IllegalArgumentException();
        }

        try {
            if (destinationFiles == null) {
                destinationFiles = new ArrayList<>();
                for (Path sourceFile : sourceFiles) {
                    Files.createTempFile(sourceFile.getFileName().toString(),null);
                }
            }

            for (int i = 0; i <sourceFiles.size(); i++) {
                Files.copy(sourceFiles.get(i),destinationFiles.get(i), StandardCopyOption.REPLACE_EXISTING);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
