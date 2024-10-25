package com.smartbiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;
import com.smartbiz.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, String>{
	Optional<Store> findByName(String name);
	Optional<Store> findByStoreLink(String storeLink);
}
