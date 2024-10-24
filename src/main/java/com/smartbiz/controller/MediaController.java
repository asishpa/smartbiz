package com.smartbiz.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.smartbiz.service.ProductPhotoService;

@RestController
@RequestMapping("/api/media")
public class MediaController {
	
	@Autowired
	private ProductPhotoService photoService; 
	@PostMapping("/upload")
	public ResponseEntity<Map<String, String>> upload(@RequestParam MultipartFile file){
		try {
			Map<String, String> response = photoService.uploadFile(file);
			if (response == null) {
				return ResponseEntity.badRequest().build();
				
			}
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
}
