package org.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.*;
import com.vaadin.ui.*;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
    private static Logger log = LogManager.getLogger(MyUI.class);


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        final LogViewPanel logViewPanel = new LogViewPanel(); //Панель логирования для тектового поля.
        final TestWorldLayout testWorldLayout = new TestWorldLayout(logViewPanel); //Layout для тектового поля

        /** Layout самостоятельного поля валидации. **/
        final TextValidationPanel textValidationPanel = new TextValidationPanel(testWorldLayout.getButtonOkTestWord());


        /** Layout для поля с регулярным выражением, в который я передаю поле ввода сетового слова и кнопку ок,
         * для дальнейшего управления этими элементами  **/
        final RegexpLayout regexpLayout = new RegexpLayout(testWorldLayout.getButtonOkTestWord(),testWorldLayout.getTextFieldTestWord());

        /** Слой для объединения двух панелей */
        HorizontalLayout panelUnitLayout = new HorizontalLayout(logViewPanel,textValidationPanel);
        panelUnitLayout.setSpacing(true);


        layout.addComponents(testWorldLayout,regexpLayout,panelUnitLayout);
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
