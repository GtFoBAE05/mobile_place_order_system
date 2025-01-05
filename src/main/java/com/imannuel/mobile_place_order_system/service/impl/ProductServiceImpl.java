package com.imannuel.mobile_place_order_system.service.impl;

import com.imannuel.mobile_place_order_system.constant.MessageConstants;
import com.imannuel.mobile_place_order_system.dto.mapper.ProductMapper;
import com.imannuel.mobile_place_order_system.dto.request.product.ProductRequest;
import com.imannuel.mobile_place_order_system.dto.response.product.ProductResponse;
import com.imannuel.mobile_place_order_system.entity.Product;
import com.imannuel.mobile_place_order_system.entity.ProductType;
import com.imannuel.mobile_place_order_system.repository.ProductRepository;
import com.imannuel.mobile_place_order_system.repository.specification.ProductSpecification;
import com.imannuel.mobile_place_order_system.service.ProductService;
import com.imannuel.mobile_place_order_system.service.ProductTypeService;
import com.imannuel.mobile_place_order_system.utility.FilteringSortingUtility;
import com.imannuel.mobile_place_order_system.utility.ValidationUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductTypeService productTypeService;
    private final ValidationUtility validationUtility;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductResponse addProduct(ProductRequest productRequest) {
        validationUtility.validate(productRequest);

        ProductType productType = productTypeService.findActiveProductTypeById(productRequest.getProductTypeId());
        Product product = ProductMapper.toEntity(productRequest, productType);
        productRepository.saveAndFlush(product);

        return ProductMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Product findActiveProductById(String id) {
        return productRepository.findByIdAndDeletedFalse(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, MessageConstants.PRODUCT_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findActiveProductByIdResponse(String id) {
        Product product = findActiveProductById(id);
        return ProductMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProductResponse(Integer page, Integer size, String sortBy, String name, Integer productTypeId, String price, String stock) {
        Specification<Product> specifications = ProductSpecification.getSpecification(name, productTypeId, price, stock);
        Pageable pageable = FilteringSortingUtility.createPageable(page, size, sortBy);

        Page<Product> products = productRepository.findAll(specifications, pageable);

        return products.map(ProductMapper::toResponse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductResponse updateProductById(String id, ProductRequest productRequest) {
        validationUtility.validate(productRequest);

        Product product = findActiveProductById(id);
        ProductType productType = productTypeService.findActiveProductTypeById(productRequest.getProductTypeId());

        product.setName(productRequest.getName());
        product.setStock(productRequest.getStock());
        product.setPrice(productRequest.getPrice());
        product.setProductType(productType);
        productRepository.saveAndFlush(product);

        return ProductMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public void hasEnoughStock(String productId, Integer quantity) {
        Product product = findActiveProductById(productId);
        if (product.getStock() < quantity) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Not enough product stock for product: %s", product.getName()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reduceProductStock(String productId, Integer quantity) {
        Product product = findProductById(productId);

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProduct(String id) {
        Product product = findActiveProductById(id);

        product.setDeleted(true);
        product.setDeletedAt(LocalDateTime.now());
        productRepository.save(product);
    }
}
