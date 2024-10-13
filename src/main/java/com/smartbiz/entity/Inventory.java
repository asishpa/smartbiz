package com.smartbiz.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Inventory {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String inventoryId;
	private Integer qunatity;
	@OneToOne
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;
	@OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL)
	private List<Warehouse> warehouses;
}
