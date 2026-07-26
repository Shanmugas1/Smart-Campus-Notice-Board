package repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Admin;
import model.Student;
import model.User;

public class UserRepository {
    private static final String DB_URL;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC driver not found.", e);
        }
        DB_URL = "jdbc:sqlite:" + resolveDatabasePath();
    }

    private static String resolveDatabasePath() {
        Path[] candidates = {
                Paths.get("data", "smartcampus.db"),
                Paths.get("SmartCampusNoticeBoard", "data", "smartcampus.db")
        };

        for (Path candidate : candidates) {
            Path parent = candidate.getParent();
            if (parent != null) {
                try {
                    Files.createDirectories(parent);
                } catch (IOException ignored) {
                    continue;
                }
            }

            if (Files.exists(candidate)) {
                return candidate.toAbsolutePath().normalize().toString();
            }
        }

        Path fallback = Paths.get("data", "smartcampus.db").toAbsolutePath().normalize();
        try {
            Files.createDirectories(fallback.getParent());
        } catch (IOException ignored) {
            // Ignore and continue with the resolved fallback path.
        }
        return fallback.toString();
    }

    private List<User> users;
    private int nextId;

    public UserRepository() {
        users = new ArrayList<>();
        nextId = 3;
        initializeDatabase();
        loadUsers();
    }

    private void initializeDatabase() {
        String createUsersSql = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name VARCHAR(100) NOT NULL,"
                + "username VARCHAR(50) UNIQUE NOT NULL,"
                + "password VARCHAR(100) NOT NULL,"
                + "role VARCHAR(20) NOT NULL"
                + ");";

        String createNoticesSql = "CREATE TABLE IF NOT EXISTS notices ("
                + "notice_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "title VARCHAR(150) NOT NULL,"
                + "description TEXT,"
                + "category VARCHAR(30) NOT NULL,"
                + "posted_by VARCHAR(100),"
                + "post_date DATE NOT NULL,"
                + "expiry_date DATE NOT NULL"
                + ");";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(createUsersSql);
            statement.executeUpdate(createNoticesSql);

            if (countUsers(connection) == 0) {
                seedUsers(connection);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to initialize database for user storage.", e);
        }
    }

    private int countUsers(Connection connection) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }

    // Preload a couple of demo accounts so login works out of the box
    private void seedUsers(Connection connection) throws SQLException {
        insertUser(connection, new Admin(1, "Campus Admin", "admin", "admin123"));
        insertUser(connection, new Student(2, "Test Student", "student", "student123"));
    }

    private void loadUsers() {
        users.clear();
        String sql = "SELECT id, name, username, password, role FROM users";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");

                if ("ADMIN".equalsIgnoreCase(role)) {
                    users.add(new Admin(id, name, username, password));
                } else {
                    users.add(new Student(id, name, username, password));
                }
            }

            nextId = users.stream()
                    .mapToInt(User::getId)
                    .max()
                    .orElse(0) + 1;
        } catch (SQLException e) {
            throw new RuntimeException("Unable to load users from database.", e);
        }
    }

    public User findByUsername(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    public int getNextId() {
        return nextId++;
    }

    public void addUser(User user) {
        if (findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists.");
        }

        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            insertUser(connection, user);
            users.add(user);
            nextId = Math.max(nextId, user.getId() + 1);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to persist user.", e);
        }
    }

    private void insertUser(Connection connection, User user) throws SQLException {
        String sql = "INSERT INTO users (name, username, password, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole());
            statement.executeUpdate();
        }
    }
}
