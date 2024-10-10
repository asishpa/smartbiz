package com.smartbiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.smartbiz.exceptions.GlobalExceptionsHandler;

@SpringBootApplication
public class SmartbizAppApplication {

	public static void main(String[] args) {
		System.out.println("Environment Variable TEST_ENV_VAR: " + System.getenv("DB_NAME"));
		SpringApplication.run(SmartbizAppApplication.class, args);
	}

}
