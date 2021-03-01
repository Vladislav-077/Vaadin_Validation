package org.example;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TestWorldLayout extends HorizontalLayout {
    private Logger log = LogManager.getLogger(TestWorldLayout.class);
    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private TextField testWord;
    private Button okTestWord;
    private LogViewPanel logViewPanel;

    public TestWorldLayout(LogViewPanel logViewPanel) {
        this.logViewPanel = logViewPanel;

        initTextField();
        initButton();

        /** Слой для объединения тектового поля , кнопки ok **/
        this.addComponents(testWord, okTestWord);
        this.setComponentAlignment(testWord, Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(okTestWord, Alignment.MIDDLE_CENTER);
        this.setSpacing(true);

    }

    /**
     * Кнопка ок отвечает за созранение и дальнейшее использование тестового слова.
     **/
    private void initButton() {
        okTestWord = new Button("ОК");
        okTestWord.setEnabled(false);
        okTestWord.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        okTestWord.setIcon(FontAwesome.SEND);
        okTestWord.setClickShortcut(ShortcutAction.KeyCode.ENTER); /** Кнопка Enter автоматически тригерит эту кнопку.**/

        /** Обработчик события нажатия кнопки **/
        okTestWord.addClickListener(clickEvent -> {

            /** При нажатии на кнопку ok, проверяется валидация строки и пишется инфомрация в лог **/
            String regExp = RegexpLayout.getRegExp();
            boolean isValidate = Validator.isValidate(testWord.getValue(),regExp);
            log.info("Валидация isValidate : {}",isValidate);
            if (isValidate) {
                logViewPanel.setContentLogViewPanel(dateTimeFormatter.format(LocalDateTime.now(ZoneId.systemDefault())) + " : " + " строка \"" + testWord.getValue() + "\" успешно прошла валидацию!" + "\nRegex: " + regExp + "\n" + logViewPanel.getContentLogViewPanel());
            } else {
                logViewPanel.setContentLogViewPanel(dateTimeFormatter.format(LocalDateTime.now(ZoneId.systemDefault())) + " : " + " строка \"" + testWord.getValue() + "\" не прошла валидацию!" + "\nRegex: " + regExp + "\n" + logViewPanel.getContentLogViewPanel());
            }
        });
    }

    /** Блок отвечающий за тестируемое слово **/
    private void initTextField() {
        testWord = new TextField();
        testWord.setEnabled(false);
        testWord.setInputPrompt("Введите строку для проверки");
        testWord.setWidth(350, Sizeable.Unit.PIXELS);
        testWord.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);
        testWord.addFocusListener(focusEvent -> {
            okTestWord.setClickShortcut(ShortcutAction.KeyCode.ENTER);
            log.info("У кнопки okTestWord включен ClickShortcut : ENTER");
        });

    }

    /** Метод который возвращает кнопку ок **/
    public Button getButtonOkTestWord() {
        return okTestWord;
    }

    /** Метод который возвращает кнопку поле ввода тестового слова.**/
    public TextField getTextFieldTestWord() {
        return testWord;
    }

}
