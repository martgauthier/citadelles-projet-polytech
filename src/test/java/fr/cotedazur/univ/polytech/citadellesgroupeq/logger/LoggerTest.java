package fr.cotedazur.univ.polytech.citadellesgroupeq.logger;

import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoggerTest {
    @Test
    void testIsInstanceOfALogger() {
        assertNotNull(EasyLogger.getLogger("test"));
    }

    @Test
    void testUsesSimpleFormatterWithoutDate() {
        EasyLogger logger = EasyLogger.getLogger("test");
        assertTrue(logger.getHandlers()[0].getFormatter() instanceof SimpleFormatterWithoutDate);
    }
}
