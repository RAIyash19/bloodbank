package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.demo.repository")
public class Bloodbank1Application {

	public static void main(String[] args) {
		SpringApplication.run(Bloodbank1Application.class, args);
	}

}
