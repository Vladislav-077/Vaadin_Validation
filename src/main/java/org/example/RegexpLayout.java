package org.example;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.server.UserError;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class RegexpLayout  extends HorizontalLayout{
    private static String regExp;
    private TextField testRegexp;
    private Button ok;
    private Button edit;
    private TextField testWord;
    private Button okTestWord;
//    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
//    final Label logView = new Label("", ContentMode.PREFORMATTED);
//

    public RegexpLayout(Button okTestWord,TextField testWord ) {
        this.okTestWord = okTestWord;
        this.testWord = testWord;

        initTextFieldRegExp(); /** Тектовое поля содержащее регулярное выражение **/
        initButtonOK(); /** Инициализация кнопки ок **/
        initButtonEdit(); /** Инициализация кнопки редактирования **/

        /** Слой для объединения тектового поля - рег.выражения, кнопки ok, и кнопки edit **/
        this.addComponents(testRegexp, ok, edit);
        this.setComponentAlignment(testRegexp, Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(ok, Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(edit, Alignment.MIDDLE_CENTER);
        this.setSpacing(true);
    }


    /** Тектовое поля содержащее регулярное выражение **/
    private void initTextFieldRegExp() {
        testRegexp = new TextField();
        testRegexp.addValidator(new StringLengthValidator("Для дальнейшей проверки поле не может быть пустым",1,100,false));
        testRegexp.setInputPrompt("Введите здесь свою регулярное выражение");
        testRegexp.setWidth(350, Sizeable.Unit.PIXELS);
        testRegexp.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.TIMEOUT);
        testRegexp.setValidationVisible(true);
        testRegexp.setTextChangeTimeout(5);

        testRegexp.addValueChangeListener(valueChangeEvent -> {
            if (testRegexp.isValid()) {
                if (Validator.checkRegExpValidate(testRegexp.getValue())) {
                    ok.setEnabled(true);
                    testRegexp.setComponentError(null);
                }
                else {
                    ok.setEnabled(false);
                    testRegexp.setComponentError(new UserError("Регулярное выражение не прошло валидацию !"));
                    Notification.show("Регулярное выражение не прошло валидацию !", Notification.Type.ERROR_MESSAGE);
                }
            }
            else {
                ok.setEnabled(false);
            }
        });

    }

    private void initButtonOK() {
        ok = new Button("ОК");
        ok.setEnabled(false);
        /** Кнопка ок отвечает за созранение и дальнейшее использование регулярного выражения. **/
        ok.addClickListener(clickEvent -> {
            //Fixme: костыль, не смог придумать как передать регулярное выражеие в класс TestWorldLayout
            setRegExp(testRegexp.getValue());
            testRegexp.setEnabled(false);
            okTestWord.setEnabled(true);
            testWord.setEnabled(true);
            ok.setEnabled(false);
            edit.setEnabled(true);
            Notification.show("Регулярное выражение : "+testRegexp.getValue() + " принято !", Notification.Type.ASSISTIVE_NOTIFICATION);
//            TextValidationPanel.setVisibleComponent(true);
        });
        ok.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        ok.setIcon(FontAwesome.SEND);
    }

    private void initButtonEdit(){
        edit = new Button("Edit");
        /** Кнопка edit отвечает за редактирование регулярного выражения **/
        edit.addClickListener(clickEvent -> {
            testRegexp.setEnabled(true);
            okTestWord.setEnabled(false);
            testWord.setEnabled(false);

            ok.setEnabled(true);
            edit.setEnabled(false);
//            TextValidationPanel.setVisibleComponent(false);
        });
        edit.addStyleName(ValoTheme.BUTTON_PRIMARY);
        edit.setIcon(FontAwesome.EDIT);
    }

    public static String getRegExp() {
        return regExp;
    }

    public void setRegExp(String regExpNew) {
        regExp = regExpNew;
    }


}
