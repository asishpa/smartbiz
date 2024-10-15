package com.smartbiz.entity;

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
public class ProductPhoto {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String publicId;
	private String url;
	@ManyToOne
	@JoinColumn(name = "product_id",nullable = true)
	private Products product;

}
