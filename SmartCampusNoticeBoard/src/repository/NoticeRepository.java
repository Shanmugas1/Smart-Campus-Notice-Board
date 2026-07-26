package repository;

import model.Notice;
import java.util.ArrayList;
import java.util.List;

public class NoticeRepository {
    private List<Notice> notices;
    private int nextId;

    public NoticeRepository() {
        notices = new ArrayList<>();
        nextId = 1;
    }

    public int getNextId() {
        return nextId++;
    }

    public void save(Notice notice) {
        notices.add(notice);
    }

    public List<Notice> findAll() {
        return notices;
    }

    public Notice findById(int id) {
        for (Notice n : notices) {
            if (n.getNoticeId() == id) {
                return n;
            }
        }
        return null;
    }

    public boolean deleteById(int id) {
        return notices.removeIf(n -> n.getNoticeId() == id);
    }

    public int removeExpired() {
        int before = notices.size();
        notices.removeIf(Notice::isExpired);
        return before - notices.size();
    }
}
