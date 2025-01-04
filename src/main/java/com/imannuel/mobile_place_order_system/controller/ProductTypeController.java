package com.imannuel.mobile_place_order_system.controller;

import com.imannuel.mobile_place_order_system.constant.ApiEndpointConstants;
import com.imannuel.mobile_place_order_system.constant.MessageConstants;
import com.imannuel.mobile_place_order_system.dto.mapper.ResponseMapper;
import com.imannuel.mobile_place_order_system.dto.request.product.ProductTypeRequest;
import com.imannuel.mobile_place_order_system.dto.response.product.ProductTypeResponse;
import com.imannuel.mobile_place_order_system.service.ProductTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = ApiEndpointConstants.PRODUCT_TYPE_ENDPOINT)
@RequiredArgsConstructor
public class ProductTypeController {
    private final ProductTypeService productTypeService;

    @PostMapping
    public ResponseEntity<?> createProductType(
            @RequestBody ProductTypeRequest productTypeRequest
    ) {
        ProductTypeResponse productTypeResponse = productTypeService.addProductType(productTypeRequest);
        return ResponseMapper.toCommonResponse(true, HttpStatus.CREATED,
                MessageConstants.PRODUCT_TYPE_SUCCESS_CREATE, productTypeResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findProductTypeById(
            @PathVariable Integer id
    ) {
        ProductTypeResponse productTypeResponse = productTypeService.findProductTypeByIdResponse(id);
        return ResponseMapper.toCommonResponse(true, HttpStatus.OK,
                MessageConstants.PRODUCT_TYPE_SUCCESS_FIND, productTypeResponse);
    }

    @GetMapping()
    public ResponseEntity<?> getAllProductType(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String name
    ) {
        Page<ProductTypeResponse> productTypesResponse = productTypeService.getAllProductTypesResponse(page, size, sortBy, name);
        return ResponseMapper.toCommonResponseWithPagination(true, HttpStatus.OK,
                MessageConstants.PRODUCT_TYPE_SUCCESS_FETCH_ALL, productTypesResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProductType(
            @PathVariable Integer id,
            @RequestBody ProductTypeRequest productTypeRequest
    ) {
        ProductTypeResponse productTypeResponse = productTypeService.updateProductById(id, productTypeRequest);
        return ResponseMapper.toCommonResponse(true, HttpStatus.OK,
                MessageConstants.PRODUCT_TYPE_SUCCESS_UPDATE, productTypeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductType(
            @PathVariable Integer id
    ) {
        productTypeService.deleteProductType(id);
        return ResponseMapper.toCommonResponse(true, HttpStatus.NO_CONTENT,
                MessageConstants.PRODUCT_TYPE_SUCCESS_DELETE, null);
    }
}
