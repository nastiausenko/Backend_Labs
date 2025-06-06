package dev.usenkonastia.backend_lab2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BackendLab2Application {

    public static void main(String[] args) {
        SpringApplication.run(BackendLab2Application.class, args);
    }
}
