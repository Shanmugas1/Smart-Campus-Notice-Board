package service;

import exception.UnauthorizedAccessException;
import model.Admin;
import model.Student;
import model.User;
import repository.UserRepository;

public class AuthService {
    private UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public User registerUser(String name, String username, String password, String role)
            throws UnauthorizedAccessException {
        if (name == null || name.trim().isEmpty() || username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Name, username, and password are required.");
        }

        if (userRepository.findByUsername(username) != null) {
            throw new IllegalArgumentException("Username already exists.");
        }

        if (role == null || role.trim().isEmpty()) {
            throw new UnauthorizedAccessException("Role is required. Use ADMIN or STUDENT.");
        }

        int id = userRepository.getNextId();
        User user;

        if (role.equalsIgnoreCase("ADMIN")) {
            user = new Admin(id, name.trim(), username.trim(), password);
        } else if (role.equalsIgnoreCase("STUDENT")) {
            user = new Student(id, name.trim(), username.trim(), password);
        } else {
            throw new UnauthorizedAccessException("Only ADMIN or STUDENT roles are allowed.");
        }

        userRepository.addUser(user);
        return user;
    }
}
