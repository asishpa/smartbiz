package com.smartbiz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;
import com.smartbiz.dto.ProductsDTO;
import com.smartbiz.entity.Products;
import com.smartbiz.entity.Store;

@Repository
public interface ProductsRepository extends JpaRepository<Products, String>{
	List<Products> findByStore(Store store);
	Optional<Products> findByIdAndStoreId(String productId, String storeId);
	int deleteByIdAndStoreId(String productId, String storeId);
	@Query("SELECT p FROM Products p WHERE p.category.categoryId = :categoryId")
    List<Products> findProductsByCategoryId(@Param("categoryId") String categoryId);
}
