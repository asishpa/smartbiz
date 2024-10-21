package com.smartbiz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartbiz.entity.ProductPhoto;

@Repository
public interface ProductsPhotoRepository extends JpaRepository<ProductPhoto, String>{
	List<ProductPhoto> findByPublicIdIn(List<String> publicIds);

}
