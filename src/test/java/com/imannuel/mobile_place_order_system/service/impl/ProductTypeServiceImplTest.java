package com.imannuel.mobile_place_order_system.service.impl;

import com.imannuel.mobile_place_order_system.constant.MessageConstants;
import com.imannuel.mobile_place_order_system.dto.request.product.ProductTypeRequest;
import com.imannuel.mobile_place_order_system.dto.response.product.ProductTypeResponse;
import com.imannuel.mobile_place_order_system.entity.ProductType;
import com.imannuel.mobile_place_order_system.repository.ProductTypeRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductTypeServiceImplTest {
    @Mock
    private ProductTypeRepository productTypeRepository;

    @Mock
    private ValidationUtility validationUtility;

    @InjectMocks
    private ProductTypeServiceImpl productTypeService;

    @Test
    void shouldReturnProductTypeResponseWhenAddProductType() {
        ProductTypeRequest expectedProductTypeRequest = ProductTypeRequest.builder()
                .name("Book")
                .build();

        Mockito.doNothing().when(validationUtility).validate(expectedProductTypeRequest);
        Mockito.when(productTypeRepository
                        .existsByDeletedFalseAndNameEqualsIgnoreCase(
                                Mockito.anyString()
                        ))
                .thenReturn(false);

        ProductTypeResponse productTypeResponse = productTypeService.addProductType(expectedProductTypeRequest);

        assertEquals(expectedProductTypeRequest.getName(), productTypeResponse.getName());
        Mockito.verify(productTypeRepository, Mockito.times(1))
                .existsByDeletedFalseAndNameEqualsIgnoreCase(Mockito.anyString());
    }

    @Test
    void shouldThrowErrorWhenAddProductType() {
        ProductTypeRequest expectedProductTypeRequest = ProductTypeRequest.builder()
                .name("Book")
                .build();
        Mockito.doNothing().when(validationUtility).validate(expectedProductTypeRequest);
        Mockito.when(productTypeRepository
                        .existsByDeletedFalseAndNameEqualsIgnoreCase(
                                Mockito.anyString()
                        ))
                .thenReturn(true);

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class,
                () -> productTypeService.addProductType(expectedProductTypeRequest));

        assertEquals(MessageConstants.PRODUCT_TYPE_ALREADY_EXISTS, responseStatusException.getReason());
        Mockito.verify(productTypeRepository,
                Mockito.times(1)).existsByDeletedFalseAndNameEqualsIgnoreCase(
                Mockito.anyString()
        );
    }

    @Test
    void shouldReturnProductTypeWhenFindActiveProductTypeById() {
        Integer productTypeId = 1;
        ProductType expectedProductType = ProductType.builder()
                .id(productTypeId)
                .name("Book")
                .build();

        Mockito.when(productTypeRepository.findByIdAndDeletedFalse(productTypeId))
                .thenReturn(Optional.of(expectedProductType));

        ProductType productType = productTypeService.findActiveProductTypeById(productTypeId);

        assertEquals(expectedProductType, productType);
        Mockito.verify(productTypeRepository, Mockito.times(1))
                .findByIdAndDeletedFalse(Mockito.anyInt());
    }

    @Test
    void shouldThrowErrorWhenFindActiveProductTypeById() {
        Integer productTypeId = 1;

        Mockito.when(productTypeRepository.findByIdAndDeletedFalse(productTypeId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> productTypeService.findActiveProductTypeById(productTypeId));

        assertEquals(MessageConstants.PRODUCT_TYPE_NOT_FOUND, exception.getReason());
        Mockito.verify(productTypeRepository, Mockito.times(1))
                .findByIdAndDeletedFalse(Mockito.anyInt());
    }

    @Test
    void shouldReturnProductTypeResponseWhenFindActiveProductTypeByIdResponse() {
        Integer productTypeId = 1;
        ProductType expectedProductType = ProductType.builder()
                .id(productTypeId)
                .name("Book")
                .build();

        Mockito.when(productTypeRepository.findByIdAndDeletedFalse(productTypeId))
                .thenReturn(Optional.of(expectedProductType));

        ProductTypeResponse productTypeResponse = productTypeService.findActiveProductTypeByIdResponse(productTypeId);

        assertEquals(expectedProductType.getId(), productTypeResponse.getId());
        assertEquals(expectedProductType.getName(), productTypeResponse.getName());
        Mockito.verify(productTypeRepository, Mockito.times(1))
                .findByIdAndDeletedFalse(Mockito.anyInt());
    }

    @Test
    void shouldReturnPageProductTypeResponseWhenGetAllProductTypesResponse() {
        int page = 1;
        int size = 10;
        String sortBy = "name_asc";
        String name = "Book";

        ProductType productType = ProductType.builder()
                .id(1)
                .name("Book")
                .build();

        Page<ProductType> expectedProductTypePage = new PageImpl<>(List.of(productType));

        Mockito.when(productTypeRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(expectedProductTypePage);

        Page<ProductTypeResponse> productTypeResponsePage = productTypeService
                .getAllProductTypesResponse(page, size, sortBy, name);

        assertEquals(expectedProductTypePage.getContent().size(), productTypeResponsePage.getContent().size());
        Mockito.verify(productTypeRepository, Mockito.times(1))
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));
    }

    @Test
    void shouldReturnProductTypeResponseWhenUpdateProductById() {
        Integer productTypeId = 1;
        ProductTypeRequest productTypeRequest = ProductTypeRequest.builder()
                .name("Laptop")
                .build();

        ProductType existingProductType = ProductType.builder()
                .id(productTypeId)
                .name("Book")
                .build();

        Mockito.when(productTypeRepository.findByIdAndDeletedFalse(productTypeId))
                .thenReturn(Optional.of(existingProductType));
        Mockito.when(productTypeRepository.existsByDeletedFalseAndNameEqualsIgnoreCase("Laptop"))
                .thenReturn(false);

        ProductTypeResponse productTypeResponse = productTypeService.updateProductById(productTypeId, productTypeRequest);

        assertEquals(productTypeRequest.getName(), productTypeResponse.getName());
        Mockito.verify(productTypeRepository, Mockito.times(1))
                .saveAndFlush(Mockito.any(ProductType.class));
    }

    @Test
    void shouldThrowErrorWhenUpdateProductTypeAlreadyExists() {
        Integer productTypeId = 1;
        ProductTypeRequest productTypeRequest = ProductTypeRequest.builder()
                .name("Laptop")
                .build();

        ProductType existingProductType = ProductType.builder()
                .id(productTypeId)
                .name("Book")
                .build();

        Mockito.when(productTypeRepository.findByIdAndDeletedFalse(productTypeId))
                .thenReturn(Optional.of(existingProductType));
        Mockito.when(productTypeRepository.existsByDeletedFalseAndNameEqualsIgnoreCase("Laptop")).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> productTypeService.updateProductById(productTypeId, productTypeRequest));

        assertEquals(MessageConstants.PRODUCT_TYPE_ALREADY_EXISTS, exception.getReason());
        Mockito.verify(productTypeRepository, Mockito.times(0))
                .saveAndFlush(Mockito.any(ProductType.class));
    }

    @Test
    void shouldDeleteProductType() {
        Integer productTypeId = 1;
        ProductType productType = ProductType.builder()
                .id(productTypeId)
                .name("Book")
                .deleted(false)
                .build();

        Mockito.when(productTypeRepository.findByIdAndDeletedFalse(productTypeId)).thenReturn(Optional.of(productType));
        Mockito.when(productTypeRepository.exists(Mockito.any(Specification.class))).thenReturn(false);

        productTypeService.deleteProductType(productTypeId);

        assertTrue(productType.isDeleted());
        Mockito.verify(productTypeRepository, Mockito.times(1))
                .save(Mockito.any(ProductType.class));
    }

    @Test
    void shouldThrowErrorWhenDeleteProductTypeIsUsed() {
        Integer productTypeId = 1;
        ProductType productType = ProductType.builder()
                .id(productTypeId)
                .name("Book")
                .deleted(false)
                .build();

        Mockito.when(productTypeRepository.findByIdAndDeletedFalse(productTypeId)).thenReturn(Optional.of(productType));
        Mockito.when(productTypeRepository.exists(Mockito.any(Specification.class))).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> productTypeService.deleteProductType(productTypeId));

        assertEquals(MessageConstants.PRODUCT_TYPE_IS_USED_BY_PRODUCT, exception.getReason());
        Mockito.verify(productTypeRepository, Mockito.times(0))
                .save(Mockito.any(ProductType.class));
    }

}