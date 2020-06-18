package com.npn.javafx.ui;

import java.io.PrintStream;

/**
 * Класс для вывода информации в консоль
 */
public class UiConsole {
    /**
     * Адрес потока куда выводит программа информацию
     */
    public static final PrintStream outputStream = System.out;

    public static void println(String s) {
        outputStream.println(s);
    }
}
