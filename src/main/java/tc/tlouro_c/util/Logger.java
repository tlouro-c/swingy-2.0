package tc.tlouro_c.util;
import tc.tlouro_c.config.Config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final boolean ON = Config.getInstance().isLoggingOn();

    public static void info(String message) {
        log("INFO", message);
    }

    public static void debug(String message) {
        log("DEBUG", message);
    }

    public static void error(String message) {
        log("ERROR", message);
    }

    public static void error(String message, Throwable throwable) {
        log("ERROR", message);
        throwable.printStackTrace(System.err);
    }

    private static void log(String level, String message) {
        if (!ON) {
            return;
        }
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.printf("%s [%s] %s%n", timestamp, level, message);
    }
}
