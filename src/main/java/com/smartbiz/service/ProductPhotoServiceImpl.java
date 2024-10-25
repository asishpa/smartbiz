package com.smartbiz.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.smartbiz.entity.ProductPhoto;
import com.smartbiz.repository.ProductsPhotoRepository;

@Service
public class ProductPhotoServiceImpl implements ProductPhotoService{

	@Autowired
	private Cloudinary cloudinary;
	
	@Autowired
	private ProductsPhotoRepository photoRepo;
	@Override
	public Map<String, String> uploadFile(MultipartFile file) {
		try {
			HashMap<Object, Object> options = new HashMap<>();
			options.put("folder", "product");
			Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
			String fullPublicId = (String)uploadedFile.get("public_id");
			// Strip off the folder name if present (get part after the last "/")
            String publicId = fullPublicId.contains("/") 
                    ? fullPublicId.substring(fullPublicId.lastIndexOf("/") + 1) 
                    : fullPublicId;
            
            String imageUrl = cloudinary.url().secure(true).generate("product/" + publicId);
			System.out.println(imageUrl);
			//save to db
			ProductPhoto photo = new ProductPhoto();
			//photo.setUrl(imageUrl);
			photo.setPublicId(publicId);
			photoRepo.save(photo);
			
			 return Map.of("publicId", publicId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
