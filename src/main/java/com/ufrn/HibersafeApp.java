package com.ufrn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@SpringBootApplication(scanBasePackages="com.ufrn.api")
@EntityScan("com.ufrn.api")
@EnableJpaRepositories("com.ufrn.api")
public class HibersafeApp {
	
    public static void main(String[] args) {
        SpringApplication.run(HibersafeApp.class, args);
    }
}
