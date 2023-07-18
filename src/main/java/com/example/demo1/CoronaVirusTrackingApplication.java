package com.example.demo1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"com.example.*"})
@EnableJpaRepositories("com.example.*")
@EntityScan("com.example.*")
public class CoronaVirusTrackingApplication {
	
    public static void main(String[] args) {
        SpringApplication.run( CoronaVirusTrackingApplication.class, args);
    }
}
