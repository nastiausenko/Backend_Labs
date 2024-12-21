package dev.usenkonastia.backend_lab2.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class User  {
    UUID id;
    String name;
    String email;
    String password;
    List<Category> categories;
}
