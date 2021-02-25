package org.example;


import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.components.colorpicker.ColorChangeEvent;
import com.vaadin.ui.components.colorpicker.ColorChangeListener;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceMode;
import org.vaadin.aceeditor.AceTheme;
import org.vaadin.aceeditor.TextRange;
import org.vaadin.aceeditor.client.AceAnnotation;
import org.vaadin.aceeditor.client.AceMarker;
import org.vaadin.aceeditor.client.AceRange;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextValidationPanel extends VerticalLayout {
    private Button ok;
    private static final Panel logViewPanel = new Panel();
    private static AceEditor aceEditor;
    private long latestErrorMarkerId = 0L;
    private Pattern pattern;

    private String newErrorMarkerId() {
        return "e" + (++latestErrorMarkerId);
    }

    public TextValidationPanel(TextField testWord, Button ok) {
        this.ok = ok;
        aceEditor = aceEditorINIT();
        logViewPanel.setCaption("Панель проверки валидации");
        logViewPanel.setWidth(40, Unit.EM);
        logViewPanel.setHeight(30, Unit.EM);
        logViewPanel.setContent(aceEditor);
        Button buttonClearText = buttonINIT(aceEditor);
//        ColorPicker colorPicker = getColor();
//        TextArea textArea = textAreaINIT();
//        RichTextArea richTextArea = richTextAreaINIT();
//        MyErrorChecker checker = new MyErrorChecker();
//        checker.attachTo(aceEditor);
        addComponents(logViewPanel, buttonClearText);
        setSpacing(true);
    }

    /**
     * Устанавливаем регулярное выражение.
     **/
    public void setPattern(String regexp) {
        pattern = Pattern.compile(regexp);
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

        aceEditor.addTextChangeListener(textChangeEvent -> {
            checkError(aceEditor.getValue());
            Notification.show(pattern.pattern(), Notification.Type.TRAY_NOTIFICATION);
        });

        return aceEditor;
    }

    private void checkError(String text) {
        aceEditor.clearMarkerAnnotations();
        aceEditor.clearMarkers();

        Matcher matcher = pattern.matcher(text);
        int i = 0;
        while (i < text.length() && matcher.find(i)) {
            i = matcher.end() + 1;
            AceRange range = AceRange.fromPositions(matcher.start(), matcher.end(), text);
            AceMarker m = new AceMarker(newErrorMarkerId(), range, "myerrormarker1", AceMarker.Type.text, false, AceMarker.OnTextChange.ADJUST);
            aceEditor.addMarker(m);


            // FIXME : Анотации работают не корректно, выявленны сделующие проблемы.
            // 1. При указании регулярного выражения \s* - пробелы подчеркиваются красными точками и возникает анотация..
            // 2. Если строка не подчеркивается как ошибка, анотация все равно проставляется.
            AceAnnotation ann = new AceAnnotation("Запрещено использовать : (" + matcher.group() + ")", AceAnnotation.Type.error);
            aceEditor.addMarkerAnnotation(ann,m);
        }

    }

    public void setAceEditorEnable(boolean isEnable) {
//        aceEditor.setEnabled(isEnable);
    }


    //FIXME : в TextArea наблюдается неприятная проблама скорола, возможно вызванна методом setWordwrap() и неопределенными размерами.
    private TextArea textAreaINIT() {
        TextArea textArea = new TextArea();
        textArea.setStyleName("coloredtextarea");
        textArea.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.LAZY);
        textArea.setTextChangeTimeout(400);
        textArea.setWordwrap(false); // Перенос слова
        textArea.setSizeFull();
        return textArea;
    }

    private RichTextArea richTextAreaINIT() {
        RichTextArea richTextArea = new RichTextArea("richTextArea");
        richTextArea.setStyleName("coloredtextarea");
        richTextArea.setValue("Тест");
        richTextArea.setReadOnly(false);

        richTextArea.setSizeFull();
        return richTextArea;
    }

    public ColorPicker getColor() {
        // Some UI logic to change CSS
        ColorPicker colorPicker = new ColorPicker("Установите цвет тут");
        colorPicker.addColorChangeListener(new ColorChangeListener() {
            @Override
            public void colorChanged(ColorChangeEvent event) {
                Page.getCurrent().getStyles().add(
                        ".v-textarea-coloredtextarea {color: " +
                                event.getColor().getCSS() + " !important;}");
            }
        });
        return colorPicker;
    }

    private void newAceMarker(AceEditor aceEditor) {
        String cssClass = "mymarker1";
        TextRange range = aceEditor.getSelection();
        AceMarker.Type type = AceMarker.Type.text;
        boolean inFront = false; // whether in front or behind the text
        AceMarker.OnTextChange onChange = AceMarker.OnTextChange.ADJUST;
        String markerId = aceEditor.addMarker(range, cssClass, type, inFront, onChange);
//        aceEditor.removeMarker(markerId);
    }

}
