package com.imannuel.mobile_place_order_system.service;

import com.imannuel.mobile_place_order_system.dto.request.product.ProductRequest;
import com.imannuel.mobile_place_order_system.dto.response.product.ProductResponse;
import com.imannuel.mobile_place_order_system.entity.Product;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductResponse addProduct(ProductRequest productRequest);

    Product findActiveProductById(String id);

    ProductResponse findActiveProductByIdResponse(String id);

    Page<ProductResponse> getAllProductResponse(Integer page, Integer size, String sortBy, String name,
                                                Integer productTypeId, String price, String stock);

    ProductResponse updateProductById(String id, ProductRequest productRequest);

    void hasEnoughStock(String productId, Integer quantity);

    void deleteProduct(String id);
}
