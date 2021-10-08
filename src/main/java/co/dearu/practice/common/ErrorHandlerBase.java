package co.dearu.practice.common;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorHandlerBase {

    public static final int RestErrorHandlerOrder = 0;
    public static final int HtmlErrorHandlerOrder = 1;
    /*
     * StackTrace 를 String 으로 변환
     */
    protected String stackTraceToString(Exception ex) {
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }
}
