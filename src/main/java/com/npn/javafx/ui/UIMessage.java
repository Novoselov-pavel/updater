package com.npn.javafx.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.SynchronousQueue;

/**
 * Класс для получения сообщений об выполнении архивирования
 */
public class UIMessage {
    private static final Logger logger = LoggerFactory.getLogger(UIMessage.class);
    private static final UIMessage uiMessage = new UIMessage();
    private final SynchronousQueue<String> queue = new SynchronousQueue<>();

    private volatile boolean work = false;

    private UIMessage() {};

    public static UIMessage getUiMessage() {
        return uiMessage;
    }

    /**
     * Добавляет сообщение в очередь если есть получатель (work = true)
     *
     * @param message сообщение
     * @throws InterruptedException
     */
    public void sendMessage (String message) throws InterruptedException {
        if (work) {
            queue.put(message);
        }
    }
    /**
     * Добавляет сообщение в очередь если есть получатель (work = true)
     *
     * @param stringFormat сообщение String.format
     * @param args аргументы String.format
     * @throws InterruptedException
     */
    public void sendMessage (String stringFormat, Object ...args) throws InterruptedException {
        sendMessage(String.format(stringFormat,args));
    }

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
        logger.info("UIMessage set {}",work);
        this.work = work;
    }

    public String getMessage() throws InterruptedException {
        return queue.take();
    }
}
