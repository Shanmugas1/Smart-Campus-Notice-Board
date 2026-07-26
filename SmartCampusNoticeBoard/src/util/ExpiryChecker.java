package util;

import repository.NoticeRepository;

// Demonstrates threading: periodically scans and removes expired notices
public class ExpiryChecker extends Thread {
    private NoticeRepository repository;
    private volatile boolean running = true;
    private final int intervalMillis;

    public ExpiryChecker(NoticeRepository repository, int intervalMillis) {
        this.repository = repository;
        this.intervalMillis = intervalMillis;
        this.setDaemon(true); // so it doesn't block JVM shutdown
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(intervalMillis);
                int removed = repository.removeExpired();
                if (removed > 0) {
                    FileManager.log(removed + " expired notice(s) auto-removed.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running = false;
            }
        }
    }

    public void stopChecking() {
        running = false;
        this.interrupt();
    }
}
