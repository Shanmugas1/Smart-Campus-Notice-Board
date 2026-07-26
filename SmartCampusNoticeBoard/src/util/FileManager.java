package util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class FileManager {
    private static final String LOG_PATH = "data/logs.txt";

    public static void log(String message) {
        try (FileWriter fw = new FileWriter(LOG_PATH, true)) {
            fw.write("[" + LocalDateTime.now() + "] " + message + System.lineSeparator());
        } catch (IOException e) {
            // Logging failure should not crash the app; print to console instead
            System.out.println("Warning: could not write to log file (" + e.getMessage() + ")");
        }
    }
}
