package com.smartbiz.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartbiz.dto.ProductWarehouseDTO;
import com.smartbiz.dto.ProductsDTO;
import com.smartbiz.entity.Categories;
import com.smartbiz.entity.ProductPhoto;
import com.smartbiz.entity.ProductWarehouseInventory;
import com.smartbiz.entity.Products;
import com.smartbiz.entity.Store;
import com.smartbiz.entity.Warehouse;
import com.smartbiz.exceptions.ResourceNotFoundException;
import com.smartbiz.mapper.EntityMapper;
import com.smartbiz.model.AddProduct;
import com.smartbiz.repository.CategoryRepository;
import com.smartbiz.repository.InventoryRepo;
import com.smartbiz.repository.ProductsPhotoRepository;
import com.smartbiz.repository.ProductsRepository;
import com.smartbiz.repository.StoreRepository;
import com.smartbiz.repository.WarehouseRepository;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private CategoryRepository categoryRepo;

	@Autowired
	private WarehouseRepository warehouseRepo;

	@Autowired
	private InventoryRepo inventoryRepo;
	
	@Autowired
	private StoreRepository storeRepo;

	@Autowired
	private ProductsRepository productRepo;
	
	@Autowired
	private ProductsPhotoRepository productPhotoRepo;
	@Autowired
	private EntityMapper entityMapper;
	@Override
	public List<ProductsDTO> addProduct(String storeId,AddProduct addProduct) {
		Store store = storeRepo.findById(storeId).orElseThrow(() -> new ResourceNotFoundException("Store not found"));
		Categories category = categoryRepo.findById(addProduct.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException("Category Not found with given ID"));
		Products product = new Products();
		product.setProductName(addProduct.getProductName());
		product.setProductDesc(addProduct.getProductDesc());
		product.setActualPrice(addProduct.getActualPrice());
		product.setDiscountedPrice(addProduct.getDiscountedPrice());
		product.setCategory(category);
		product.setHsnCode(addProduct.getHsnCode());
		product.setWeight(addProduct.getWeight());
		
		product.setStore(store);
		 List<ProductPhoto> existingPhotos = productPhotoRepo.findByPublicIdIn(addProduct.getPhotoPublicId());
		    existingPhotos.forEach(photo -> photo.setProduct(product)); // Set the product reference

		    product.setPhotos(existingPhotos);
		Products savedProduct = productRepo.save(product);
		addProduct.getInventoryList().forEach((warehouseId, quantity) -> {
			Warehouse warehouse = warehouseRepo.findById(warehouseId)
					.orElseThrow(() -> new ResourceNotFoundException("warehouse not found"));
			ProductWarehouseInventory inventory = new ProductWarehouseInventory();
			inventory.setProduct(savedProduct);
			inventory.setWarehouse(warehouse);
			inventory.setQuantity(quantity);
			inventoryRepo.save(inventory);

		});
		
	    // Manual conversion from Products to ProductsDTO
	    return productRepo.findByStore(store).stream().map(entityMapper::toProductsDTO).collect(Collectors.toList());
	}
	
}
