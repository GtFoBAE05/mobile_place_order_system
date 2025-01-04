package com.imannuel.mobile_place_order_system.service.impl;

import com.imannuel.mobile_place_order_system.constant.MessageConstants;
import com.imannuel.mobile_place_order_system.dto.mapper.ProductTypeMapper;
import com.imannuel.mobile_place_order_system.dto.request.product.ProductTypeRequest;
import com.imannuel.mobile_place_order_system.dto.response.product.ProductTypeResponse;
import com.imannuel.mobile_place_order_system.entity.ProductType;
import com.imannuel.mobile_place_order_system.repository.ProductTypeRepository;
import com.imannuel.mobile_place_order_system.repository.specification.ProductTypeSpecification;
import com.imannuel.mobile_place_order_system.service.ProductTypeService;
import com.imannuel.mobile_place_order_system.utility.FilteringSortingUtility;
import com.imannuel.mobile_place_order_system.utility.ValidationUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductTypeServiceImpl implements ProductTypeService {
    private final ProductTypeRepository productTypeRepository;
    private final ValidationUtility validationUtility;

    @Override
    public ProductTypeResponse addProductType(ProductTypeRequest productTypeRequest) {
        validationUtility.validate(productTypeRequest);

        ProductType productType = ProductTypeMapper.toEntity(productTypeRequest);

        if (productTypeRepository.existsByNameEqualsIgnoreCase(productTypeRequest.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, MessageConstants.PRODUCT_TYPE_ALREADY_EXISTS);
        }
        productTypeRepository.saveAndFlush(productType);

        return ProductTypeMapper.toResponse(productType);
    }

    @Override
    public ProductType findProductTypeById(Integer id) {
        return productTypeRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, MessageConstants.PRODUCT_TYPE_NOT_FOUND));
    }

    @Override
    public ProductTypeResponse findProductTypeByIdResponse(Integer id) {
        ProductType productType = findProductTypeById(id);
        return ProductTypeMapper.toResponse(productType);
    }

    @Override
    public Page<ProductTypeResponse> getAllProductTypesResponse(Integer page, Integer size, String sortBy, String name) {
        Specification<ProductType> specifications = ProductTypeSpecification.getSpecification(name);
        Pageable pageable = FilteringSortingUtility.createPageable(page, size, sortBy);

        Page<ProductType> productTypes = productTypeRepository.findAll(specifications, pageable);

        return productTypes.map(ProductTypeMapper::toResponse);
    }

    @Override
    public ProductTypeResponse updateProductById(Integer id, ProductTypeRequest productTypeRequest) {
        validationUtility.validate(productTypeRequest);

        ProductType productType = findProductTypeById(id);

        if (productTypeRepository.existsByNameEqualsIgnoreCase(productTypeRequest.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, MessageConstants.PRODUCT_TYPE_ALREADY_EXISTS);
        }
        productType.setName(productTypeRequest.getName());
        productTypeRepository.saveAndFlush(productType);

        return ProductTypeMapper.toResponse(productType);
    }

    @Override
    public void deleteProductType(Integer id) {
        ProductType productType = findProductTypeById(id);

        productTypeRepository.delete(productType);
    }
}
