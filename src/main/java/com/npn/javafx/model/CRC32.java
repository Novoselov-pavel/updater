package com.npn.javafx.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**Расчитывает CRC32
 *
 */
public class CRC32 {
    private static final Logger logger = LoggerFactory.getLogger(CRC32.class);
    private final int BUFFER_SIZE = 8192;
    private final java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();


    /**расчитывает
     *
     * @param path
     * @return
     * @throws IOException
     */
    public long getCRC32 (Path path) throws IOException {
        String logFormat = "getCRC32 File\t%s";
        logger.debug(String.format(logFormat,path.toString()));
        if (Files.isDirectory(path)) {
            return 0;
        }
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(Files.newInputStream(path))) {
            while (bufferedInputStream.available() > 0) {
                byte[] buffer = new byte[BUFFER_SIZE];

                int val = bufferedInputStream.read(buffer);
                if (val>-1) {
                    crc32.update(buffer,0,val);
                }
            }
            return crc32.getValue();
        }
    }

}
