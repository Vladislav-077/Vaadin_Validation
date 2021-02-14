package org.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.*;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
//@StyleSheet("marker.css")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        TextField testWord = new TextField();
        final Button ok = new Button("ОК");
        final VerticalLayout layout = new VerticalLayout();
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        final Label logView = new Label("", ContentMode.PREFORMATTED);
        final TextField testRegexp = new TextField();
        final Button edit = new Button("Edit");

        final TextValidationPanel textValidationPanel = new TextValidationPanel(testWord,ok);
        textValidationPanel.setEnabled(false);
        textValidationPanel.setAceEditorEnable(textValidationPanel.isEnabled());

        /** Блок отвечающий за тестируемое слово **/
        testWord.setInputPrompt("Введите строку для проверки");
        testWord.setEnabled(false);
        testWord.setWidth(350, Unit.PIXELS);
        testWord.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);

        /** Кнопка ок отвечает за созранение и дальнейшее использование тестового слова. **/
        final Button okTestWord = new Button("ОК", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                testWord.addValidator(new RegexpValidator(testRegexp.getValue(), "Error...")); // Добавляем новый валидатор.
                if (!testWord.isValid()) {
                    System.out.println("Текстовая строка не прошла валидацию!");
                    logView.setValue(dateTimeFormatter.format(LocalDateTime.now(ZoneId.systemDefault())) + " : " + " строка \"" + testWord.getValue() + "\" не прошла валидацию!" + "\nRegex: " + testRegexp.getValue() + "\n" + logView.getValue());
                } else if (testWord.isValid()) {
                    testWord.setComponentError(null);
                    System.out.println("Валидация прошла успешно!");
                    logView.setValue(dateTimeFormatter.format(LocalDateTime.now(ZoneId.systemDefault())) + " : " + " строка \"" + testWord.getValue() + "\" успешно прошла валидацию!" + "\nRegex: " + testRegexp.getValue() + "\n" + logView.getValue());
                }
                testWord.removeAllValidators(); // Удаляем все валидаторы
            }
        });
        okTestWord.setEnabled(false);
        okTestWord.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        okTestWord.setIcon(FontAwesome.SEND);


        /** Слой для объединения тектового поля , кнопки ok **/
        final HorizontalLayout testWordLayout = new HorizontalLayout(testWord, okTestWord);
        testWordLayout.setComponentAlignment(testWord, Alignment.MIDDLE_CENTER);
        testWordLayout.setComponentAlignment(okTestWord, Alignment.MIDDLE_CENTER);
        testWordLayout.setSpacing(true);


        /** Отдельный блок для редактирования регулярного выражения. **/

        testRegexp.setInputPrompt("Введите здесь свою регулярное выражение");
        testRegexp.setWidth(350, Unit.PIXELS);
        testRegexp.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.TIMEOUT);
        testRegexp.setValidationVisible(true);
        testRegexp.setTextChangeTimeout(5);

        /** Кнопка ок отвечает за созранение и дальнейшее использование регулярного выражения. **/
        ok.addClickListener(clickEvent -> {
            textValidationPanel.setPattern(testRegexp.getValue()); // Отправляем паттерн в TextValidationPanel.
            testRegexp.setEnabled(false);
            testWord.setEnabled(true);
            okTestWord.setEnabled(true);
            ok.setEnabled(false);
            edit.setEnabled(true);
            textValidationPanel.setEnabled(true);
            textValidationPanel.setAceEditorEnable(true);
        });
        ok.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        ok.setIcon(FontAwesome.SEND);

        /** Кнопка edit отвечает за редактирование регулярного выражения **/
        edit.addClickListener(clickEvent -> {
            testRegexp.setEnabled(true);
            okTestWord.setEnabled(false);
            testWord.setEnabled(false);
            ok.setEnabled(true);
            edit.setEnabled(false);
            textValidationPanel.setEnabled(false);
            textValidationPanel.setAceEditorEnable(true);
        });
        edit.addStyleName(ValoTheme.BUTTON_PRIMARY);
        edit.setIcon(FontAwesome.EDIT);

        /** Слой для объединения тектового поля - рег.выражения, кнопки ok, и кнопки edit **/
        final HorizontalLayout regexpLayout = new HorizontalLayout(testRegexp, ok, edit);
        regexpLayout.setComponentAlignment(testRegexp, Alignment.MIDDLE_CENTER);
        regexpLayout.setComponentAlignment(ok, Alignment.MIDDLE_CENTER);
        regexpLayout.setComponentAlignment(edit, Alignment.MIDDLE_CENTER);
        regexpLayout.setSpacing(true);


        LogViewPanel logViewPanel = new LogViewPanel(logView);

        /** Слой для объединения двух панелей */
        HorizontalLayout panelUnitLayout = new HorizontalLayout(logViewPanel, textValidationPanel);
        panelUnitLayout.setSpacing(true);

        layout.addComponents(testWordLayout, regexpLayout, panelUnitLayout);
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);


        this.getReconnectDialogConfiguration().setDialogModal(false);
        this.getReconnectDialogConfiguration().setDialogText("Соединение с сервером потеряно, попытка переподключения ...");

    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {

        @Override
        protected void servletInitialized() throws ServletException {

            this.getService().setSystemMessagesProvider(new SystemMessagesProvider() {

                @Override
                public SystemMessages getSystemMessages(SystemMessagesInfo systemMessagesInfo) {
                    CustomizedSystemMessages customizedSystemMessages = new CustomizedSystemMessages();
                    customizedSystemMessages.setSessionExpiredCaption("Сессия истекла");
                    customizedSystemMessages.setSessionExpiredMessage("Обратите внимание на все несохраненные данные и <u> щелкните здесь </u> или нажмите клавишу ESC, чтобы продолжить.");
                    return customizedSystemMessages;
                }
            });
        }

    }
}
