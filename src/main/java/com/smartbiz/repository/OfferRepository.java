package com.smartbiz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartbiz.entity.Offer;
import com.smartbiz.entity.Store;

@Repository
public interface OfferRepository extends JpaRepository<Offer, String>{
	boolean existsByofferCodeAndStoreId(String offerCode,String storeId);
	boolean existsByofferNameAndStoreId(String offerName,String storeId);
	List<Offer> findByStore(Store store);
}
