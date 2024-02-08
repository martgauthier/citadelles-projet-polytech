package fr.cotedazur.univ.polytech.citadellesgroupeq.logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;


/**
 * Class that easily creates a logger, that will not display unnecessary infos like date and class from where it is called.
 */

public class EasyLogger extends Logger {

    /**
     * Unused but mandatory.
     */
    protected EasyLogger(String name, String resourceBundleName) {
        super(name, resourceBundleName);
    }

    /**
     * Creates an easy logger, that doesn't display useless stuff.
     * @param name
     * @return
     */
    public static EasyLogger getLogger(String name) {
        return getLogger(name, false);
    }


    /**
     * Creates an easy logger ready to use.
     * @param name
     * @param displayContext false if you don't want dates and classes.
     * @return
     */
    public static EasyLogger getLogger(String name, boolean displayContext) {
        EasyLogger logger = new EasyLogger(name, null);

        if(!displayContext) {
            logger.setUseParentHandlers(false);
            ConsoleHandler handler=new ConsoleHandler();

            handler.setFormatter(new SimpleFormatterWithoutDate());

            logger.addHandler(handler);
        }

        return logger;
    }
}
