package org.example;


import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceMode;
import org.vaadin.aceeditor.AceTheme;

public class TextValidationPanel extends VerticalLayout {
    private static Logger log = LogManager.getLogger(TextValidationPanel.class);
    private Button ok;
    private static final Panel logViewPanel = new Panel();
    private static AceEditor aceEditor;
    private static Button buttonClearText;
    private Button okTestWord;

    public TextValidationPanel(Button okTestWord) {
        this.okTestWord = okTestWord;
        aceEditor = aceEditorINIT();
        logViewPanel.setCaption("Панель проверки валидации");
        logViewPanel.setWidth(40, Unit.EM);
        logViewPanel.setHeight(30, Unit.EM);
        logViewPanel.setContent(aceEditor);
        buttonClearText = buttonINIT(aceEditor);
        addComponents(logViewPanel, buttonClearText);
        setSpacing(true);
    }


    private Button buttonINIT(AbstractField logView) {
        /** Кнопка ок отвечает за созранение и дальнейшее использование регулярного выражения. **/
        final Button logClear = new Button("Очистить", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                logView.setValue("");
                aceEditor.clearMarkerAnnotations();
                aceEditor.clearMarkers();
            }
        });
        logClear.addStyleName(ValoTheme.BUTTON_DANGER);
        logClear.setClickShortcut(ShortcutAction.KeyCode.DELETE);
        logClear.setIcon(FontAwesome.CODE);

        return logClear;
    }

    private AceEditor aceEditorINIT() {
        AceEditor aceEditor = new AceEditor();
        aceEditor.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.LAZY);
        aceEditor.setMode(AceMode.text);
        aceEditor.setTheme(AceTheme.chrome);
        aceEditor.setFontSize("15px");
        aceEditor.setWordWrap(true);
        aceEditor.setSizeFull();

        aceEditor.addFocusListener(focusEvent -> {
            okTestWord.removeClickShortcut();
            log.info("У кнопки okTestWord отключен ClickShortcut : ENTER");

        });

        aceEditor.addTextChangeListener(textChangeEvent -> {
            if (RegexpLayout.getRegExp() != null && Validator.checkRegExpValidate(RegexpLayout.getRegExp())) {
                Validator.isValidate(aceEditor,RegexpLayout.getRegExp());
            }
        });
        return aceEditor;
    }

}
