package com.imannuel.mobile_place_order_system.controller;

import com.imannuel.mobile_place_order_system.constant.ApiEndpointConstants;
import com.imannuel.mobile_place_order_system.constant.MessageConstants;
import com.imannuel.mobile_place_order_system.dto.mapper.ResponseMapper;
import com.imannuel.mobile_place_order_system.dto.request.cart.CartRequest;
import com.imannuel.mobile_place_order_system.dto.response.cart.CartResponse;
import com.imannuel.mobile_place_order_system.service.CustomerCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = ApiEndpointConstants.CART_ENDPOINT)
@RequiredArgsConstructor
public class CartController {
    private final CustomerCartService customerCartService;

    @PostMapping("/{customerId}")
    public ResponseEntity<?> addCartItem(
            @PathVariable(name = "customerId") String customerId,
            @RequestBody CartRequest cartRequest
    ) {
        CartResponse cartResponse = customerCartService.addCartItem(customerId, cartRequest);
        return ResponseMapper.toCommonResponse(true, HttpStatus.CREATED,
                MessageConstants.CART_SUCCESS_ADD_PRODUCT, cartResponse);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomerCart(
            @PathVariable(name = "customerId") String customerId
    ) {
        CartResponse customerCart = customerCartService.getCustomerCart(customerId);
        return ResponseMapper.toCommonResponse(true, HttpStatus.OK,
                MessageConstants.CART_SUCCESS_FIND, customerCart);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<?> updateCustomerCartItem(
            @PathVariable(name = "customerId") String customerId,
            @RequestBody CartRequest cartRequest
    ) {
        CartResponse cartResponse = customerCartService.updateCartItem(customerId, cartRequest);
        return ResponseMapper.toCommonResponse(true, HttpStatus.OK,
                MessageConstants.CART_SUCCESS_UPDATE_ITEM, cartResponse);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> clearAllCartItem(
            @PathVariable(name = "customerId") String customerId
    ) {
        customerCartService.clearAllCartItem(customerId);
        return ResponseMapper.toCommonResponse(true, HttpStatus.OK,
                MessageConstants.CART_SUCCESS_CLEAR, null);
    }

    @DeleteMapping("/{customerId}/items/{cartItemId}")
    public ResponseEntity<?> removeCartItem(
            @PathVariable(name = "customerId") String customerId,
            @PathVariable(name = "cartItemId") String cartItemId
    ) {
        CartResponse cartResponse = customerCartService.removeItemFromCart(customerId, cartItemId);
        return ResponseMapper.toCommonResponse(true, HttpStatus.OK,
                MessageConstants.CART_SUCCESS_REMOVE_ITEM, cartResponse);
    }
}
