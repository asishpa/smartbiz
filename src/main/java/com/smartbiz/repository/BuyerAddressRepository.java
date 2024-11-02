package com.smartbiz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartbiz.entity.BuyerAddress;

@Repository
public interface BuyerAddressRepository extends JpaRepository<BuyerAddress, Long>{
	List<BuyerAddress> findByCustomerUserId(String userId);
}
