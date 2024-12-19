package dev.usenkonastia.backend_lab2.service;

import dev.usenkonastia.backend_lab2.entity.UserEntity;
import dev.usenkonastia.backend_lab2.repository.UserRepository;
import dev.usenkonastia.backend_lab2.service.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserEntity addUser(UserEntity user) {
        return userRepository.save(user);
    }

    public UserEntity getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
