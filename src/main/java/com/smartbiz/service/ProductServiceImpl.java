package com.smartbiz.service;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;
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
import com.smartbiz.model.Toggle;
import com.smartbiz.repository.CategoryRepository;
import com.smartbiz.repository.InventoryRepo;
import com.smartbiz.repository.ProductsPhotoRepository;
import com.smartbiz.repository.ProductsRepository;
import com.smartbiz.repository.StoreRepository;
import com.smartbiz.repository.WarehouseRepository;

import jakarta.transaction.Transactional;

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
	@Transactional
	public List<ProductsDTO> addProduct(String storeId, AddProduct addProduct) {
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
		product.setActive(true);
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
			 inventoryRepo.flush();
		});

		return productRepo.findByStore(store).stream().map(entityMapper::toProductsDTO).collect(Collectors.toList());
	}

	@Override
	public List<ProductsDTO> getProducts(String storeId) {
		Store store = storeRepo.findById(storeId)
				.orElseThrow(() -> new ResourceNotFoundException("Store does not exist with the specified storeId"));

		return productRepo.findByStore(store).stream().map(entityMapper::toProductsDTO).collect(Collectors.toList());

	}

	@Override
	@Transactional
	public boolean deleteProduct(String storeId, String productId) {
		Products product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with given Id"));
		Categories category = product.getCategory();
		Store store = product.getStore();
		try {
			category.getProducts().remove(product);
			store.getProducts().remove(product);
			if (product.getWarehouseInventories() != null) {
	            inventoryRepo.deleteAllByProductId(productId);
	            product.setWarehouseInventories(null);
	        }
			if (product.getPhotos() != null) {
	            productPhotoRepo.deleteAllByProductId(productId);
	            product.setPhotos(null);
	        }
			product.setCategory(null);
			product.setStore(null);
			productRepo.delete(product);
			productRepo.flush();
			return true;
		} 
		catch (Exception e) {
			throw new RuntimeException("Failed to delete product with ID: " + productId, e);
        }
		}


	@Override 
	public ProductsDTO partialUpdate(String storeId, String productId, Boolean status) {
		Products product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("productId does not exist"));

		product.setActive(status);

		productRepo.save(product);

		return entityMapper.toProductsDTO(product);
	}

	@Override
	public ProductsDTO updateProduct(String storeId, String productId, AddProduct newProduct) {
		Products existingProduct = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
		Categories category = categoryRepo.findById(newProduct.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id"));
		existingProduct.setProductName(newProduct.getProductName());
		existingProduct.setProductDesc(newProduct.getProductDesc());
		existingProduct.setActualPrice(newProduct.getActualPrice());
		existingProduct.setDiscountedPrice(newProduct.getDiscountedPrice());
		existingProduct.setCategory(category);
		existingProduct.setHsnCode(newProduct.getHsnCode());
		existingProduct.setWeight(newProduct.getWeight());
		Products savedProduct = productRepo.save(existingProduct);
		newProduct.getInventoryList().forEach((warehouseId,quantity) -> {
			Warehouse warehouse = warehouseRepo.findById(warehouseId)
						.orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));
		List<ProductWarehouseInventory> existingInventoryList = inventoryRepo.findByProduct(savedProduct);
		ProductWarehouseInventory existingInventory = existingInventoryList.stream()
				.filter(inventory -> inventory.getWarehouse().getWarehouseId().equals(warehouseId))
				.findFirst()
				.orElse(null);
		if (existingInventory != null) {
			existingInventory.setQuantity(quantity);
			inventoryRepo.save(existingInventory);
		}
		else {
			ProductWarehouseInventory inventory = new ProductWarehouseInventory();
			inventory.setProduct(savedProduct);
			inventory.setWarehouse(warehouse);
			inventory.setQuantity(quantity);
			inventoryRepo.save(inventory);
		}
		
		});
			return entityMapper.toProductsDTO(savedProduct);
		}

	@Override
	public List<ProductsDTO> getProductByStoreId(String storeId, String categoryId) {
		List<Products> products = productRepo.findProductsByCategoryId(categoryId);
		return null;
		//return entityMapper.toProductsDTO(products);
	}
	
}
