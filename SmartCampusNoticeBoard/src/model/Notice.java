package model;

import java.time.LocalDate;

public class Notice {
    private int noticeId;
    private String title;
    private String description;
    private String category;   // e.g. EXAM, EVENT, PLACEMENT, HOLIDAY, GENERAL
    private String postedBy;
    private LocalDate postDate;
    private LocalDate expiryDate;

    public Notice(int noticeId, String title, String description, String category,
                  String postedBy, LocalDate postDate, LocalDate expiryDate) {
        this.noticeId = noticeId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.postedBy = postedBy;
        this.postDate = postDate;
        this.expiryDate = expiryDate;
    }

    // Encapsulation: private fields, public getters/setters
    public int getNoticeId() { return noticeId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPostedBy() { return postedBy; }

    public LocalDate getPostDate() { return postDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    @Override
    public String toString() {
        return "-------------------------------------\n" +
               "Notice ID   : " + noticeId + "\n" +
               "Title       : " + title + "\n" +
               "Category    : " + category + "\n" +
               "Description : " + description + "\n" +
               "Posted By   : " + postedBy + "\n" +
               "Posted On   : " + postDate + "\n" +
               "Expires On  : " + expiryDate + "\n" +
               "Status      : " + (isExpired() ? "EXPIRED" : "ACTIVE") +
               "\n-------------------------------------";
    }
}
