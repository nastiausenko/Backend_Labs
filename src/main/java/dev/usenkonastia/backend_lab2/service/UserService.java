package dev.usenkonastia.backend_lab2.service;

import dev.usenkonastia.backend_lab2.entity.User;
import dev.usenkonastia.backend_lab2.service.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserService {
    private Map<UUID, User> users = new HashMap<>();

    public User addUser(User user) {
        User newUser = User.builder()
                .id(UUID.randomUUID())
                .name(user.getName())
                .build();
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    public User getUserById(UUID id) {
        User user = users.get(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return user;
    }

    public List<User> getUsers() {
        return List.copyOf(users.values());
    }

    public void deleteUser(UUID id) {
        User user = getUserById(id);
        users.remove(user.getId());
    }
}
