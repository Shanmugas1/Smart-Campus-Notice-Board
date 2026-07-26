package menu;

import exception.InvalidNoticeException;
import exception.UnauthorizedAccessException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import model.Admin;
import model.Notice;
import model.Student;
import model.User;
import service.AuthService;
import service.NoticeService;

public class Menu {
    private Scanner sc;
    private AuthService authService;
    private NoticeService noticeService;

    public Menu(AuthService authService, NoticeService noticeService) {
        this.sc = new Scanner(System.in);
        this.authService = authService;
        this.noticeService = noticeService;
    }

    public void start() {
        System.out.println("=======================================");
        System.out.println("   SMART CAMPUS NOTICE BOARD");
        System.out.println("=======================================");

        boolean exitApp = false;
        while (!exitApp) {
            System.out.println("\n1. Login");
            System.out.println("2. Register User");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String choice = readLine();

            switch (choice) {
                case "1":
                    handleLogin();
                    break;
                case "2":
                    handleRegister();
                    break;
                case "3":
                    exitApp = true;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
        sc.close();
    }

    private void handleLogin() {
        System.out.print("Username: ");
        String username = readLine();
        System.out.print("Password: ");
        String password = readLine();

        User user = authService.login(username, password);
        if (user == null) {
            System.out.println("Invalid username or password.");
            return;
        }

        System.out.println("Login successful. Welcome, " + user.getName() + "!");

        if (user instanceof Admin) {
            runAdminSession((Admin) user);
        } else if (user instanceof Student) {
            runStudentSession((Student) user);
        }
    }

    private void handleRegister() {
        try {
            System.out.print("Name: ");
            String name = readLine();
            System.out.print("Username: ");
            String username = readLine();
            System.out.print("Password: ");
            String password = readLine();
            System.out.print("Role (ADMIN/STUDENT): ");
            String role = readLine();

            User user = authService.registerUser(name, username, password, role);
            System.out.println("User registered successfully: " + user.getUsername() + " [" + user.getRole() + "]");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (UnauthorizedAccessException e) {
            System.out.println("Access error: " + e.getMessage());
        }
    }

    private void runAdminSession(Admin admin) {
        boolean logout = false;
        while (!logout) {
            admin.showMenu();
            System.out.print("Choose an option: ");
            String choice = readLine();

            switch (choice) {
                case "1": postNoticeFlow(admin.getName()); break;
                case "2": updateNoticeFlow(); break;
                case "3": deleteNoticeFlow(); break;
                case "4": viewAllNotices(); break;
                case "5": searchMenu(); break;
                case "6": logout = true; break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private void runStudentSession(Student student) {
        boolean logout = false;
        while (!logout) {
            student.showMenu();
            System.out.print("Choose an option: ");
            String choice = readLine();

            switch (choice) {
                case "1": viewAllNotices(); break;
                case "2": searchByCategoryFlow(); break;
                case "3": searchByKeywordFlow(); break;
                case "4": logout = true; break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private void postNoticeFlow(String adminName) {
        try {
            System.out.print("Title: ");
            String title = readLine();
            System.out.print("Description: ");
            String description = readLine();
            System.out.print("Category (EXAM/EVENT/PLACEMENT/HOLIDAY/GENERAL): ");
            String category = readLine();
            System.out.print("Expiry date (yyyy-MM-dd): ");
            LocalDate expiryDate = LocalDate.parse(readLine());

            Notice notice = noticeService.addNotice(title, description, category,
                    adminName, LocalDate.now(), expiryDate);
            System.out.println("Notice posted successfully with ID " + notice.getNoticeId());

        } catch (InvalidNoticeException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("Error: Invalid date format. Use yyyy-MM-dd.");
        }
    }

    private void updateNoticeFlow() {
        try {
            System.out.print("Enter Notice ID to update: ");
            int id = Integer.parseInt(readLine());
            System.out.print("New title (leave blank to keep unchanged): ");
            String title = readLine();
            System.out.print("New description (leave blank to keep unchanged): ");
            String description = readLine();
            System.out.print("New category (leave blank to keep unchanged): ");
            String category = readLine();

            noticeService.updateNotice(id, title, description, category);
            System.out.println("Notice updated successfully.");

        } catch (NumberFormatException e) {
            System.out.println("Error: Notice ID must be a number.");
        } catch (InvalidNoticeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteNoticeFlow() {
        try {
            System.out.print("Enter Notice ID to delete: ");
            int id = Integer.parseInt(readLine());
            noticeService.deleteNotice(id);
            System.out.println("Notice deleted successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Error: Notice ID must be a number.");
        } catch (InvalidNoticeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAllNotices() {
        List<Notice> notices = noticeService.getAllNotices();
        if (notices.isEmpty()) {
            System.out.println("No notices available.");
            return;
        }
        for (Notice n : notices) {
            System.out.println(n);
        }
    }

    private void searchMenu() {
        System.out.println("Search by: 1. Category  2. Keyword  3. Date");
        String choice = readLine();
        switch (choice) {
            case "1": searchByCategoryFlow(); break;
            case "2": searchByKeywordFlow(); break;
            case "3": searchByDateFlow(); break;
            default: System.out.println("Invalid option.");
        }
    }

    private void searchByCategoryFlow() {
        System.out.print("Enter category: ");
        String category = readLine();
        printResults(noticeService.searchNotice(category));
    }

    private void searchByKeywordFlow() {
        System.out.print("Enter keyword: ");
        String keyword = readLine();
        printResults(noticeService.searchNotice(keyword, true));
    }

    private void searchByDateFlow() {
        try {
            System.out.print("Enter date (yyyy-MM-dd): ");
            LocalDate date = LocalDate.parse(readLine());
            printResults(noticeService.searchNotice(date));
        } catch (DateTimeParseException e) {
            System.out.println("Error: Invalid date format.");
        }
    }

    private void printResults(List<Notice> results) {
        if (results.isEmpty()) {
            System.out.println("No matching notices found.");
        } else {
            for (Notice n : results) {
                System.out.println(n);
            }
        }
    }

    // Secure input: never crashes on empty/interrupted input
    private String readLine() {
        if (sc.hasNextLine()) {
            return sc.nextLine().trim();
        }
        return "";
    }
}
