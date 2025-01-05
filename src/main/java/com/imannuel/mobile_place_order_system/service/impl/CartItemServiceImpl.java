package com.imannuel.mobile_place_order_system.service.impl;

import com.imannuel.mobile_place_order_system.constant.MessageConstants;
import com.imannuel.mobile_place_order_system.entity.Cart;
import com.imannuel.mobile_place_order_system.entity.CartItem;
import com.imannuel.mobile_place_order_system.entity.Product;
import com.imannuel.mobile_place_order_system.repository.CartItemRepository;
import com.imannuel.mobile_place_order_system.service.CartItemService;
import com.imannuel.mobile_place_order_system.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addItemToCart(Cart cart, String productId, Integer quantity) {
        productService.hasEnoughStock(productId, quantity);
        Product product = productService.findProductById(productId);

        if (cartItemRepository.existsByCartAndProduct(cart, product)) {
            updateCartItem(cart, productId, quantity);
            return;
        }

        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(quantity)
                .build();
        cartItemRepository.saveAndFlush(cartItem);
    }

    @Override
    @Transactional(readOnly = true)
    public CartItem findCartItemByIdAndCart(String cartItemId, Cart cart) {
        return cartItemRepository.findByIdAndCart(cartItemId, cart).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, MessageConstants.CART_ITEM_NOT_FOUND)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CartItem findCartItemByCartAndProduct(Cart cart, String productId) {
        Product product = productService.findProductById(productId);

        return cartItemRepository.findByCartAndProduct(cart, product).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, MessageConstants.CART_NOT_FOUND)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCartItem(Cart cart, String productId, Integer quantity) {
        CartItem cartItem = findCartItemByCartAndProduct(cart, productId);
        cartItem.setQuantity(quantity);
        cartItemRepository.saveAndFlush(cartItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeItemFromCart(String cartItemId, Cart cart) {
        CartItem cartItem = findCartItemByIdAndCart(cartItemId, cart);
        cartItemRepository.delete(cartItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearAllItemFromCart(Cart cart) {
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        cartItemRepository.deleteAll(cartItems);
    }
}
