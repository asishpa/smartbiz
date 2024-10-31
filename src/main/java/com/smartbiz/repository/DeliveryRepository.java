package com.smartbiz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartbiz.entity.Delivery;
import com.smartbiz.entity.Store;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long>{
	Optional<Delivery> findByStore(Store store);
}
