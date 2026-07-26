package model;

public class Student extends User {

    public Student(int id, String name, String username, String password) {
        super(id, name, username, password, "STUDENT");
    }

    @Override
    public void showMenu() {
        System.out.println("\n===== STUDENT MENU =====");
        System.out.println("1. View All Notices");
        System.out.println("2. Search Notice by Category");
        System.out.println("3. Search Notice by Keyword");
        System.out.println("4. Logout");
    }
}
