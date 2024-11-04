package dev.usenkonastia.backend_lab2.entity;


import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Builder
@Value
public class Category {
    UUID id;
    String categoryName;
}
