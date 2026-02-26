package game.engine;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * GameLogger: Handles asynchronous logging of game events.
 * Events are written to log.txt using a dedicated thread.
 */
public class GameLogger {

    private static final BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();
    private static final String LOG_FILE = "log.txt";
    private static volatile boolean running = true;

    static {
        Thread loggerThread = new Thread(() -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
                while (running || !logQueue.isEmpty()) {
                    String message = logQueue.take();
                    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    writer.write("[" + timestamp + "] " + message);
                    writer.newLine();
                    writer.flush();
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("Logger stopped: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }, "GameLoggerThread");
        loggerThread.setDaemon(true);
        loggerThread.start();
    }

    /**
     * Adds a log entry to the queue.
     * @param message the log message
     */
    public static void log(String message) {
        if (running) {
            logQueue.offer(message);
        }
    }

    /**
     * Shuts down the logger gracefully.
     */
    public static void shutdown() {
        running = false;
    }
}
