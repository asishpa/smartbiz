package com.smartbiz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartbiz.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, String>{
	Optional<Store> findByName(String name);
	Optional<Store> findByStoreLink(String storeLink);
}
