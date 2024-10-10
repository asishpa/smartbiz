package com.smartbiz.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	@GetMapping("/env")
    public String getEnvVars() {
        return "DB_HOST: " + System.getenv("DB_HOST") + 
               ", DB_PORT: " + System.getenv("DB_PORT") + 
               ", DB_USERNAME: " + System.getenv("DB_USERNAME");
    }
}
