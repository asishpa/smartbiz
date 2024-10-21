package com.smartbiz.service;

import java.util.List;

import com.smartbiz.dto.ProductsDTO;
import com.smartbiz.model.AddProduct;

public interface ProductService {
	public List<ProductsDTO> addProduct(String storeId,AddProduct addProduct);
}
