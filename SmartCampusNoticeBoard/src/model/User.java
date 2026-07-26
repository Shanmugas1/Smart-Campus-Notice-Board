package model;

public abstract class User {
    private int id;
    private String name;
    private String username;
    private String password;
    private String role;

    public User(int id, String name, String username, String password, String role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public void setName(String name) { this.name = name; }
    public void setPassword(String password) { this.password = password; }

    // Abstraction: each subclass defines its own menu
    public abstract void showMenu();

    @Override
    public String toString() {
        return "User{id=" + id + ", name=" + name + ", username=" + username + ", role=" + role + "}";
    }
}
