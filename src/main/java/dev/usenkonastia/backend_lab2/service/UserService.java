package dev.usenkonastia.backend_lab2.service;

import dev.usenkonastia.backend_lab2.domain.User;
import dev.usenkonastia.backend_lab2.repository.UserRepository;
import dev.usenkonastia.backend_lab2.security.jwt.JwtUtil;
import dev.usenkonastia.backend_lab2.service.exception.UserNotFoundException;
import dev.usenkonastia.backend_lab2.service.mapper.UserMapper;
import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String registerUser(User user) {
        try {
            User newUser = User.builder()
                    .name(user.getName())
                    .email(user.getEmail())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .build();
            userMapper.toUser(userRepository.save(userMapper.toUserEntity(newUser)));
            return jwtUtil.generateToken(newUser.getEmail());
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Transactional
    public String loginUser(User user) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            return jwtUtil.generateToken(user.getEmail());
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Wrong email or password");
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Transactional(readOnly = true)
    public User getUserById(UUID id) {
        try {
            return userMapper.toUser(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<User> getUsers() {
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
