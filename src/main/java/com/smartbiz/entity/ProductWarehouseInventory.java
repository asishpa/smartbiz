package com.smartbiz.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = {"product", "warehouse"})
public class ProductWarehouseInventory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "product_id",nullable = false)
	private Products product;
	@ManyToOne
	@JoinColumn(name = "warehouse_id",nullable = false)
	private Warehouse warehouse;
	
	private Integer quantity;
	@Version
	private Integer version;
}
