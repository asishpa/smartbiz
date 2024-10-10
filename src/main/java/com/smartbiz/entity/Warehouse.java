package com.smartbiz.entity;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Warehouse {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long warehouseId;
	private String wareHouseName;
	private String contactPerson;
	private Long mobileNo;
	private String flatHouseNo;
	private String areaColony;
	private Long pinCode;
	private String city;
	private String state;
	@CreationTimestamp
	private LocalDate createdAt;
	@UpdateTimestamp
	private LocalDate updatedAt;
	
	@ManyToOne
	@JoinColumn(name = "store_id",nullable = false)
	private Store store;
	
	@ManyToOne
	@JoinColumn(name = "inventory_id",nullable =  false)
	private Inventory inventory;

}
