package com.smartbiz.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import com.smartbiz.dto.ProductWarehouseDTO;
import com.smartbiz.dto.ProductsDTO;
import com.smartbiz.dto.WarehouseDTO;
import com.smartbiz.dto.WarehouseInventoryDTO;
import com.smartbiz.entity.ProductPhoto;
import com.smartbiz.entity.ProductWarehouseInventory;
import com.smartbiz.entity.Products;
import com.smartbiz.entity.Warehouse;

@Mapper(componentModel = "spring")
public interface EntityMapper {
	// mapping for warehouse
	WarehouseDTO toDto(Warehouse warehouse);

	Warehouse toEntity(WarehouseDTO dto);

	@Mapping(source = "category.categoryName", target = "categoryName")
	@Mapping(source = "store.name", target = "storeName")
	@Mapping(source = "photos", target = "photoPublicId", qualifiedByName = "mapPhotosToPublicIds")
	@Mapping(source = "warehouseInventories", target = "inventory") 
	ProductsDTO toProductsDTO(Products product);

	List<ProductsDTO> toProductsDTOs(List<ProductPhoto> products);

	@Mapping(source = "warehouse.warehouseName", target = "warehouseName")
	ProductWarehouseDTO toProductWarehouseDTO(ProductWarehouseInventory inventory);

	List<ProductWarehouseDTO> toProductWarehouseDTOs(List<ProductWarehouseInventory> inventories);

	@Named("mapPhotosToPublicIds")
	 static List<String> mapPhotosToPublicIds(List<ProductPhoto> photos) {
		return photos.stream().map(ProductPhoto::getPublicId).collect(Collectors.toList());
	}
}
