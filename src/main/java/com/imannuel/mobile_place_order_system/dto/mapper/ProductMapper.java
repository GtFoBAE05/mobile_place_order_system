package com.imannuel.mobile_place_order_system.dto.mapper;

import com.imannuel.mobile_place_order_system.dto.request.product.ProductRequest;
import com.imannuel.mobile_place_order_system.dto.response.product.ProductResponse;
import com.imannuel.mobile_place_order_system.entity.Product;
import com.imannuel.mobile_place_order_system.entity.ProductType;

public class ProductMapper {
    public static Product toEntity(ProductRequest productRequest, ProductType productType) {
        return Product.builder()
                .name(productRequest.getName())
                .productType(productType)
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .build();
    }

    public static ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .productType(ProductTypeMapper.toResponse(product.getProductType()))
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }
}
