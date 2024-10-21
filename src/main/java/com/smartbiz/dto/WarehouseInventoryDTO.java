package com.smartbiz.dto;

import lombok.Data;

@Data
public class WarehouseInventoryDTO {
	private Long id;
    private String warehouseId;
    private String warehouseName;
    private Integer quantity;
}
