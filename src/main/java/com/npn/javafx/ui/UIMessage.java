package com.npn.javafx.ui;

import java.util.concurrent.SynchronousQueue;

/**
 * Класс для получения сообщений об выполнении архивирования
 */
public class UIMessage {
    private static final UIMessage uiMessage = new UIMessage();
    private final SynchronousQueue<String> queue = new SynchronousQueue<>();

    private volatile boolean work = false;

    private UIMessage() {};

    public static UIMessage getUiMessage() {
        return uiMessage;
    }

    public void addMessage (String message) {
        if (work) {
            queue.add(message);
        }
    }

    public void addMessage (String stringFormat, Object ...args) {
        if (work) {
            queue.add(String.format(stringFormat,args));
        }
    }

    ///TODO остановился тут

    /**
     * Сообщает о наличии Consumer
     *
     * @return
     */
    public boolean isWork() {
        return work;
    }

    /**
     * Регистрирует наличие Consumer
     * @param work true если существует Consumer
     */
    public void setWork(boolean work) {
        this.work = work;
    }
}
