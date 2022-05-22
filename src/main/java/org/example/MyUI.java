package org.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.PreserveOnRefresh;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.*;
import com.vaadin.ui.*;

import static com.vaadin.server.Constants.SERVLET_PARAMETER_HEARTBEAT_INTERVAL;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@PreserveOnRefresh
public class MyUI extends UI {
    private static Logger log = LogManager.getLogger(MyUI.class);


    @Override
    protected void init(VaadinRequest vaadinRequest) {

        VaadinSession.getCurrent().getSession().setMaxInactiveInterval(1500);

        VerticalLayout layout = new VerticalLayout();
        LogViewPanel logViewPanel = new LogViewPanel(); //Панель логирования для тектового поля.
        TestWorldLayout testWorldLayout = new TestWorldLayout(logViewPanel); //Layout для тектового поля

        /** Layout самостоятельного поля валидации. **/
        TextValidationPanel textValidationPanel = new TextValidationPanel(testWorldLayout.getButtonOkTestWord());


        /** Layout для поля с регулярным выражением, в который я передаю поле ввода сетового слова и кнопку ок,
         * для дальнейшего управления этими элементами  **/
        RegexpLayout regexpLayout = new RegexpLayout(testWorldLayout.getButtonOkTestWord(), testWorldLayout.getTextFieldTestWord());

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

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true, initParams = @WebInitParam(name = SERVLET_PARAMETER_HEARTBEAT_INTERVAL, value = "10"))
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false, closeIdleSessions = true)
    public static class MyUIServlet extends VaadinServlet implements SessionDestroyListener {
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

        @Override
        public void sessionDestroy(SessionDestroyEvent event) {
            VaadinService.getCurrentRequest().getWrappedSession().invalidate();
            log.info("SESSION INVALID");
        }
    }
}
