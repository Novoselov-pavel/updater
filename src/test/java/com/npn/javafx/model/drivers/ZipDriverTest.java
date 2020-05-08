package com.npn.javafx.model.drivers;

import com.npn.javafx.model.CRC32Calculator;
import com.npn.javafx.model.FileItem;
import com.npn.javafx.model.drivers.parsers.FileSystemDirParse;
import com.npn.javafx.model.interfaces.ArchiveDriver;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ZipDriverTest {
    private static final String inputFiles = "/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/inputFiles";
    private static final String outputZipFile = "/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/outputDir/1.zip";
    private static final String outputFiles = "/home/pavel/IdeaProjects/javafx/updater/src/test/testArchive/outputFiles";
    private static final Charset currentCharset = StandardCharsets.UTF_8;
    private FileItem zipFile = null;

    @Test
    void unPack() {
        try {
            ArchiveDriver driver = new ZipDriver();
            driver.unPack(Paths.get(outputZipFile),Paths.get(outputFiles), currentCharset);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void pack() {
        try {
            FileSystemDirParse parse = new FileSystemDirParse();
            List<String> filesPathToPack = parse.getFilesAddress(inputFiles);
            ArchiveDriver driver = new ZipDriver();
            CRC32Calculator crc32Calculator = new CRC32Calculator();
            List<FileItem>  fileItemListToPack = filesPathToPack.stream().
                                                                    map(x->new FileItem(Paths.get(x))).collect(Collectors.toList());
            for (FileItem item : fileItemListToPack) {
                item.setCRC32(crc32Calculator.getCRC32(item.getPath()));
            }
            zipFile = driver.pack(fileItemListToPack,Paths.get(inputFiles), Paths.get(outputZipFile),currentCharset);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void complex() {
        try {
            deleteAllFromPath(Paths.get(outputZipFile));
            deleteAllFromPath(Paths.get(outputFiles));
            pack();
            unPack();
            FileSystemDirParse parse = new FileSystemDirParse();
            List<String>  inputFilesList = parse.getFilesAddress(inputFiles);
            assertTrue(filesOK(inputFilesList));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private boolean filesOK(List<String>  inputFilesList) throws IOException {
        CRC32Calculator calculator = new CRC32Calculator();
        for (String s : inputFilesList) {
            Path firstFile = Paths.get(s);
            Path endFile = Paths.get(outputFiles).resolve(Paths.get(inputFiles).relativize(firstFile));
            long firstFileCRC = calculator.getCRC32(firstFile);
            long secondFileCRC = calculator.getCRC32(endFile);
            if (firstFileCRC!=secondFileCRC) return false;
        }
        return true;
    }

    private void deleteAllFromPath(Path path) throws Exception {
        if (!Files.exists(path)) return;

        if (Files.isRegularFile(path)) {
            Files.deleteIfExists(path);
            return;
        }

        List<Path> paths = Files.list(path).collect(Collectors.toList());
        for (Path p : paths) {
            deleteAllFromPath(p);
        }
        Files.deleteIfExists(path);
    }
}