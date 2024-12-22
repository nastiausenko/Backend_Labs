package dev.usenkonastia.backend_lab2.web;

import dev.usenkonastia.backend_lab2.dto.user.UserListDto;
import dev.usenkonastia.backend_lab2.dto.user.request.LoginRequestDto;
import dev.usenkonastia.backend_lab2.dto.user.request.RegisterRequestDto;
import dev.usenkonastia.backend_lab2.dto.user.response.AuthResponseDto;
import dev.usenkonastia.backend_lab2.service.UserService;
import dev.usenkonastia.backend_lab2.service.mapper.UserMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Validated
@Tag(name = "User")
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> registerUser(@Valid @RequestBody RegisterRequestDto user) {
        String token = userService.registerUser(userMapper.toUser(user));
        return ResponseEntity.ok(AuthResponseDto.builder().accessToken(token).build());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginUser(@Valid @RequestBody LoginRequestDto user) {
        String token = userService.loginUser(userMapper.toUser(user));
        return ResponseEntity.ok(AuthResponseDto.builder().accessToken(token).build());
    }

    @SecurityRequirement(name = "JWT")
    @GetMapping
    public ResponseEntity<UserListDto> getAllUsers() {
        return ResponseEntity.ok(userMapper.toUserListDto(userService.getUsers()));
    }

    @SecurityRequirement(name = "JWT")
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
