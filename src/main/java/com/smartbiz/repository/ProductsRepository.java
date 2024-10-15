package com.smartbiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartbiz.entity.Products;

@Repository
public interface ProductsRepository extends JpaRepository<Products, String>{

}
