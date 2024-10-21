package com.smartbiz.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartbiz.dto.CategoriesDTO;
import com.smartbiz.entity.Categories;
import com.smartbiz.entity.Store;
import com.smartbiz.exceptions.ResourceNotFoundException;
import com.smartbiz.exceptions.UnauthorizedAccessException;
import com.smartbiz.model.AddCategory;
import com.smartbiz.repository.CategoryRepository;
import com.smartbiz.repository.StoreRepository;
import com.smartbiz.utils.SecurityUtil;

@Service
public class CategoryServiceImpl implements CategoryService {

	
	@Autowired
	private CategoryRepository categoryRepo;

	@Autowired
	private StoreRepository storeRepo;
	
	@Autowired
	private SecurityUtil securityUtil;

	@Override
	public List<CategoriesDTO> addCategory(String storeId, AddCategory addCategory) {
		Store store = storeRepo.findById(storeId).orElseThrow(() -> new ResourceNotFoundException("Store not found"));
		Categories category = new Categories();
		System.out.println("get category name"+addCategory.getCategoryName());
		category.setCategoryName(addCategory.getCategoryName());
		category.setDescription(addCategory.getDescription());
		category.setActive(true);
		category.setStore(store);
		categoryRepo.save(category);
		return categoryRepo.findByStore(store)
				.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<CategoriesDTO> deleteCategory(String categoryId,String storeId) {
		Store store = storeRepo.findById(storeId).orElseThrow(() -> new ResourceNotFoundException("Store not found"));

		Categories category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category Not found with given ID"));
		categoryRepo.delete(category);
		return categoryRepo.findByStore(store)
				.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
		}

	@Override
	public List<CategoriesDTO> editCategoy(String categoryId, AddCategory updatedCategory,String storeId) {
		Store store = storeRepo.findById(storeId).orElseThrow(() -> new ResourceNotFoundException("Store not found"));
		Categories existingCategory = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with given Id"));
		existingCategory.setCategoryName(updatedCategory.getCategoryName());
		existingCategory.setDescription(updatedCategory.getDescription());
		existingCategory.setActive(updatedCategory.isActive());
		categoryRepo.save(existingCategory);
		return categoryRepo.findByStore(store)
				.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
		}
	@Override
	public List<CategoriesDTO> viewCategory(String storeId) {
		Store store = storeRepo.findById(storeId).orElseThrow(() -> new ResourceNotFoundException("Store not found"));

		return categoryRepo.findByStore(store)
				.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
	}

	private CategoriesDTO convertToDto(Categories category) {
		CategoriesDTO dto = new CategoriesDTO();
		dto.setCategoryId(category.getCategoryId());
		dto.setCategoryName(category.getCategoryName());
		dto.setDescription(category.getDescription());
		dto.setActive(category.isActive());
		return dto;
	}

	
}
