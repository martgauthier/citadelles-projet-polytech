package fr.cotedazur.univ.polytech.citadellesgroupeq.logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FormatterTest {
    LogRecord record;
    LogRecord recordWithParams;
    Formatter simpleFormatter;

    @BeforeEach
    void setup() {
        record=new LogRecord(Level.INFO, "le test de log.");
        recordWithParams=new LogRecord(Level.INFO, "test param {} {}");

        recordWithParams.setParameters(new Object[]{1, "hehe"});

        simpleFormatter=new SimpleFormatterWithoutDate();
    }

    @Test
    void testBasicString() {
        assertEquals(SimpleFormatterWithoutDate.ANSI_WHITE + "le test de log.\n", simpleFormatter.format(record));
    }

    @Test
    void testParams() {
        assertEquals(SimpleFormatterWithoutDate.ANSI_WHITE + "test param 1 hehe\n", simpleFormatter.format(recordWithParams));
    }
}
