package com.smartbiz.dto;

import java.util.Date;
import java.util.Set;

import lombok.Data;

@Data
public class UserDTO {
	private Long userId;
	private String userName;
	private String email;
	private boolean emailVerified;
	private Date createdAt;
	private Date updatedAt;
	private Set<String> roleNames;
	private String storeName;

}
