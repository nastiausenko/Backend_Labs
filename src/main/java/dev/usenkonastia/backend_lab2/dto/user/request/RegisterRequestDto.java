package dev.usenkonastia.backend_lab2.dto.user.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class RegisterRequestDto {

    @Size(max = 100, message = "Name cannot exceed 100 characters")
    @NotBlank(message = "Name cannot be null")
    String username;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    String email;

    @NotBlank(message = "Email is mandatory")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.* ).{8,16}$",
    message = "Password must contain one digit from 1 to 9, " +
              "one lowercase letter, one uppercase letter, " +
              "one special character, no space, must be 8-16 characters long.")
    String password;
}
