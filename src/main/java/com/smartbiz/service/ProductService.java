package com.smartbiz.service;

import java.util.List;

import com.smartbiz.dto.ProductsDTO;
import com.smartbiz.model.AddProduct;
import com.smartbiz.model.Toggle;

public interface ProductService {
	public List<ProductsDTO> addProduct(String storeId,AddProduct addProduct);
	public List<ProductsDTO> getProducts(String storeId,int page,int size);
	public boolean deleteProduct(String storeId,String productId);
	public ProductsDTO partialUpdate(String storeId,String productId ,Boolean status);
	public ProductsDTO updateProduct(String storeId,String productId,AddProduct addProduct);
	public List<ProductsDTO> getProductByCategoryId(String storeId,String categoryId);
	public ProductsDTO getProductByProductId(String storeId,String productId);
}
