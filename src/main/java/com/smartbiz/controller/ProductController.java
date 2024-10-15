package com.smartbiz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartbiz.dto.CategoriesDTO;
import com.smartbiz.entity.Categories;
import com.smartbiz.model.AddCategory;
import com.smartbiz.service.CategoryService;


@RestController
@CrossOrigin
@RequestMapping("/api/stores/")
public class ProductController {
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/{storeId}/categories")
	public ResponseEntity<List<CategoriesDTO>> viewCategory(@PathVariable String storeId){
		List<CategoriesDTO> categories = categoryService.viewCategory(storeId);
		return new ResponseEntity<>(categories,HttpStatus.OK);
	}
	
	@PostMapping("/{storeId}/categories")
	public ResponseEntity<List<CategoriesDTO>> addCategory(@RequestBody AddCategory addCategory,@PathVariable String storeId) {
		List<CategoriesDTO> categories = categoryService.addCategory(storeId, addCategory);
		return new ResponseEntity<>(categories, HttpStatus.CREATED);
	}

	@DeleteMapping("/{storeId}/categories/{categoryId}")
	public ResponseEntity<List<CategoriesDTO>> deleteCategory(@PathVariable String storeId, @PathVariable String categoryId) {
		List<CategoriesDTO> categories = categoryService.deleteCategory(categoryId, storeId);
		return new ResponseEntity<List<CategoriesDTO>>(categories,HttpStatus.ACCEPTED);

	}

	@PatchMapping("/{storeId}/categories/{categoryId}")
	public ResponseEntity<List<CategoriesDTO>> editCategory(@PathVariable String categoryId,
			@RequestBody AddCategory updatedCategory, @PathVariable String storeId) {
		List<CategoriesDTO> categories = categoryService.editCategoy(categoryId, updatedCategory, storeId);
		return new ResponseEntity<>(categories, HttpStatus.OK);
	}
}
