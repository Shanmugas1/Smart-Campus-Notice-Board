# Smart Campus Notice Board System

A Java-based console application designed to digitize and automate notice board management across a campus. It allows administrators to post, update, and manage official notices while providing students access to relevant announcements with automated validity checking.

---

## 📌 Features

* **User Authentication & Roles:** Secure login with role-based access for `Admin` and `Student`.
* **Notice Management:** Post, edit, view, and delete notices with structured models.
* **Auto-Expiry Mechanism:** Background utility (`ExpiryChecker`) to automatically filter out expired announcements.
* **Data Validation & Error Handling:** Custom exceptions (`UnauthorizedAccessException`, `InvalidNoticeException`) to ensure smooth execution and proper input validation.
* **Database & Backup Support:** SQL scripts for schema set up and sample data, alongside file-based storage utilities for backups and logs.

---

## 🛠️ Project Structure

```text
SmartCampusNoticeBoard/
├── data/                  # System backups and log files
│   ├── backup.txt
│   └── logs.txt
├── documentation/         # Extended project documentation
├── screenshots/           # Application execution screenshots
├── sql/                   # Database scripts
│   ├── schema.sql         # Database tables definition
│   └── sample_data.sql    # Sample initial records
└── src/                   # Source code
    ├── Main.java          # Application entry point
    ├── exception/         # Custom exception classes
    │   ├── InvalidNoticeException.java
    │   └── UnauthorizedAccessException.java
    ├── menu/              # Interactive console menus
    │   └── Menu.java
    ├── model/             # Object models (User, Student, Admin, Notice)
    │   ├── Admin.java
    │   ├── Notice.java
    │   ├── Student.java
    │   └── User.java
    ├── repository/        # Data access layers
    │   ├── NoticeRepository.java
    │   └── UserRepository.java
    ├── service/           # Core business logic (Auth, Notice management)
    │   ├── AuthService.java
    │   └── NoticeService.java
    └── util/              # Helper utilities (Validation, File IO, Expiry checks)
        ├── ExpiryChecker.java
        ├── FileManager.java
        └── Validation.java