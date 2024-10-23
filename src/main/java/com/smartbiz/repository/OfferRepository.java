package com.smartbiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartbiz.entity.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer, String>{
	boolean existsByofferCodeAndStoreId(String offerCode,String storeId);
	boolean existsByofferNameAndStoreId(String offerName,String storeId);
}
