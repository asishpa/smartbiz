package com.smartbiz.entity;

import java.util.Date;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(exclude = "owner")
@ToString(exclude = "owner")
public class Store {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	
	private String name;
	private String storeLink;
	private Long mobileNo;
	private String country;
	private String storeAddress;
	private String storeEmail;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="owner_id",nullable = false)
	private User owner;
	
	@OneToMany(mappedBy = "store",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private Set<Orders> orders;
	
	@OneToMany(mappedBy = "store",cascade = CascadeType.ALL)
	private Set<Products> products;
	
	@CreationTimestamp
	private Date createdAt;
	
	@OneToMany(mappedBy = "store",cascade = CascadeType.ALL)
	private Set<Warehouse> warehouse;
	

	@OneToMany(mappedBy = "store",cascade = CascadeType.ALL)
	private Set<Categories> category;
	
	@OneToMany(mappedBy = "store",cascade = CascadeType.ALL)
	private Set<Offer> offers;
}
