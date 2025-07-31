package com.boot.loiteBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LoiteBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoiteBackendApplication.class, args);
	}

}
