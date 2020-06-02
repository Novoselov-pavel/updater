package com.npn.javafx.ui.eventhandler;

import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;

import java.util.function.Predicate;

/**
 * Проверяет текст в TextArea на правильность в соответствии с переданным Predicate<String> checker
 * если текст правильный - переводит
 *
 */
public class TextAreaCheck implements EventHandler<KeyEvent> {
    private Predicate<String> checker;
    private String unHighlightStyle ="";
    private boolean isHighlight = false;

    /**
     * Конструктор проверки текста в TextArea на правильность в соответствии с переданным Predicate<String> checker
     *
     * @param checker Predicate<String> c реализованным методом test
     */
    public TextAreaCheck(Predicate<String> checker) {
        this.checker = checker;
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getSource() instanceof TextArea) {
            TextArea textArea = (TextArea) event.getSource();
            if (textArea.getText() != null && !textArea.getText().isBlank()) {
                String text = textArea.getText();
                if (!checker.test(text)) {
                    highlight(textArea);
                } else {
                    unHighlight(textArea);
                }
            }
        }

    }

    private void highlight(TextArea textArea) {
        if(!isHighlight)
            unHighlightStyle = textArea.getStyle();
        isHighlight = true;
        textArea.setStyle("-fx-text-fill: red;");
    }

    private void unHighlight(TextArea textArea) {
        textArea.setStyle(unHighlightStyle);
    }
}
