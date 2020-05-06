package com.npn.javafx.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.CRC32;

/**Расчитывает CRC32
 *
 */
public class CRC32Calculator {
    private static final Logger logger = LoggerFactory.getLogger(CRC32Calculator.class);
    private static final int BUFFER_SIZE = 8192;
    private CRC32 crc32 = new CRC32();


    /**Расчитывает CRC32 у файла
     *
     * @param path путь файла
     * @return CRC32
     * @throws IOException
     */
    public long getCRC32 (Path path) throws IOException {
        String logFormat = "getCRC32 File\t%s";
        logger.debug(String.format(logFormat,path.toString()));
        crc32 = new CRC32();

        logFormat = "Start calculate SRC32 from file\t%s";
        logger.info(String.format(logFormat,path.toString()));

        if (Files.isDirectory(path)) {
            logFormat = "End calculate SRC32 from file\t%s";
            logger.info(String.format(logFormat,path.toString()));
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

            logFormat = "End calculate SRC32 from file\t%s";
            logger.info(String.format(logFormat,path.toString()));
            return crc32.getValue();
        }
    }

    /**Обновляет CRC32
     *
     * @param b массив байтов
     * @param off начальный индекс в массиве
     * @param len длина массива
     */
    public void update(byte[] b, int off, int len) {
        crc32.update(b,off,len);

    }

    /**Возвращает CRC32 у текущего объекта
     *
     * @return CRC32
     */
    public long getCRC32 () {
        return crc32.getValue();
    }
}
