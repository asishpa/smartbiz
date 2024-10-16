package com.smartbiz.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartbiz.dto.WarehouseDTO;
import com.smartbiz.entity.Store;
import com.smartbiz.entity.Warehouse;
import com.smartbiz.exceptions.ResourceNotFoundException;
import com.smartbiz.exceptions.UnauthorizedAccessException;
import com.smartbiz.mapper.EntityMapper;
import com.smartbiz.model.AddWarehouse;
import com.smartbiz.repository.StoreRepository;
import com.smartbiz.repository.WarehouseRepository;
import com.smartbiz.utils.SecurityUtil;

@Service
public class InventoryServiceImpl implements InventoryService {

	@Autowired
	private WarehouseRepository warehouseRepo;

	@Autowired
	private StoreRepository storeRepo;

	@Autowired
	private EntityMapper entityMapper;

	@Autowired
	private SecurityUtil securityUtil;

	@Override
	public List<WarehouseDTO> addWareHouse(AddWarehouse addWarehouse, String storeId) {
		String authenticatedUserId = securityUtil.getAuthenticatedUserId();
		Store store = storeRepo.findById(storeId).orElseThrow(() -> new ResourceNotFoundException("Store not found"));
		if (!store.getOwner().getUserId().equals(authenticatedUserId)) {
			throw new UnauthorizedAccessException("You are not allowed to add a warehouse to this store");
		}

		Warehouse warehouse = new Warehouse();
		warehouse.setWarehouseName(addWarehouse.getWarehouseName());
		warehouse.setContactPerson(addWarehouse.getContactPerson());
		warehouse.setMobileNo(addWarehouse.getMobileNo());
		warehouse.setFlatHouseNo(addWarehouse.getFlatHouseNo());
		warehouse.setAreaColony(addWarehouse.getAreaColony());
		warehouse.setPinCode(addWarehouse.getPinCode());
		warehouse.setCity(addWarehouse.getCity());
		warehouse.setState(addWarehouse.getState());
		warehouse.setStore(store);
		warehouseRepo.save(warehouse);
		return warehouseRepo.findByStore(store).stream().map(entityMapper::toDto).collect(Collectors.toList());
	}

	@Override
	public List<WarehouseDTO> listWareHouse(String storeId) {
		String authenticatedUserId = securityUtil.getAuthenticatedUserId();
		Store store = storeRepo.findById(storeId).orElseThrow(() -> new ResourceNotFoundException("Store not found"));
		if (!store.getOwner().getUserId().equals(authenticatedUserId)) {
			throw new UnauthorizedAccessException("You are not allowed to view warehouses of this store");
		}
		return warehouseRepo.findByStore(store).stream().map(entityMapper::toDto).collect(Collectors.toList());
	}

	@Override
	public List<WarehouseDTO> deletWareHouse(String storeId, String warehouseId) {
		String authenticatedUserId = securityUtil.getAuthenticatedUserId();
		Store store = storeRepo.findById(storeId).orElseThrow(() -> new ResourceNotFoundException("Store not found"));
		if (!store.getOwner().getUserId().equals(authenticatedUserId)) {
			throw new UnauthorizedAccessException("You are not allowed to delete warehouses of this store");
		}
		Warehouse wareHouse = warehouseRepo.findById(warehouseId).orElseThrow(() -> new ResourceNotFoundException("Warehouse does not exist"));
		warehouseRepo.delete(wareHouse);
		return warehouseRepo.findByStore(store).stream().map(entityMapper::toDto).collect(Collectors.toList());
		
	}

	@Override
	public List<WarehouseDTO> editWareHouse(String storeId, String warehouseId,AddWarehouse updatedWarehouse) {
		String authenticatedUserId = securityUtil.getAuthenticatedUserId();
		Store store = storeRepo.findById(storeId).orElseThrow(() -> new ResourceNotFoundException("Store not found"));
		if (!store.getOwner().getUserId().equals(authenticatedUserId)) {
			throw new UnauthorizedAccessException("You are not allowed to delete warehouses of this store");
		}
		Warehouse existingWarehouse = warehouseRepo.findById(warehouseId).orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with supplied id"));
		existingWarehouse.setWarehouseName(updatedWarehouse.getWarehouseName());
		existingWarehouse.setContactPerson(updatedWarehouse.getContactPerson());
		existingWarehouse.setMobileNo(updatedWarehouse.getMobileNo());
		existingWarehouse.setFlatHouseNo(updatedWarehouse.getFlatHouseNo());
		existingWarehouse.setAreaColony(updatedWarehouse.getAreaColony());
		existingWarehouse.setPinCode(updatedWarehouse.getPinCode());
		existingWarehouse.setCity(updatedWarehouse.getCity());
		existingWarehouse.setState(updatedWarehouse.getState());
		warehouseRepo.save(existingWarehouse);
		return warehouseRepo.findByStore(store).stream().map(entityMapper::toDto).collect(Collectors.toList());
	}
	

}
