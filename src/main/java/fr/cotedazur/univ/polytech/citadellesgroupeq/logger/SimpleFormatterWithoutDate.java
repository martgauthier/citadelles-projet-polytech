package fr.cotedazur.univ.polytech.citadellesgroupeq.logger;

import java.util.logging.Formatter;
import java.util.logging.*;

public class SimpleFormatterWithoutDate extends Formatter {
    public static final String ANSI_WHITE = "\u001B[37m";
    @Override
    public String format(LogRecord rec) {
        String message = rec.getMessage();
        Object[] params = rec.getParameters();

        if (params != null) {
            for (Object param : params) {
                message = message.replaceFirst("\\{\\}", String.valueOf(param));
            }
        }

        return ANSI_WHITE + message + "\n";
    }
}
