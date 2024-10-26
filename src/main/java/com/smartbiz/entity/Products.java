package com.smartbiz.entity;

import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = {"warehouseInventories", "photos", "store", "category"})
public class Products {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private String productName;
	@ManyToOne
	@JoinColumn(name = "category_id",nullable = false)
	private Categories category;
	private String productDesc;
	private Float actualPrice;
	private Float discountedPrice;
	private Float weight;
	private String hsnCode;
	private boolean active;
	
	
	@OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
	private List<ProductPhoto> photos;
	
	@OneToMany(mappedBy = "product")
	private Set<ProductWarehouseInventory> warehouseInventories;
	
	@ManyToOne
	@JoinColumn(name = "store_id",nullable = false)
	private Store store;
}
