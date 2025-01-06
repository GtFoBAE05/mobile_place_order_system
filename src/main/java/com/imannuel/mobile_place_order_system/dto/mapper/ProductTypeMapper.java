package com.imannuel.mobile_place_order_system.dto.mapper;

import com.imannuel.mobile_place_order_system.dto.request.product.ProductTypeRequest;
import com.imannuel.mobile_place_order_system.dto.response.product.ProductTypeResponse;
import com.imannuel.mobile_place_order_system.entity.ProductType;

public class ProductTypeMapper {
    public static ProductType toEntity(ProductTypeRequest productTypeRequest){
        return ProductType.builder()
                .name(productTypeRequest.getName())
                .build();
    }

    public static ProductTypeResponse toResponse(ProductType productType){
        return ProductTypeResponse.builder()
                .productTypeId(productType.getId())
                .productTypeName(productType.getName())
                .build();
    }
}