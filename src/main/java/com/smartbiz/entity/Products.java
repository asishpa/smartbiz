package com.smartbiz.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Products {
	private Long id;
	private String productName;
	@ManyToOne
	@JoinColumn(name = "category_id",nullable = false)
	private Categories category;
	@OneToMany
	@JoinColumn(name = "")
	private Inventory inventory;
}
