package com.smartbiz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartbiz.entity.ProductWarehouseInventory;
import com.smartbiz.entity.Products;
import com.smartbiz.entity.Store;

@Repository
public interface InventoryRepo extends JpaRepository<ProductWarehouseInventory, Long>{
	 
	 List<ProductWarehouseInventory> findByProduct(Products product);

}
