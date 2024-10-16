package com.smartbiz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;

import com.smartbiz.dto.WarehouseDTO;
import com.smartbiz.model.AddWarehouse;
import com.smartbiz.service.InventoryService;

import io.swagger.v3.oas.annotations.StringToClassMapItem;

@RestController
@RequestMapping("/api/stores")
public class InventoryController {

	@Autowired
	public InventoryService inventoryService;

	@PostMapping("/{storeId}/warehouses")
	public ResponseEntity<List<WarehouseDTO>> addWarehouse(@RequestBody AddWarehouse addWarehouse,
			@PathVariable String storeId) {
		System.out.println(addWarehouse);
		List<WarehouseDTO> wareHouseList = inventoryService.addWareHouse(addWarehouse, storeId);
		return new ResponseEntity<>(wareHouseList, HttpStatus.CREATED);
	}

	@GetMapping("/{storeId}/warehouses")
	public ResponseEntity<List<WarehouseDTO>> viewWarehouse(@PathVariable String storeId) {
		List<WarehouseDTO> wareHouses = inventoryService.listWareHouse(storeId);
		return new ResponseEntity<>(wareHouses, HttpStatus.OK);
	}

	@DeleteMapping("/{storeId}/warehouses/{warehouseId}")
	public ResponseEntity<List<WarehouseDTO>> deleteWarehouse(@PathVariable String storeId,
			@PathVariable String warehouseId) {
		List<WarehouseDTO> wareHouses = inventoryService.deletWareHouse(storeId, warehouseId);
		return new ResponseEntity<>(wareHouses, HttpStatus.OK);

	}
	@PatchMapping("/{storeId}/warehouses/{warehouseId}")
	public ResponseEntity<List<WarehouseDTO>> editWareHouse(@PathVariable String storeId,@RequestBody AddWarehouse updatedWarehouse,@PathVariable String warehouseId){
		List<WarehouseDTO> wareHouses = inventoryService.editWareHouse(storeId, warehouseId, updatedWarehouse);
		return new ResponseEntity<>(wareHouses,HttpStatus.OK);
	}
}
