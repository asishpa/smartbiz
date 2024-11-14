package com.smartbiz.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import com.smartbiz.dto.BuyerAddressDTO;
import com.smartbiz.dto.CartItemDTO;
import com.smartbiz.dto.CartResponseDTO;
import com.smartbiz.dto.DeliveryChargesDTO;
import com.smartbiz.dto.OfferDTO;
import com.smartbiz.dto.OrderDTO;
import com.smartbiz.dto.OrderItemDTO;
import com.smartbiz.dto.ProductWarehouseDTO;
import com.smartbiz.dto.ProductsDTO;
import com.smartbiz.dto.StoreDTO;
import com.smartbiz.dto.WarehouseDTO;
import com.smartbiz.dto.WarehouseInventoryDTO;
import com.smartbiz.entity.BuyerAddress;
import com.smartbiz.entity.Cart;
import com.smartbiz.entity.CartItem;
import com.smartbiz.entity.Delivery;
import com.smartbiz.entity.Offer;
import com.smartbiz.entity.OrderItem;
import com.smartbiz.entity.Orders;
import com.smartbiz.entity.ProductPhoto;
import com.smartbiz.entity.ProductWarehouseInventory;
import com.smartbiz.entity.Products;
import com.smartbiz.entity.Store;
import com.smartbiz.entity.Warehouse;

@Mapper(componentModel = "spring")
public interface EntityMapper {
	WarehouseDTO toDto(Warehouse warehouse);

	Warehouse toEntity(WarehouseDTO dto);

	@Mapping(source = "category.categoryName", target = "categoryName")
	@Mapping(source = "store.name", target = "storeName")
	@Mapping(source = "photos", target = "photoPublicId", qualifiedByName = "mapPhotosToPublicIds")
	@Mapping(source = "warehouseInventories", target = "inventory") 
	ProductsDTO toProductsDTO(Products product);

	List<ProductsDTO> toProductsDTOs(List<Products> products);

	@Mapping(source = "warehouse.warehouseName", target = "warehouseName")
	ProductWarehouseDTO toProductWarehouseDTO(ProductWarehouseInventory inventory);

	List<ProductWarehouseDTO> toProductWarehouseDTOs(List<ProductWarehouseInventory> inventories);

	StoreDTO toStoreDTO(Store store);
	OfferDTO toOfferDTO(Offer offer);
	
	List<OfferDTO> toOfferDTO(List<Offer> offer);
	
	List<BuyerAddressDTO> toAddressDTO(List<BuyerAddress> buyerAddress);
	
	@Named("mapPhotosToPublicIds")
	 static List<String> mapPhotosToPublicIds(List<ProductPhoto> photos) {
		return photos.stream().map(ProductPhoto::getPublicId).collect(Collectors.toList());
	}
	DeliveryChargesDTO toDeliveryChargeDTO(Delivery delivery);
	
	CartResponseDTO toCartResponseDTO(Cart cart);
	@Mappings({
		@Mapping(source = "product.id", target = "productId"),
		@Mapping(source = "product.productName",target = "productName"),
		@Mapping(source = "product.discountedPrice",target = "discountedPrice"),
		@Mapping(source = "product.actualPrice",target = "actualPrice"),
		@Mapping(source = "product.photos", target = "productPhoto", qualifiedByName = "mapPhotosToPublicIds")
	})
	CartItemDTO toCartItemDTO(CartItem cartItem);
	BuyerAddressDTO toAddressDto(BuyerAddress buyerAddress);
	@Mappings({
        @Mapping(source = "product.id", target = "productId"),
        @Mapping(source = "product.productName", target = "productName"),
        @Mapping(source = "product.productDesc", target = "productDescription"),
        @Mapping(source = "product.photos", target = "productImage", qualifiedByName = "mapPhotosToPublicIds"),
        @Mapping(source = "qty", target = "qty"),
        @Mapping(source = "price", target = "price"),
       // @Mapping(source = "subtotal", target = "subtotal")
    })
    OrderItemDTO toOrderItemDTO(OrderItem orderItem);
	@Mappings({
		@Mapping(source = "store.name",target = "storeName"),
		@Mapping(source = "customer.userName",target = "customerName")
	})
	OrderDTO toOrderDTO(Orders order);
	
	Orders toOrderEntity(OrderDTO order);
}
