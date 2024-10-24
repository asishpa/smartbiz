package com.smartbiz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smartbiz.entity.ProductPhoto;

@Repository
public interface ProductsPhotoRepository extends JpaRepository<ProductPhoto, String>{
	List<ProductPhoto> findByPublicIdIn(List<String> publicIds);
	 @Modifying
	 @Query("DELETE FROM ProductPhoto p WHERE p.product.id = :productId")
	 void deleteAllByProductId(@Param("productId") String productId);
}
