package com.smartbiz.service;

import java.util.List;

import com.smartbiz.dto.WarehouseDTO;
import com.smartbiz.model.AddWarehouse;

public interface InventoryService {
	public List<WarehouseDTO> addWareHouse(AddWarehouse warehouse,String storeId);
	public List<WarehouseDTO> listWareHouse(String storeId);
	public List<WarehouseDTO> deletWareHouse(String storeId,String warehouseId);
	public List<WarehouseDTO> editWareHouse(String storeId,String warehouseId,AddWarehouse warehouse);
	
}
