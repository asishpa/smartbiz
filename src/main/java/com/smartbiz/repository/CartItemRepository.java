package com.smartbiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartbiz.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{

}
