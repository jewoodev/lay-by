package com.layby;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LayByApplication {

    public static void main(String[] args) {
        SpringApplication.run(LayByApplication.class, args);
    }

}
