package com.imannuel.mobile_place_order_system.service.impl;

import com.imannuel.mobile_place_order_system.constant.MessageConstants;
import com.imannuel.mobile_place_order_system.entity.Cart;
import com.imannuel.mobile_place_order_system.entity.CartItem;
import com.imannuel.mobile_place_order_system.entity.Customer;
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
        Product product = productService.findActiveProductById(productId);

        if (cartItemRepository.existsByCartAndProduct(cart, product)) {
            updateCartItem(cart, productId, quantity);
            return;
        }

        addNewCartItem(cart, product, quantity);
    }

    @Override
    public CartItem findCartItemById(String cartItemId) {
        return cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, MessageConstants.CART_ITEM_NOT_FOUND)
        );
    }

    @Override
    public CartItem findCartItemByIdAndCustomer(String cartItemId, Customer customer) {
        return cartItemRepository.findByIdAndCart_Customer(cartItemId, customer).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, MessageConstants.CART_ITEM_CUSTOMER_MISMATCH)
        );
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
        Product product = productService.findActiveProductById(productId);

        return cartItemRepository.findByCartAndProduct(cart, product).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, MessageConstants.CART_NOT_FOUND)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCartItem(Cart cart, String productId, Integer quantity) {
        CartItem cartItem = findCartItemByCartAndProduct(cart, productId);
        productService.hasEnoughStock(productId, quantity);
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

        if (cartItems.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageConstants.CART_ITEM_ALREADY_EMPTY);
        }

        cartItemRepository.deleteAll(cartItems);
    }

    @Override
    public void deleteCartItem(String cartItemId) {
        CartItem cartItem = findCartItemById(cartItemId);
        cartItemRepository.delete(cartItem);
    }

    private void addNewCartItem(Cart cart, Product product, Integer quantity) {
        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(quantity)
                .build();
        cartItemRepository.saveAndFlush(cartItem);
    }
}
