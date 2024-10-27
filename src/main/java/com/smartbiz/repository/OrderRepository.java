package com.smartbiz.repository;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartbiz.entity.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, String>{
	

}
