package org.example;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class LogViewPanel extends VerticalLayout {
    private Panel logViewPanel = new Panel();
    private Label contentLogPanelLabel = new Label("", ContentMode.PREFORMATTED);

    public LogViewPanel() {
        logViewPanel.setCaption("Панель логирования");
        logViewPanel.setWidth(40, Unit.EM);
        logViewPanel.setHeight(30, Unit.EM);
        logViewPanel.setContent(contentLogPanelLabel);

        Button logViewClearButton = buttonINIT(contentLogPanelLabel);
        addComponents(logViewPanel,logViewClearButton);
        this.setSpacing(true);
    }

    public LogViewPanel(String caption, int width, int height) {
        logViewPanel.setCaption(caption);
        logViewPanel.setWidth(width, Unit.EM);
        logViewPanel.setHeight(height, Unit.EM);
        logViewPanel.setContent(contentLogPanelLabel);
        buttonINIT(contentLogPanelLabel);
    }

    /** Кнопка ок отвечает за созранение и дальнейшее использование регулярного выражения. **/
    private Button buttonINIT(Label logView) {
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

    public void setContentLogViewPanel(String contentLogString) {
        contentLogPanelLabel.setValue(contentLogString);
    }

    public String getContentLogViewPanel() {
        return contentLogPanelLabel.getValue();
    }
}
