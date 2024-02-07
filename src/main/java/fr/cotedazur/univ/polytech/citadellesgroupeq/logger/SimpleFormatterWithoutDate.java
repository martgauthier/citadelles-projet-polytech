package fr.cotedazur.univ.polytech.citadellesgroupeq.logger;

import java.util.logging.Formatter;
import java.util.logging.*;

/**
 * Implémentation de {@link Formatter} qui n'affiche pas d'informations inutiles,
 * et qui supporte l'ajout de variables dans le texte avec "{}", à la manière de "format" en python
 */
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
