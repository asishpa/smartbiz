package com.smartbiz.mapper;

import org.mapstruct.Mapper;

import com.smartbiz.dto.WarehouseDTO;
import com.smartbiz.entity.Warehouse;

@Mapper(componentModel = "spring")
public interface EntityMapper {
	// mapping for warehouse
	WarehouseDTO toDto(Warehouse warehouse);

	Warehouse toEntity(WarehouseDTO dto);
}
