package com.smartbiz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smartbiz.entity.ProductWarehouseInventory;
import com.smartbiz.entity.Products;
import com.smartbiz.entity.Store;

@Repository
public interface InventoryRepo extends JpaRepository<ProductWarehouseInventory, Long>{
	 
	 List<ProductWarehouseInventory> findByProduct(Products product);
	 List<ProductWarehouseInventory> findByProduct_IdAndWarehouse_Store_Id(String productId,String storeId);
	 @Modifying
	 @Query("DELETE FROM ProductWarehouseInventory p WHERE p.product.id = :productId")
	 void deleteAllByProductId(@Param("productId") String productId);
}
