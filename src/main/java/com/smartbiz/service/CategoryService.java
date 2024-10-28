package com.smartbiz.service;

import java.util.List;

import com.smartbiz.dto.CategoriesDTO;
import com.smartbiz.entity.Categories;
import com.smartbiz.model.AddCategory;

public interface CategoryService {
	public List<CategoriesDTO> addCategory(String storeId,AddCategory addCategory);

	public List<CategoriesDTO> deleteCategory(String categoryId,String storeId);

	public List<CategoriesDTO> editCategoy(String storeId,String categoryId, AddCategory category);
	
	public List<CategoriesDTO> viewCategory(String storeId);
	
	public CategoriesDTO partialUpdate(String storeId,String categoryId,Boolean status );

	public CategoriesDTO getCategoryById(String storeId,String categoryId);
}
