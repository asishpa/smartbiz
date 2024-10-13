package com.smartbiz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartbiz.entity.Categories;
import com.smartbiz.entity.Store;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, String>{
	List<Categories> findByStore(Store store);
}
