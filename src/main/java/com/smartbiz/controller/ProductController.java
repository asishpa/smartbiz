package com.smartbiz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartbiz.annotation.PublicEndpoint;
import com.smartbiz.dto.CategoriesDTO;
import com.smartbiz.dto.ProductsDTO;
import com.smartbiz.model.AddCategory;
import com.smartbiz.model.AddProduct;
import com.smartbiz.model.Toggle;
import com.smartbiz.service.CategoryService;
import com.smartbiz.service.ProductService;

@RestController
@CrossOrigin
@RequestMapping("/api/stores/")
public class ProductController {
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@GetMapping("/{storeId}/categories")
	public ResponseEntity<List<CategoriesDTO>> viewCategory(@PathVariable String storeId) {
		List<CategoriesDTO> categories = categoryService.viewCategory(storeId);
		return new ResponseEntity<>(categories, HttpStatus.OK);
	}

	@GetMapping("/public/{storeId}/categories")
	@PublicEndpoint
	public ResponseEntity<List<CategoriesDTO>> viewBuyerCategories(@PathVariable String storeId) {
		List<CategoriesDTO> categories = categoryService.viewCategory(storeId);
		return new ResponseEntity<>(categories, HttpStatus.OK);
	}

	@PostMapping("/{storeId}/categories")
	public ResponseEntity<List<CategoriesDTO>> addCategory(@RequestBody AddCategory addCategory,
			@PathVariable String storeId) {
		List<CategoriesDTO> categories = categoryService.addCategory(storeId, addCategory);
		return new ResponseEntity<>(categories, HttpStatus.CREATED);
	}

	@GetMapping("/public/{storeId}/categories/{categoryId}")
	@PublicEndpoint
	public ResponseEntity<CategoriesDTO> getCategoryById(@PathVariable String storeId,
			@PathVariable String categoryId) {
		CategoriesDTO category = categoryService.getCategoryById(storeId, categoryId);
		return new ResponseEntity<>(category, HttpStatus.OK);
	}

	@DeleteMapping("/{storeId}/categories/{categoryId}")
	public ResponseEntity<List<CategoriesDTO>> deleteCategory(@PathVariable String storeId,
			@PathVariable String categoryId) {
		List<CategoriesDTO> categories = categoryService.deleteCategory(storeId, categoryId);
		return new ResponseEntity<List<CategoriesDTO>>(categories, HttpStatus.ACCEPTED);

	}

	@PatchMapping("/{storeId}/categories/{categoryId}")
	public ResponseEntity<List<CategoriesDTO>> editCategory(@PathVariable String storeId,
			@PathVariable String categoryId, @RequestBody AddCategory updatedCategory) {
		List<CategoriesDTO> categories = categoryService.editCategoy(storeId, categoryId, updatedCategory);
		return new ResponseEntity<>(categories, HttpStatus.OK);
	}

	@PatchMapping("/{storeId}/categories/{categoryId}/partial-update")
	public ResponseEntity<CategoriesDTO> partialUpdate(@PathVariable String storeId, @PathVariable String categoryId,
			@RequestBody Toggle toggle) {
		CategoriesDTO category = categoryService.partialUpdate(storeId, categoryId, toggle.getActive());
		return new ResponseEntity<>(category, HttpStatus.ACCEPTED);

	}

	@PostMapping("/{storeId}/products")
	public ResponseEntity<List<ProductsDTO>> addProduct(@PathVariable String storeId,
			@RequestBody AddProduct addProduct) {
		List<ProductsDTO> products = productService.addProduct(storeId, addProduct);
		return new ResponseEntity<>(products, HttpStatus.CREATED);
	}

	@GetMapping("/public/{storeId}/products")
	@PublicEndpoint
	public ResponseEntity<List<ProductsDTO>> getProduct(@PathVariable String storeId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		List<ProductsDTO> products = productService.getProducts(storeId, page, size);
		return new ResponseEntity<>(products, HttpStatus.OK);
	}

	@GetMapping("/{storeId}/products")
	public ResponseEntity<List<ProductsDTO>> getBuyerProducts(@PathVariable String storeId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size)  {
		
		List<ProductsDTO> products = productService.getProducts(storeId,page,size);
		return new ResponseEntity<>(products, HttpStatus.OK);
	}

	@GetMapping("/public/{storeId}/categories/{categoryId}/products")
	@PublicEndpoint
	public ResponseEntity<List<ProductsDTO>> getProductByCategoryId(@PathVariable String storeId,
			@PathVariable String categoryId) {
		List<ProductsDTO> products = productService.getProductByCategoryId(storeId, categoryId);
		return new ResponseEntity<>(products, HttpStatus.OK);
	}

	@PatchMapping("/{storeId}/products/{productId}/partial-update")
	public ResponseEntity<ProductsDTO> partialUpdateProduct(@PathVariable String storeId,
			@PathVariable String productId, @RequestBody Toggle toggle) {
		ProductsDTO product = productService.partialUpdate(storeId, productId, toggle.getActive());
		return new ResponseEntity<>(product, HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/{storeId}/products/{productId}")
	public ResponseEntity<String> deleteProduct(@PathVariable String storeId, @PathVariable String productId) {
		boolean isDeleted = productService.deleteProduct(storeId, productId);
		if (isDeleted) {
			return ResponseEntity.ok("Product deleted successfully");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product does not exist");
		}
	}

	@PatchMapping("/{storeId}/products/{productId}")
	public ResponseEntity<ProductsDTO> updateProduct(@PathVariable String storeId, @PathVariable String productId,
			@RequestBody AddProduct addProduct) {
		ProductsDTO updatedProduct = productService.updateProduct(storeId, productId, addProduct);
		return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
	}

	@GetMapping("/public/{storeId}/products/{productId}")
	@PublicEndpoint
	public ResponseEntity<ProductsDTO> getProductById(@PathVariable String storeId, @PathVariable String productId) {
		ProductsDTO product = productService.getProductByProductId(storeId, productId);
		return new ResponseEntity<>(product, HttpStatus.OK);
	}
}
