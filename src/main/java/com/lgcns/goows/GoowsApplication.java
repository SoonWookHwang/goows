package com.lgcns.goows;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GoowsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoowsApplication.class, args);
    }

}
