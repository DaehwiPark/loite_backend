package com.boot.loiteMsBack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.boot.loiteMsBack")
public class LoiteMsBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoiteMsBackApplication.class, args);
	}

}
