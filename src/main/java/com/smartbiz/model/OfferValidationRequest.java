package com.smartbiz.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Singular;

@Data
public class OfferValidationRequest {
	@NotBlank(message = "offer name is required")
	@Size(max = 17,message = "offer name cannot exceed 17 characters")
	private String offerName;
	@NotBlank(message = "offer code is required")
	@Size(max = 17,message = "offer code cannot exceed 17 characters")
	private String offerCode;
}
