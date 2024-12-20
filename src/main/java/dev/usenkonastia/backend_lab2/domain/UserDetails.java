package dev.usenkonastia.backend_lab2.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class UserDetails {
    UUID id;
    String name;
    String email;
    String password;
    List<Category> categories;
}
