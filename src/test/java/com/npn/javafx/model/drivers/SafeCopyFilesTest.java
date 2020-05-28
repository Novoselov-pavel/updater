package com.npn.javafx.model.drivers;

import com.npn.javafx.model.CRC32Calculator;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class SafeCopyFilesTest {
    private static final String inputFilesDir = "/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles";
    private static final String outputFilesDir = "/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/safeCopyDir";

    @Test
    void run() {
        Path inputPath = Paths.get(inputFilesDir);
        Path outputPath = Paths.get(outputFilesDir);
//        for (File file : outputPath.toFile().listFiles()) {
//            FileUtils.deleteQuietly(file);
//        }
        List<Path> sourceFiles;
        List<Path> destinationFiles;

        Map<Path, Path> map = new HashMap<>();

        sourceFiles = FileUtils
                            .listFiles(inputPath.toFile(),null,true)
                            .stream()
                            .map(File::toPath).collect(Collectors.toList());

        destinationFiles = sourceFiles
                                .stream()
                                .map(x->inputPath.relativize(x))
                                .map(x->outputPath.resolve(x))
                                .collect(Collectors.toList());

        for (int i = 0; i <sourceFiles.size(); i++) {
            map.put(sourceFiles.get(i), destinationFiles.get(i));
        }


        SafeCopyFiles files = new SafeCopyFiles(map);
        files.run();
        CRC32Calculator crc32Calculator = new CRC32Calculator();
        try {
            for (int i = 0; i <sourceFiles.size() ; i++) {
                if (crc32Calculator.getCRC32(sourceFiles.get(i))!=crc32Calculator.getCRC32(destinationFiles.get(i))) {
                    fail();
                }
            }
        } catch (IOException e) {
            fail();
        }
    }
}