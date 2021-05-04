Vaadin_TextFieldValidation
==============

Template for a simple Vaadin application that only requires a Servlet 3.0 container to run.


Workflow
========

To compile the entire project, run "mvn install".

To run the application, run "mvn jetty:run" and open http://localhost:8080/ .

To produce a deployable production mode WAR:
- change productionMode to true in the servlet class configuration (nested in the UI class)
- run "mvn clean package"
- test the war file with "mvn jetty:run-war"

Тестовое приложение для проверки введенной информации по заданному регулярному выражению 

![Screenshot](src/main/resources/image/VaadinValidate.PNG)
