package com.smartbiz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartbiz.entity.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, String>{
	public List<Orders> findByCustomerUserIdAndStoreId(String userId,String storeId);
	public List<Orders> findByStoreId(String storeId);
}
