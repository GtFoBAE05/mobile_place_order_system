package com.imannuel.mobile_place_order_system.service.impl;

import com.imannuel.mobile_place_order_system.constant.MessageConstants;
import com.imannuel.mobile_place_order_system.entity.Cart;
import com.imannuel.mobile_place_order_system.entity.CartItem;
import com.imannuel.mobile_place_order_system.entity.Customer;
import com.imannuel.mobile_place_order_system.entity.Product;
import com.imannuel.mobile_place_order_system.repository.CartItemRepository;
import com.imannuel.mobile_place_order_system.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class CartItemServiceImplTest {
    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartItemServiceImpl cartItemService;

    @Test
    void shouldAddItemToCartWhenStockIsEnough() {
        String productId = UUID.randomUUID().toString();
        Product product = Product.builder()
                .id(productId)
                .name("Macbook")
                .stock(10)
                .price(5000000L)
                .build();
        Integer quantity = 3;
        Cart cart = Cart.builder()
                .id(UUID.randomUUID().toString())
                .build();

        Mockito.doNothing().when(productService).hasEnoughStock(productId, quantity);
        Mockito.when(productService.findActiveProductById(productId)).thenReturn(product);
        Mockito.when(cartItemRepository.existsByCartAndProduct(cart, product)).thenReturn(false);

        cartItemService.addItemToCart(cart, productId, quantity);

        Mockito.verify(cartItemRepository, Mockito.times(1)).saveAndFlush(Mockito.any(CartItem.class));
    }

    @Test
    void shouldReturnCartItemWhenFindCartItemById() {
        Product product = Product.builder()
                .id(UUID.randomUUID().toString())
                .name("Macbook")
                .stock(10)
                .price(5000000L)
                .build();
        Cart cart = Cart.builder()
                .id(UUID.randomUUID().toString())
                .build();
        String cartItemId = UUID.randomUUID().toString();
        CartItem expectedCartItem = CartItem.builder()
                .id(cartItemId)
                .cart(cart)
                .product(product)
                .quantity(3)
                .build();

        Mockito.when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(expectedCartItem));

        CartItem cartItem = cartItemService.findCartItemById(cartItemId);

        assertEquals(expectedCartItem, cartItem);
        Mockito.verify(cartItemRepository, Mockito.times(1)).findById(cartItemId);
    }

    @Test
    void shouldThrowErrorWhenFindCartItemByIdNotFound() {
        String cartItemId = UUID.randomUUID().toString();

        Mockito.when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> cartItemService.findCartItemById(cartItemId));

        assertEquals(MessageConstants.CART_ITEM_NOT_FOUND, exception.getReason());
        Mockito.verify(cartItemRepository, Mockito.times(1)).findById(cartItemId);
    }

    @Test
    void shouldReturnCartItemWhenFindCartItemByIdAndCustomer() {
        Customer customer = Customer.builder()
                .id(UUID.randomUUID().toString())
                .name("budi")
                .address("jalan ceria")
                .build();
        Product product = Product.builder()
                .id(UUID.randomUUID().toString())
                .name("Macbook")
                .stock(10)
                .price(5000000L)
                .build();
        Cart cart = Cart.builder()
                .id(UUID.randomUUID().toString())
                .build();
        String cartItemId = UUID.randomUUID().toString();
        CartItem expectedCartItem = CartItem.builder()
                .id(cartItemId)
                .cart(cart)
                .product(product)
                .quantity(3)
                .build();

        Mockito.when(cartItemRepository.findByIdAndCart_Customer(cartItemId, customer))
                .thenReturn(Optional.of(expectedCartItem));

        CartItem cartItem = cartItemService.findCartItemByIdAndCustomer(cartItemId, customer);

        assertEquals(expectedCartItem, cartItem);
        Mockito.verify(cartItemRepository, Mockito.times(1))
                .findByIdAndCart_Customer(cartItemId, customer);
    }

    @Test
    void shouldThrowErrorWhenFindCartItemByIdAndCustomerNotFound() {
        Customer customer = Customer.builder()
                .id(UUID.randomUUID().toString())
                .name("budi")
                .address("jalan ceria")
                .build();
        String cartItemId = UUID.randomUUID().toString();

        Mockito.when(cartItemRepository.findByIdAndCart_Customer(cartItemId, customer))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> cartItemService.findCartItemByIdAndCustomer(cartItemId, customer));

        assertEquals(MessageConstants.CART_ITEM_CUSTOMER_MISMATCH, exception.getReason());
        Mockito.verify(cartItemRepository, Mockito.times(1))
                .findByIdAndCart_Customer(cartItemId, customer);
    }

    @Test
    void shouldReturnCartItemWhenFindCartItemByIdAndCart() {
        Product product = Product.builder()
                .id(UUID.randomUUID().toString())
                .name("Macbook")
                .stock(10)
                .price(5000000L)
                .build();
        Cart cart = Cart.builder()
                .id(UUID.randomUUID().toString())
                .build();
        String cartItemId = UUID.randomUUID().toString();
        CartItem expectedCartItem = CartItem.builder()
                .id(cartItemId)
                .cart(cart)
                .product(product)
                .quantity(3)
                .build();

        Mockito.when(cartItemRepository.findByIdAndCart(cartItemId, cart))
                .thenReturn(Optional.of(expectedCartItem));

        CartItem cartItem = cartItemService.findCartItemByIdAndCart(cartItemId, cart);

        assertEquals(expectedCartItem, cartItem);
        Mockito.verify(cartItemRepository, Mockito.times(1))
                .findByIdAndCart(cartItemId, cart);
    }

    @Test
    void shouldThrowErrorWhenFindCartItemByIdAndCartNotFound() {
        String cartItemId = UUID.randomUUID().toString();
        Cart cart = Cart.builder()
                .id(UUID.randomUUID().toString())
                .build();

        Mockito.when(cartItemRepository.findByIdAndCart(cartItemId, cart))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> cartItemService.findCartItemByIdAndCart(cartItemId, cart));

        assertEquals(MessageConstants.CART_ITEM_NOT_FOUND, exception.getReason());
        Mockito.verify(cartItemRepository, Mockito.times(1))
                .findByIdAndCart(cartItemId, cart);
    }

    @Test
    void shouldReturnCartItemWhenFindCartItemByCartAndProduct() {
        Product product = Product.builder()
                .id(UUID.randomUUID().toString())
                .name("Macbook")
                .stock(10)
                .price(5000000L)
                .build();
        Cart cart = Cart.builder()
                .id(UUID.randomUUID().toString())
                .build();
        CartItem expectedCartItem = CartItem.builder()
                .id(UUID.randomUUID().toString())
                .cart(cart)
                .product(product)
                .quantity(3)
                .build();

        Mockito.when(productService.findActiveProductById(product.getId())).thenReturn(product);
        Mockito.when(cartItemRepository.findByCartAndProduct(cart, product))
                .thenReturn(Optional.of(expectedCartItem));

        CartItem cartItem = cartItemService.findCartItemByCartAndProduct(cart, product.getId());

        assertEquals(expectedCartItem, cartItem);
        Mockito.verify(cartItemRepository, Mockito.times(1))
                .findByCartAndProduct(cart, product);
    }

    @Test
    void shouldThrowErrorWhenFindCartItemByCartAndProductNotFound() {
        Product product = Product.builder()
                .id(UUID.randomUUID().toString())
                .name("Macbook")
                .stock(10)
                .price(5000000L)
                .build();
        Cart cart = Cart.builder()
                .id(UUID.randomUUID().toString())
                .build();

        Mockito.when(productService.findActiveProductById(product.getId())).thenReturn(product);
        Mockito.when(cartItemRepository.findByCartAndProduct(cart, product))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> cartItemService.findCartItemByCartAndProduct(cart, product.getId()));

        assertEquals(MessageConstants.CART_NOT_FOUND, exception.getReason());
        Mockito.verify(cartItemRepository, Mockito.times(1))
                .findByCartAndProduct(cart, product);
    }

    @Test
    void shouldUpdateCartItemWhenUpdateCartItem() {
        Cart cart = Cart.builder()
                .id(UUID.randomUUID().toString())
                .build();
        String productId = UUID.randomUUID().toString();
        Product product = Product.builder()
                .id(productId)
                .name("Macbook")
                .stock(10)
                .price(5000000L)
                .build();
        Integer quantity = 3;
        CartItem existingCartItem = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(2)
                .build();

        Mockito.doNothing().when(productService).hasEnoughStock(productId, quantity);
        Mockito.when(productService.findActiveProductById(productId)).thenReturn(product);
        Mockito.when(cartItemRepository.existsByCartAndProduct(cart, product)).thenReturn(true);
        Mockito.when(cartItemRepository.findByCartAndProduct(cart, product)).thenReturn(Optional.of(existingCartItem));

        cartItemService.addItemToCart(cart, productId, quantity);

        assertEquals(quantity, existingCartItem.getQuantity());
        Mockito.verify(cartItemRepository, Mockito.times(1)).saveAndFlush(existingCartItem);
    }

    @Test
    void shouldRemoveItemFromCartWhenRemoveItemFromCart() {
        String cartItemId = UUID.randomUUID().toString();
        Cart cart = Cart.builder()
                .id(UUID.randomUUID().toString())
                .build();
        CartItem cartItem = CartItem.builder()
                .id(cartItemId)
                .cart(cart)
                .build();

        Mockito.when(cartItemRepository.findByIdAndCart(cartItemId, cart)).thenReturn(Optional.of(cartItem));

        cartItemService.removeItemFromCart(cartItemId, cart);

        Mockito.verify(cartItemRepository, Mockito.times(1)).delete(cartItem);
    }

    @Test
    void shouldClearAllItemsFromCartWhenClearAllItemFromCart() {
        Cart cart = Cart.builder()
                .id(UUID.randomUUID().toString())
                .build();
        CartItem cartItem = CartItem.builder()
                .id(UUID.randomUUID().toString())
                .cart(cart)
                .build();

        Mockito.when(cartItemRepository.findByCart(cart)).thenReturn(List.of(cartItem));

        cartItemService.clearAllItemFromCart(cart);

        Mockito.verify(cartItemRepository, Mockito.times(1)).deleteAll(Mockito.anyList());
    }

    @Test
    void shouldDeleteCartItemWhenDeleteCartItem() {
        String cartItemId = UUID.randomUUID().toString();
        Cart cart = Cart.builder()
                .id(UUID.randomUUID().toString())
                .build();
        CartItem cartItem = CartItem.builder()
                .id(cartItemId)
                .cart(cart)
                .build();

        Mockito.when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));

        cartItemService.deleteCartItem(cartItemId);

        Mockito.verify(cartItemRepository, Mockito.times(1)).delete(cartItem);
    }
}