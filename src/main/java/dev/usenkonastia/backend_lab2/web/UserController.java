package dev.usenkonastia.backend_lab2.web;

import dev.usenkonastia.backend_lab2.dto.user.UserListDto;
import dev.usenkonastia.backend_lab2.dto.user.request.LoginRequestDto;
import dev.usenkonastia.backend_lab2.dto.user.request.RegisterRequestDto;
import dev.usenkonastia.backend_lab2.dto.user.response.AuthResponseDto;
import dev.usenkonastia.backend_lab2.entity.UserEntity;
import dev.usenkonastia.backend_lab2.service.UserService;
import dev.usenkonastia.backend_lab2.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<RegisterRequestDto> addUser(@RequestBody RegisterRequestDto user) {
        return ResponseEntity.ok(userMapper.toRegisterUserDto(userService.addUser(userMapper.toUser(user))));
    }

    @GetMapping
    public ResponseEntity<UserListDto> getAllUsers() {
        return ResponseEntity.ok(userMapper.toUserListDto(userService.getUsers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegisterRequestDto> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userMapper.toRegisterUserDto(userService.getUserById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
