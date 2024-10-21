package com.smartbiz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartbiz.entity.Products;
import com.smartbiz.entity.Store;

@Repository
public interface ProductsRepository extends JpaRepository<Products, String>{
	List<Products> findByStore(Store store);
}
