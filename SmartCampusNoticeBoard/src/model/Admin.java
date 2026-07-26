package model;

public class Admin extends User {

    public Admin(int id, String name, String username, String password) {
        super(id, name, username, password, "ADMIN");
    }

    @Override
    public void showMenu() {
        System.out.println("\n===== ADMIN MENU =====");
        System.out.println("1. Post Notice");
        System.out.println("2. Update Notice");
        System.out.println("3. Delete Notice");
        System.out.println("4. View All Notices");
        System.out.println("5. Search Notice");
        System.out.println("6. Logout");
    }
}
