package org.example;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class LogViewPanel extends VerticalLayout {
    private static final Panel logViewPanel = new Panel();

    public LogViewPanel(Label content) {
        logViewPanel.setCaption("Панель логирования");
        logViewPanel.setWidth(40, Unit.EM);
        logViewPanel.setHeight(30, Unit.EM);
        logViewPanel.setContent(content);
        Button logViewClearButton = buttonINIT(content);
        addComponents(logViewPanel,logViewClearButton);
        this.setSpacing(true);
    }

    public LogViewPanel(String caption, Label content, int width, int height) {
        logViewPanel.setCaption(caption);
        logViewPanel.setWidth(width, Unit.EM);
        logViewPanel.setHeight(height, Unit.EM);
        logViewPanel.setContent(content);
        buttonINIT(content);
    }

    private Button buttonINIT(Label logView) {
        /** Кнопка ок отвечает за созранение и дальнейшее использование регулярного выражения. **/
        final Button logClear = new Button("Очистить", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                logView.setValue("");
            }
        });
        logClear.addStyleName(ValoTheme.BUTTON_DANGER);
        logClear.setClickShortcut(ShortcutAction.KeyCode.DELETE);
        logClear.setIcon(FontAwesome.CODE);
        return logClear;
    }
}
