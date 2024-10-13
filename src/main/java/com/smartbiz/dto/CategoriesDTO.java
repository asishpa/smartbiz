package com.smartbiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriesDTO {
	private String categoryId;
	private String categoryName;
	private String description;
	private boolean isActive;
}
