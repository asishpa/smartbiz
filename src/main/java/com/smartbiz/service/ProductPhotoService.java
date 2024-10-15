package com.smartbiz.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface ProductPhotoService {
	public Map<String, String> uploadFile(MultipartFile file);
		
}
