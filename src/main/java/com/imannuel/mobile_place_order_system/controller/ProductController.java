package com.imannuel.mobile_place_order_system.controller;

import com.imannuel.mobile_place_order_system.constant.ApiEndpointConstants;
import com.imannuel.mobile_place_order_system.constant.MessageConstants;
import com.imannuel.mobile_place_order_system.dto.mapper.ResponseMapper;
import com.imannuel.mobile_place_order_system.dto.request.product.ProductRequest;
import com.imannuel.mobile_place_order_system.dto.response.product.ProductResponse;
import com.imannuel.mobile_place_order_system.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = ApiEndpointConstants.PRODUCT_ENDPOINT)
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(
            @RequestBody ProductRequest productRequest
    ) {
        ProductResponse productResponse = productService.addProduct(productRequest);
        return ResponseMapper.toCommonResponse(true, HttpStatus.CREATED,
                MessageConstants.PRODUCT_SUCCESS_CREATE, productResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findProductById(
            @PathVariable String id
    ) {
        ProductResponse productResponse = productService.findActiveProductByIdResponse(id);
        return ResponseMapper.toCommonResponse(true, HttpStatus.OK,
                MessageConstants.PRODUCT_SUCCESS_FIND, productResponse);
    }

    @GetMapping()
    public ResponseEntity<?> getAllProduct(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer productTypeId,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String stock
    ) {
        Page<ProductResponse> productResponse = productService.getAllProductResponse
                (page, size, sortBy, name, productTypeId, price, stock);
        return ResponseMapper.toCommonResponseWithPagination(true, HttpStatus.OK,
                MessageConstants.PRODUCT_SUCCESS_FETCH_ALL, productResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable String id,
            @RequestBody ProductRequest productRequest
    ) {
        ProductResponse productResponse = productService.updateProductById(id, productRequest);
        return ResponseMapper.toCommonResponse(true, HttpStatus.OK,
                MessageConstants.PRODUCT_SUCCESS_UPDATE, productResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable String id
    ) {
        productService.deleteProduct(id);
        return ResponseMapper.toCommonResponse(true, HttpStatus.OK,
                MessageConstants.PRODUCT_SUCCESS_DELETE, null);
    }
}
