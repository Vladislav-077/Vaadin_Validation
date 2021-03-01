package org.example;

import com.vaadin.external.org.slf4j.Marker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.client.AceAnnotation;
import org.vaadin.aceeditor.client.AceMarker;
import org.vaadin.aceeditor.client.AceRange;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Validator{
    private static Logger log = LogManager.getLogger(Validator.class);
    private static Pattern pattern;
    private static Matcher matcher;
    private static long latestErrorMarkerId = 0L;

    private Validator() {}

    public static boolean isValidate(String testWord,String regexp) {

        pattern = Pattern.compile(regexp);
        matcher = pattern.matcher(testWord);
        boolean isValidate = matcher.find();

        log.info("\n\n");
        log.info("Тестовая строка : {}",testWord);
        log.info("Регулярное выражение : {}",pattern.pattern());
        log.info(isValidate ?
                "I found '"+ matcher.group()+
                        "' start at index "+matcher.start()+
                        " and ending at index '"+matcher.end() + "." :
                "I found nothing !");

        return isValidate;
    }

    public static void isValidate(AceEditor aceEditor, String regexp) {
        aceEditor.clearMarkerAnnotations();
        aceEditor.clearMarkers();

        String text = aceEditor.getValue();
        pattern = Pattern.compile(regexp);
        matcher = pattern.matcher(text);
        /** Если проверяемый текст не соотвествует регулярному выражению **/
        for (int i = 0; i < text.length() && !matcher.find(i); i++) {
            AceRange range = AceRange.fromPositions(0,text.length(), text);
            AceMarker m = new AceMarker(newErrorMarkerId(), range, "errorMarker", AceMarker.Type.text, false, AceMarker.OnTextChange.ADJUST);
            aceEditor.addMarker(m);


            // FIXME : Анотации работают не корректно, выявленны сделующие проблемы.
            // 1. При указании регулярного выражения \s* - пробелы подчеркиваются красными точками и возникает анотация..
            // 2. Если строка не подчеркивается как ошибка, анотация все равно проставляется.
            AceAnnotation ann = new AceAnnotation("Запрещено использовать : (" + text + ")", AceAnnotation.Type.error);
            aceEditor.addMarkerAnnotation(ann,m);
            log.info("I found {}",text);
        }
        /** Если проверяемый текст соотвествует регулярному выражению **/
        for (int i = 0; i < text.length() && matcher.find(i); i++) {
            AceRange range = AceRange.fromPositions(0,text.length(), text);
            AceMarker m = new AceMarker(newErrorMarkerId(), range, "noErrorMarker", AceMarker.Type.text, false, AceMarker.OnTextChange.ADJUST);
            aceEditor.addMarker(m);


            // FIXME : Анотации работают не корректно, выявленны сделующие проблемы.
            // 1. При указании регулярного выражения \s* - пробелы подчеркиваются красными точками и возникает анотация..
            // 2. Если строка не подчеркивается как ошибка, анотация все равно проставляется.
            AceAnnotation ann = new AceAnnotation("Можно использовать : (" + text + ")", AceAnnotation.Type.warning);
            aceEditor.addMarkerAnnotation(ann,m);
            log.info("I found {}",text);
        }
    }

    private static String newErrorMarkerId() {
        return "e" + (++latestErrorMarkerId);
    }



    public static boolean checkRegExpValidate(String regexp) {
        try {
            Pattern.compile(regexp);
            return true;
        }
        catch (PatternSyntaxException e) {
            return false;
        }
    }
}
