package service;

import exception.InvalidNoticeException;
import model.Notice;
import repository.NoticeRepository;
import util.FileManager;
import util.Validation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NoticeService {
    private NoticeRepository repository;

    public NoticeService(NoticeRepository repository) {
        this.repository = repository;
    }

    public Notice addNotice(String title, String description, String category,
                             String postedBy, LocalDate postDate, LocalDate expiryDate)
            throws InvalidNoticeException {

        if (Validation.isNullOrEmpty(title)) {
            throw new InvalidNoticeException("Title cannot be empty.");
        }
        if (!Validation.isValidCategory(category)) {
            throw new InvalidNoticeException("Invalid category. Use EXAM, EVENT, PLACEMENT, HOLIDAY, or GENERAL.");
        }
        if (!Validation.isValidDateRange(postDate, expiryDate)) {
            throw new InvalidNoticeException("Expiry date cannot be before post date.");
        }

        int id = repository.getNextId();
        Notice notice = new Notice(id, title, description, category.toUpperCase(), postedBy, postDate, expiryDate);
        repository.save(notice);
        FileManager.log("Notice added: [" + id + "] " + title);
        return notice;
    }

    public boolean updateNotice(int id, String newTitle, String newDescription, String newCategory)
            throws InvalidNoticeException {
        Notice notice = repository.findById(id);
        if (notice == null) {
            throw new InvalidNoticeException("Notice with ID " + id + " not found.");
        }
        if (!Validation.isNullOrEmpty(newTitle)) {
            notice.setTitle(newTitle);
        }
        if (!Validation.isNullOrEmpty(newDescription)) {
            notice.setDescription(newDescription);
        }
        if (!Validation.isNullOrEmpty(newCategory)) {
            if (!Validation.isValidCategory(newCategory)) {
                throw new InvalidNoticeException("Invalid category.");
            }
            notice.setCategory(newCategory.toUpperCase());
        }
        FileManager.log("Notice updated: [" + id + "]");
        return true;
    }

    public boolean deleteNotice(int id) throws InvalidNoticeException {
        boolean deleted = repository.deleteById(id);
        if (!deleted) {
            throw new InvalidNoticeException("Notice with ID " + id + " not found.");
        }
        FileManager.log("Notice deleted: [" + id + "]");
        return true;
    }

    public List<Notice> getAllNotices() {
        return repository.findAll();
    }

    // Polymorphism: method overloading — search by category
    public List<Notice> searchNotice(String category) {
        List<Notice> results = new ArrayList<>();
        for (Notice n : repository.findAll()) {
            if (n.getCategory().equalsIgnoreCase(category)) {
                results.add(n);
            }
        }
        return results;
    }

    // Polymorphism: method overloading — search by date
    public List<Notice> searchNotice(LocalDate date) {
        List<Notice> results = new ArrayList<>();
        for (Notice n : repository.findAll()) {
            if (n.getPostDate().equals(date)) {
                results.add(n);
            }
        }
        return results;
    }

    // Polymorphism: method overloading — search by keyword in title/description
    public List<Notice> searchNotice(String keyword, boolean byKeyword) {
        List<Notice> results = new ArrayList<>();
        String lower = keyword.toLowerCase();
        for (Notice n : repository.findAll()) {
            if (n.getTitle().toLowerCase().contains(lower)
                    || n.getDescription().toLowerCase().contains(lower)) {
                results.add(n);
            }
        }
        return results;
    }
}
