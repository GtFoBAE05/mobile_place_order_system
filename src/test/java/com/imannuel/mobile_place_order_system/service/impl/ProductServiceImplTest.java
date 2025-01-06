package com.imannuel.mobile_place_order_system.service.impl;

import com.imannuel.mobile_place_order_system.constant.MessageConstants;
import com.imannuel.mobile_place_order_system.dto.request.product.ProductRequest;
import com.imannuel.mobile_place_order_system.dto.response.product.ProductResponse;
import com.imannuel.mobile_place_order_system.entity.Product;
import com.imannuel.mobile_place_order_system.entity.ProductType;
import com.imannuel.mobile_place_order_system.repository.ProductRepository;
import com.imannuel.mobile_place_order_system.service.ProductTypeService;
import com.imannuel.mobile_place_order_system.utility.ValidationUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductTypeService productTypeService;

    @Mock
    private ValidationUtility validationUtility;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void shouldCallProductRepositoryWhenAddProduct() {
        ProductType productType = ProductType.builder()
                .id(1)
                .name("Laptop")
                .build();
        ProductRequest productRequest = ProductRequest.builder()
                .name("Macbook")
                .stock(10)
                .price(5000000L)
                .productTypeId(1)
                .build();

        Mockito.doNothing().when(validationUtility).validate(productRequest);
        Mockito.when(productTypeService.findActiveProductTypeById(1)).thenReturn(productType);

        productService.addProduct(productRequest);

        Mockito.verify(productRepository, Mockito.times(1))
                .saveAndFlush(Mockito.any(Product.class));
    }

    @Test
    void shouldReturnProductWhenFindActiveProductById() {
        String productId = UUID.randomUUID().toString();
        ProductType productType = ProductType.builder()
                .id(1)
                .name("Laptop")
                .build();
        Product expectedProduct = Product.builder()
                .id(productId)
                .name("Macbook")
                .stock(20)
                .price(5000000L)
                .productType(productType)
                .deleted(false)
                .build();

        Mockito.when(productRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(expectedProduct));

        Product product = productService.findActiveProductById(productId);

        assertEquals(expectedProduct, product);
        Mockito.verify(productRepository, Mockito.times(1))
                .findByIdAndDeletedFalse(Mockito.anyString());
    }

    @Test
    void shouldThrowErrorWhenFindActiveProductByIdNotFound() {
        String productId = UUID.randomUUID().toString();

        Mockito.when(productRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> productService.findActiveProductById(productId));

        assertEquals(MessageConstants.PRODUCT_NOT_FOUND, exception.getReason());
        Mockito.verify(productRepository, Mockito.times(1))
                .findByIdAndDeletedFalse(Mockito.anyString());
    }

    @Test
    void shouldReturnProductResponseWhenFindActiveProductByIdResponse() {
        String productId = UUID.randomUUID().toString();
        ProductType productType = ProductType.builder()
                .id(1)
                .name("Laptop")
                .build();
        Product expectedProduct = Product.builder()
                .id(productId)
                .name("Macbook")
                .stock(20)
                .price(5000000L)
                .productType(productType)
                .deleted(false)
                .build();

        Mockito.when(productRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(expectedProduct));

        ProductResponse productResponse = productService.findActiveProductByIdResponse(productId);

        assertEquals(expectedProduct.getId(), productResponse.getProductId());
        assertEquals(expectedProduct.getName(), productResponse.getProductName());
        assertEquals(expectedProduct.getStock(), productResponse.getStock());
        assertEquals(expectedProduct.getPrice(), productResponse.getPrice());
        Mockito.verify(productRepository, Mockito.times(1))
                .findByIdAndDeletedFalse(Mockito.anyString());
    }

    @Test
    void shouldReturnPageProductResponseWhenGetAllProductResponse() {
        int page = 1;
        int size = 10;
        String sortBy = "name_asc";
        String name = "Macbook";
        Integer productTypeId = 1;
        String price = "gte:500000";
        String stock = "gt:5";

        ProductType productType = ProductType.builder()
                .id(1)
                .name("Laptop")
                .build();
        Product expectedProduct = Product.builder()
                .id(UUID.randomUUID().toString())
                .name("Macbook")
                .stock(20)
                .price(5000000L)
                .productType(productType)
                .deleted(false)
                .build();

        Page<Product> expectedProductPage = new PageImpl<>(List.of(expectedProduct));

        Mockito.when(productRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(expectedProductPage);

        Page<ProductResponse> productResponsePage = productService.getAllProductResponse(
                page, size, sortBy, name, productTypeId, price, stock);

        assertEquals(expectedProductPage.getContent().size(), productResponsePage.getContent().size());
        Mockito.verify(productRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));
    }

    @Test
    void shouldReturnProductResponseWhenUpdateProductById() {
        String productId = UUID.randomUUID().toString();
        ProductType productType = ProductType.builder().id(1)
                .name("Laptop")
                .build();
        ProductRequest productRequest = ProductRequest.builder()
                .name("Macbook")
                .stock(10)
                .price(5000000L)
                .productTypeId(productType.getId())
                .build();

        Product existingProduct = Product.builder()
                .id(productId)
                .name("Thinkpad")
                .stock(15)
                .price(8000000L)
                .productType(productType)
                .build();

        Mockito.when(productRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(existingProduct));
        Mockito.when(productTypeService.findActiveProductTypeById(1)).thenReturn(productType);
        Mockito.when(productRepository.saveAndFlush(Mockito.any(Product.class))).thenReturn(existingProduct);

        ProductResponse productResponse = productService.updateProductById(productId, productRequest);

        assertEquals(productRequest.getName(), productResponse.getProductName());
        assertEquals(productRequest.getStock(), productResponse.getStock());
        assertEquals(productRequest.getPrice(), productResponse.getPrice());
        Mockito.verify(productRepository, Mockito.times(1))
                .saveAndFlush(Mockito.any(Product.class));
    }

    @Test
    void shouldThrowErrorWhenUpdateProductByIdProductNotFound() {
        String productId = UUID.randomUUID().toString();
        ProductRequest productRequest = ProductRequest.builder()
                .name("Macbook")
                .stock(10)
                .price(5000000L)
                .build();

        Mockito.when(productRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> productService.updateProductById(productId, productRequest));

        assertEquals(MessageConstants.PRODUCT_NOT_FOUND, exception.getReason());
        Mockito.verify(productRepository, Mockito.times(0))
                .saveAndFlush(Mockito.any(Product.class));
    }

    @Test
    void shouldThrowErrorWhenNotEnoughStock() {
        String productId = UUID.randomUUID().toString();
        Product product = Product.builder()
                .id(productId)
                .name("Macbook")
                .stock(9)
                .price(5000000L)
                .build();

        Mockito.when(productRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(product));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> productService.hasEnoughStock(productId, 10));

        assertEquals(String.format("Not enough product stock for product: %s",
                product.getName()), exception.getReason());
        Mockito.verify(productRepository, Mockito.times(1))
                .findByIdAndDeletedFalse(Mockito.anyString());
    }

    @Test
    void shouldReduceProductStockWhenReduceProductStock() {
        String productId = UUID.randomUUID().toString();
        Product product = Product.builder()
                .id(productId)
                .name("Macbook")
                .stock(9)
                .price(5000000L)
                .build();

        Mockito.when(productRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

        productService.reduceProductStock(productId, 1);

        assertEquals(8, product.getStock());
        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any(Product.class));
    }

    @Test
    void shouldDeleteProduct() {
        String productId = UUID.randomUUID().toString();
        Product product = Product.builder()
                .id(productId)
                .name("Macbook")
                .stock(9)
                .price(5000000L)
                .build();

        Mockito.when(productRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

        productService.deleteProduct(productId);

        assertTrue(product.isDeleted());
        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any(Product.class));
    }

}