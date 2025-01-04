package com.imannuel.mobile_place_order_system.service;

import com.imannuel.mobile_place_order_system.dto.request.product.ProductTypeRequest;
import com.imannuel.mobile_place_order_system.dto.response.product.ProductTypeResponse;
import com.imannuel.mobile_place_order_system.entity.ProductType;
import org.springframework.data.domain.Page;

public interface ProductTypeService {
    ProductTypeResponse addProductType(ProductTypeRequest productTypeRequest);

    ProductType findProductTypeById(Integer id);

    ProductTypeResponse findProductTypeByIdResponse(Integer id);

    Page<ProductTypeResponse> getAllProductTypesResponse(Integer page, Integer size, String sortBy, String name);

    ProductTypeResponse updateProductById(Integer id, ProductTypeRequest productTypeRequest);

    void deleteProductType(Integer id);
}
