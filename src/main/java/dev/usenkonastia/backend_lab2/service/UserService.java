package dev.usenkonastia.backend_lab2.service;

import dev.usenkonastia.backend_lab2.domain.UserDetails;
import dev.usenkonastia.backend_lab2.repository.UserRepository;
import dev.usenkonastia.backend_lab2.service.exception.UserNotFoundException;
import dev.usenkonastia.backend_lab2.service.mapper.UserMapper;
import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserDetails addUser(UserDetails user) {
        try {
            return userMapper.toUser(userRepository.save(userMapper.toUserEntity(user)));
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Transactional(readOnly = true)
    public UserDetails getUserById(UUID id) {
        try {
            return userMapper.toUser(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<UserDetails> getUsers() {
        try {
            return userMapper.toUserList(userRepository.findAll().iterator());
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Transactional
    public void deleteUser(UUID id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }
}
