package dev.usenkonastia.backend_lab2.service;

import dev.usenkonastia.backend_lab2.domain.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    String registerUser(User user);
    String loginUser(User user);
    User getUserById(UUID id);
    List<User> getUsers();
    void deleteAccount();
    void validateUserExists(UUID userId);
}
