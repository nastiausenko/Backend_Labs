package dev.usenkonastia.backend_lab2.service.impl;

import dev.usenkonastia.backend_lab2.domain.User;
import dev.usenkonastia.backend_lab2.repository.UserRepository;
import dev.usenkonastia.backend_lab2.security.jwt.JwtUtil;
import dev.usenkonastia.backend_lab2.service.UserService;
import dev.usenkonastia.backend_lab2.service.exception.UserNotFoundException;
import dev.usenkonastia.backend_lab2.service.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String registerUser(User user) {
        User newUser = User.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .build();
        userMapper.toUser(userRepository.save(userMapper.toUserEntity(newUser)));
        return jwtUtil.generateToken(newUser.getEmail());
    }

    @Transactional
    public String loginUser(User user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        return jwtUtil.generateToken(user.getEmail());
    }

    @Transactional(readOnly = true)
    public User getUserById(UUID id) {
        return userMapper.toUser(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userMapper.toUserList(userRepository.findAll().iterator());
    }

    @Transactional
    public void deleteAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        UUID id = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email)).getId();
        userRepository.deleteById(id);
    }
}
